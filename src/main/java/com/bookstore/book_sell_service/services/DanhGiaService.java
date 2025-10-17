package com.bookstore.book_sell_service.services;

import com.bookstore.book_sell_service.dto.request.DanhGia.DanhGiaRequest;
import com.bookstore.book_sell_service.entity.DanhGia;
import com.bookstore.book_sell_service.entity.KhachHang;
import com.bookstore.book_sell_service.entity.Sach;
import com.bookstore.book_sell_service.exception.AppException;
import com.bookstore.book_sell_service.exception.ErrorCode;
import com.bookstore.book_sell_service.repositories.DanhGiaRepository;
import com.bookstore.book_sell_service.repositories.KhachHangRepository;
import com.bookstore.book_sell_service.repositories.SachRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class DanhGiaService {

    KhachHangRepository khachHangRepository;
    DanhGiaRepository danhGiaRepository;
    SachRepository sachRepository;

    @PreAuthorize("hasRole('USER')")
    public DanhGia createDanhGia(DanhGiaRequest danhGiaRequest){


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userName =  authentication.getName();

        KhachHang khachHang = khachHangRepository.findByuserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Sach sach = sachRepository.findById(danhGiaRequest.getMaSach())
                .orElseThrow(() -> new RuntimeException("Khong tim thay sach"));

        DanhGia danhGia = new DanhGia();
        danhGia.setSoSao(danhGiaRequest.getSoSao());
        danhGia.setBinhLuan(danhGiaRequest.getBinhLuan());
        danhGia.setKhachHang(khachHang);
        danhGia.setSach(sach);

        return danhGiaRepository.save(danhGia);




    }
}
