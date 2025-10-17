package com.bookstore.book_sell_service.services;

import com.bookstore.book_sell_service.dto.request.GioHang.GioHangRequest;
import com.bookstore.book_sell_service.entity.*;
import com.bookstore.book_sell_service.exception.AppException;
import com.bookstore.book_sell_service.exception.ErrorCode;
import com.bookstore.book_sell_service.repositories.ChiTietGioHangRepository;
import com.bookstore.book_sell_service.repositories.GioHangRepository;
import com.bookstore.book_sell_service.repositories.KhachHangRepository;
import com.bookstore.book_sell_service.repositories.SachRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GioHangService {

    GioHangRepository gioHangRepository;
    ChiTietGioHangRepository chiTietGioHangRepository;
    KhachHangRepository khachHangRepository;
    SachRepository sachRepository;

    @Transactional
    public GioHang addSachGioHang(GioHangRequest request) {
        // Lấy thông tin user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        KhachHang user = khachHangRepository.findByuserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Lấy sách
        Sach sach = sachRepository.findById(request.getMaSach())
                .orElseThrow(() -> new RuntimeException("Loi ko tim thay sach"));

        // Tìm hoặc tạo giỏ hàng
        GioHang gioHang = gioHangRepository.findByKhachHang_UserName(userName)
                .orElseGet(() -> {
                    GioHang newGioHang = new GioHang();
                    newGioHang.setKhachHang(user);
                    newGioHang.setChiTietGioHangList(new ArrayList<>());
                    return gioHangRepository.saveAndFlush(newGioHang);
                });

        // ✅ Kiểm tra sách đã tồn tại bằng method custom
        Optional<ChiTietGioHang> chiTietOptional =
                chiTietGioHangRepository.findByGioHangAndSach(gioHang, sach);

        if (chiTietOptional.isEmpty()) {
            // ✅ Tạo ID trước
            ChiTietGioHangId chiTietId = new ChiTietGioHangId(
                    gioHang.getMaGioHang(),
                    sach.getMaSach()
            );

            // ✅ Tạo chi tiết mới VÀ SET ID
            ChiTietGioHang newChiTiet = ChiTietGioHang.builder()
                    .id(chiTietId)  // ← QUAN TRỌNG: Phải set ID
                    .gioHang(gioHang)
                    .sach(sach)
                    .soLuongMua(2)
                    .build();

            chiTietGioHangRepository.save(newChiTiet);
        }

        return gioHangRepository.findById(gioHang.getMaGioHang())
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tìm thấy"));
    }
}