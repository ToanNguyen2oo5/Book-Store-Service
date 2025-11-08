package com.bookstore.book_sell_service.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@Embeddable
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DonHangChiTietId implements Serializable {
    private Long maDonHang;
    private Long maSach;


}
