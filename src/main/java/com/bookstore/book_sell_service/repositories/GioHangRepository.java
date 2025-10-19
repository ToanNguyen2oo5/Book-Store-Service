package com.bookstore.book_sell_service.repositories;

import com.bookstore.book_sell_service.entity.DonHang;
import com.bookstore.book_sell_service.entity.GioHang;
import com.bookstore.book_sell_service.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GioHangRepository extends JpaRepository<GioHang,Long> {
    Optional<GioHang> findByKhachHang_maKH(Optional<KhachHang> khachHang);
    Optional<GioHang> findByKhachHang_UserName (String userName);

    void deleteByKhachHang(KhachHang khachHang);

    void deleteByMaGioHang(Long maGioHang);
}
