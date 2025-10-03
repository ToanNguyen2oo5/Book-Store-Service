package com.bookstore.book_sell_service.services;

import com.bookstore.book_sell_service.dto.request.AuthenticationRequest;
import com.bookstore.book_sell_service.dto.request.IntrospectRequest;
import com.bookstore.book_sell_service.dto.responses.AuthenticationResponse;
import com.bookstore.book_sell_service.dto.responses.IntrospectResponse;
import com.bookstore.book_sell_service.exception.AppException;
import com.bookstore.book_sell_service.exception.ErrorCode;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true )
public class AuthenticationService {
    KhachHangRepository khachHangRepository;
    @NonFinal
    public static final String SIGNER_KEY="1TjXchw5FloESb63Kc+DFhTARvpWL4jUGCwfGWxuG5SIf/1y/LgJxHnMqaF6A/ij";

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
    var user =khachHangRepository.findByHoTen(request.getHoTen())
            .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder(10);
        boolean authenticated= passwordEncoder.matches(request.getMatKhau(), user.getMatKhau());
        if(!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        var token = generateToken(request.getHoTen());
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();

    }

    private String generateToken(String username){
        JWSHeader jwsHeader= new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("stewie.vn")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("CustomClaims","Custom ")
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
}
