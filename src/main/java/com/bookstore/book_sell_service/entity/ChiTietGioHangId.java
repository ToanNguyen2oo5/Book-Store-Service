package com.bookstore.book_sell_service.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class ChiTietGioHangId implements Serializable {
    private Long maGioHang;
    private Long maSach;

    // getters and setters, hashCode, equals
    public Long getMaGioHang() {
        return maGioHang;
    }
    public void setMaGioHang(Long maGioHang) {
        this.maGioHang = maGioHang;
    }
    public Long getMaSach() {
        return maSach;
    }
    public void setMaSach(Long maSach) {
        this.maSach = maSach;
    }
}
