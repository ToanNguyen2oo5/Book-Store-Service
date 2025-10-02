package com.bookstore.book_sell_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CHI_TIET_GIO_HANG")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChiTietGioHang {
    @EmbeddedId
    private ChiTietGioHangId id;

    private Integer soLuongMua;

    @ManyToOne
    @MapsId("maGioHang")
    @JoinColumn(name = "maGioHang")
    private GioHang gioHang;

    @ManyToOne
    @MapsId("maSach")
    @JoinColumn(name = "maSach")
    private Sach sach;
    }

