package com.bookstore.book_sell_service.repositories;

import com.bookstore.book_sell_service.entity.DonHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonHangRepository extends JpaRepository<DonHang,Long> {
}
