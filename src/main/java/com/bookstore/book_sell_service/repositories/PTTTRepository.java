package com.bookstore.book_sell_service.repositories;

import com.bookstore.book_sell_service.entity.PhuongThucThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PTTTRepository extends JpaRepository<PhuongThucThanhToan,Long> {
}
