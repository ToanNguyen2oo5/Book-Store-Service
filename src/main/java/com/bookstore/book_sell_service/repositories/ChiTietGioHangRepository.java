package com.bookstore.book_sell_service.repositories;

import com.bookstore.book_sell_service.entity.ChiTietGioHang;
import com.bookstore.book_sell_service.entity.ChiTietGioHangId;
import com.bookstore.book_sell_service.entity.GioHang;
import com.bookstore.book_sell_service.entity.Sach;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChiTietGioHangRepository extends JpaRepository<ChiTietGioHang, ChiTietGioHangId> {
    Optional<ChiTietGioHang> findByGioHangAndSach(GioHang gioHang, Sach sach);

    void deleteByGioHangAndSach(GioHang gioHang, Sach sach);

    void deleteByGioHang_MaGioHang(Long maGioHang);
}