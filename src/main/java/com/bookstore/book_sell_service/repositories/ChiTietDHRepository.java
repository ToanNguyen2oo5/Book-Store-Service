package com.bookstore.book_sell_service.repositories;

import com.bookstore.book_sell_service.entity.ChiTietGioHang;
import com.bookstore.book_sell_service.entity.ChiTietGioHangId;
import com.bookstore.book_sell_service.entity.DonHangChiTiet;
import com.bookstore.book_sell_service.entity.DonHangChiTietId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChiTietDHRepository extends JpaRepository<DonHangChiTiet, DonHangChiTietId > {
}
