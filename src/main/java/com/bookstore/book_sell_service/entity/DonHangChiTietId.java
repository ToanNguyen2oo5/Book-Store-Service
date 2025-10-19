package com.bookstore.book_sell_service.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Embeddable
@Data
@NoArgsConstructor
public class DonHangChiTietId implements Serializable {
    private Long maDonHang;
    private Long maSach;

    public DonHangChiTietId(Long maDonHang, Long maSach) {
    }

    public Long getMaDonHang() {
        return maDonHang;
    }
    public void setMaDonHang(Long maDonHang) {
        this.maDonHang = maDonHang;
    }
    public Long getMaSach() {
        return maSach;
    }
    public void setMaSach(Long maSach) {
        this.maSach = maSach;
    }
}
