package com.bookstore.book_sell_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "GIO_HANG")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GioHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maGioHang;

    @ManyToOne
    @JoinColumn(name = "maKH")
    private KhachHang khachHang;

    @OneToMany(mappedBy = "gioHang")
    private List<ChiTietGioHang> chiTietGioHangList;
}
