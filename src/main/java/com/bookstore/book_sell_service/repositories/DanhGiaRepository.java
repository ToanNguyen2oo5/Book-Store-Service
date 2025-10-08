package com.bookstore.book_sell_service.repositories;

import com.bookstore.book_sell_service.entity.DanhGia;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DanhGiaRepository extends JpaRepository<DanhGia,Long> {
}
