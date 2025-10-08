package com.bookstore.book_sell_service.repositories;

import com.bookstore.book_sell_service.entity.Sach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SachRepository extends JpaRepository<Sach,Long>, JpaSpecificationExecutor<Sach> {

}
