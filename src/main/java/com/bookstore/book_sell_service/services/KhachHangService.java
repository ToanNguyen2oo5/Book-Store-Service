package com.bookstore.book_sell_service.services;

import com.bookstore.book_sell_service.dto.request.KhachHang.KhachHangCreationRequest;
import com.bookstore.book_sell_service.dto.request.KhachHang.KhachHangUpdateRequest;
import com.bookstore.book_sell_service.dto.responses.KHResponse;
import com.bookstore.book_sell_service.entity.KhachHang;
import com.bookstore.book_sell_service.entity.QuanHuyen;
import com.bookstore.book_sell_service.entity.Tinh;
import com.bookstore.book_sell_service.exception.AppException;
import com.bookstore.book_sell_service.exception.ErrorCode;
import com.bookstore.book_sell_service.mapper.UserMapper;
import com.bookstore.book_sell_service.repositories.KhachHangRepository;
import com.bookstore.book_sell_service.repositories.QuanHuyenRepository;
import com.bookstore.book_sell_service.repositories.TinhRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class KhachHangService {
    TinhRepository tinhRepository;
    QuanHuyenRepository quanHuyenRepository;
    KhachHangRepository khachHangRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public  KhachHang createKhachHang(KhachHangCreationRequest request){
        QuanHuyen quanHuyen = quanHuyenRepository.findById(request.getMaQuanHuyen())
                .orElseThrow(() -> new RuntimeException("QuanHuyen not found"));
        KhachHang khachHang=userMapper.toUser(request);
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

    public KHResponse updateKH(String maKH, KhachHangUpdateRequest request){
        KhachHang khachHang =khachHangRepository.findById(maKH)
                .orElseThrow(() -> new RuntimeException("user not found"));
        userMapper.updateKH(khachHang, request);
        khachHang.setMatKhau(passwordEncoder.encode(request.getMatKhau()));
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
