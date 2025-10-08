package com.bookstore.book_sell_service.repositories;

import com.bookstore.book_sell_service.entity.InvalidateToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidateToken, String> {
}
