package com.bookstore.book_sell_service.services;

import com.bookstore.book_sell_service.dto.request.KhachHang.KhachHangCreationRequest;
import com.bookstore.book_sell_service.dto.request.KhachHang.KhachHangUpdateRequest;
import com.bookstore.book_sell_service.dto.responses.KHResponse;
import com.bookstore.book_sell_service.entity.KhachHang;
import com.bookstore.book_sell_service.entity.QuanHuyen;
import com.bookstore.book_sell_service.enums.Role;
import com.bookstore.book_sell_service.exception.AppException;
import com.bookstore.book_sell_service.exception.ErrorCode;
import com.bookstore.book_sell_service.mapper.UserMapper;
import com.bookstore.book_sell_service.repositories.KhachHangRepository;
import com.bookstore.book_sell_service.repositories.QuanHuyenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class KhachHangService {
    QuanHuyenRepository quanHuyenRepository;
    KhachHangRepository khachHangRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public  KhachHang createKhachHang(KhachHangCreationRequest request){
        QuanHuyen quanHuyen = quanHuyenRepository.findById(request.getMaQuanHuyen())
                .orElseThrow(() -> new RuntimeException("QuanHuyen not found"));
        KhachHang khachHang=userMapper.toUser(request);
        var roles = new HashSet<String>();
        roles.add(Role.USER.name());
        khachHang.setRoles(roles);
        khachHang.setUserName(request.getUserName());
        khachHang.setQuanHuyen(quanHuyen);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        khachHang.setMatKhau(passwordEncoder.encode(request.getMatKhau()));
        return khachHangRepository.save(khachHang);
    }

    @PostAuthorize("returnObject.hoTen == authentication.name")
    public KhachHang getKhachHang(@PathVariable  String maKH){
        return khachHangRepository.findById(maKH)
                .orElseThrow(() -> new RuntimeException("user not found"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<KHResponse> getAllKhachHangs(){
    return khachHangRepository.findAll().stream().
                map(userMapper::toKHResponse).collect(Collectors.toList());
    }
    @PreAuthorize("hasRole('ADMIN')")
    public KHResponse updateKH(String maKH, KhachHangUpdateRequest request){
        KhachHang khachHang =khachHangRepository.findById(maKH)
                .orElseThrow(() -> new RuntimeException("user not found"));
        userMapper.updateKH(khachHang, request);
        khachHang.setMatKhau(passwordEncoder.encode(request.getMatKhau()));
        QuanHuyen quanHuyen = quanHuyenRepository.findById(request.getMaQuanHuyen())
                .orElseThrow(() -> new RuntimeException("Loi"));
        khachHang.setQuanHuyen(quanHuyen);
        return userMapper.toKHResponse(khachHangRepository.save(khachHang));
    }

    public void deleteKH(String maKH){
        khachHangRepository.deleteById(maKH);
    }

    public KHResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        KhachHang khachHang = khachHangRepository.findByUserName(name).orElseThrow(
                ()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toKHResponse(khachHang);
    }
}
