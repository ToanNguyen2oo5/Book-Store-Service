package com.bookstore.book_sell_service.services;

import com.bookstore.book_sell_service.dto.request.KhachHangCreationRequest;
import com.bookstore.book_sell_service.dto.request.KhachHangUpdateRequest;
import com.bookstore.book_sell_service.dto.responses.KHResponse;
import com.bookstore.book_sell_service.entity.KhachHang;
import com.bookstore.book_sell_service.entity.QuanHuyen;
import com.bookstore.book_sell_service.mapper.UserMapper;
import com.bookstore.book_sell_service.repositories.KhachHangRepository;
import com.bookstore.book_sell_service.repositories.QuanHuyenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
    QuanHuyenRepository quanHuyenRepository;
    KhachHangRepository khachHangRepository;
    UserMapper userMapper;

    public  KhachHang createKhachHang(KhachHangCreationRequest request){
        QuanHuyen quanHuyen = quanHuyenRepository.findById(request.getMaQuanHuyen())
                .orElseThrow(() -> new RuntimeException("QuanHuyen not found"));
        KhachHang khachHang=userMapper.toUser(request);
        khachHang.setQuanHuyen(quanHuyen);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        khachHang.setMatKhau(passwordEncoder.encode(request.getMatKhau()));
        return khachHangRepository.save(khachHang);
    }

    public KhachHang getKhachHang(@PathVariable  String maKH){
        return khachHangRepository.findById(maKH)
                .orElseThrow(() -> new RuntimeException("user not found"));
    }

    public List<KHResponse> getAllKhachHangs(){
    return khachHangRepository.findAll().stream().
                map(userMapper::toKHResponse).collect(Collectors.toList());
    }

    public KHResponse updateKH(String maKH, KhachHangUpdateRequest request){
        KhachHang khachHang =khachHangRepository.findById(maKH)
                .orElseThrow(() -> new RuntimeException("user not found"));
        userMapper.updateKH(khachHang,request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        khachHang.setMatKhau(passwordEncoder.encode(request.getMatKhau()));
        return userMapper.toKHResponse(khachHangRepository.save(khachHang));
    }

    public void deleteKH(String maKH){
        khachHangRepository.deleteById(maKH);
    }
}
