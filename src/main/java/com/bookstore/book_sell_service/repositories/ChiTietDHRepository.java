package com.bookstore.book_sell_service.repositories;

import com.bookstore.book_sell_service.entity.DonHangChiTiet;
import com.bookstore.book_sell_service.entity.DonHangChiTietId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChiTietDHRepository extends JpaRepository<DonHangChiTiet, DonHangChiTietId > {
}
