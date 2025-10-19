package com.bookstore.book_sell_service.services;

import com.bookstore.book_sell_service.dto.request.AuthenticationRequest;
import com.bookstore.book_sell_service.dto.request.IntrospectRequest;
import com.bookstore.book_sell_service.dto.request.LogOut_Refresh.LogoutRequest;
import com.bookstore.book_sell_service.dto.request.LogOut_Refresh.RefreshRequest;
import com.bookstore.book_sell_service.dto.responses.AuthenticationResponse;
import com.bookstore.book_sell_service.dto.responses.IntrospectResponse;
import com.bookstore.book_sell_service.entity.InvalidateToken;
import com.bookstore.book_sell_service.entity.KhachHang;
import com.bookstore.book_sell_service.exception.AppException;
import com.bookstore.book_sell_service.exception.ErrorCode;
import com.bookstore.book_sell_service.repositories.InvalidatedTokenRepository;
import com.bookstore.book_sell_service.repositories.KhachHangRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true )
public class AuthenticationService {
    KhachHangRepository khachHangRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    PasswordEncoder passwordEncoder;
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESH_DURATION;


    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        var token =request.getToken();
        JWSVerifier verifier ;

            verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);
        signedJWT.verify((verifier));
        Date expiryTime=signedJWT.getJWTClaimsSet().getExpirationTime();
    var verified =signedJWT.verify(verifier);

    return IntrospectResponse.builder()
            .valid(verified && expiryTime.after(new Date()))
            .build();

    }

   public AuthenticationResponse authenticate(AuthenticationRequest request){
    var user =khachHangRepository.findByUserName(request.getUserName())
            .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean authenticated= passwordEncoder.matches(request.getMatKhau(), user.getMatKhau());
        if(!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();

    }

    public void logout(LogoutRequest request)  throws ParseException, JOSEException{
        try {
            var signJWT = verifyToken(request.getToken(),true);

            String jit = signJWT.getJWTClaimsSet().getJWTID();
            var expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();

            InvalidateToken invalidateToken =
                    InvalidateToken.builder().id(jit).expiryTime(expiryTime).build();

            invalidatedTokenRepository.save(invalidateToken);

        } catch (AppException exception){
            log.info("Token already expired");
        }
    }

    public AuthenticationResponse refreshToken (RefreshRequest request) throws ParseException, JOSEException {
        var signJWT = verifyToken(request.getToken(), true);

        var jit = signJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();

        InvalidateToken invalidatedTToken =
                InvalidateToken.builder().id(jit).expiryTime(expiryTime).build();

        var username = signJWT.getJWTClaimsSet().getSubject();

        var user = khachHangRepository.findByUserName(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();

    }


    private SignedJWT verifyToken(String token , boolean isRefresh) throws JOSEException, ParseException {

        // tao verify xac thuc chu ky token
        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY);

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expotyTime = (isRefresh) ?
                new Date(signedJWT
                        .getJWTClaimsSet()
                        .getIssueTime()
                        .toInstant()
                        .plus(REFRESH_DURATION,ChronoUnit.SECONDS)
                        .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(jwsVerifier);

        if (!(verified && expotyTime.after(new Date()))) throw  new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;


    }

    private String generateToken(KhachHang khachHang){
        JWSHeader jwsHeader= new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(khachHang.getUserName())
                .issuer("stewie.vn")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope",buildScope(khachHang))
                .build();

        Payload payload=new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject=new JWSObject(jwsHeader,payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
        } catch (JOSEException e) {
            log.error("cannot create token");
            throw new RuntimeException(e);
        }
        return jwsObject.serialize();
    }
    private String buildScope(KhachHang khachHang) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(khachHang.getRoles()))
            khachHang.getRoles().forEach(stringJoiner::add);
        return stringJoiner.toString();

    }

    public KhachHang khachHang () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return khachHangRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }
}
