package com.bookstore.book_sell_service.repositories;

import com.bookstore.book_sell_service.entity.QuanHuyen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuanHuyenRepository extends JpaRepository<QuanHuyen,Long> {
    Optional<QuanHuyen> findByTenQuanHuyen(String tenQuanHuyen);
}
