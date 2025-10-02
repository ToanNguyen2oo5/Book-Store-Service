package com.bookstore.book_sell_service.repositories;

import com.bookstore.book_sell_service.entity.Tinh;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TinhRepository extends JpaRepository<Tinh,Long> {
    Optional<Tinh> findByTenTinh(String tenTinh);
}
