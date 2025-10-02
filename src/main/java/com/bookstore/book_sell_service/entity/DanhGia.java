package com.bookstore.book_sell_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DANH_GIA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DanhGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maDanhGia;

    private Integer soSao;
    private String binhLuan;

    @ManyToOne
    @JoinColumn(name = "maSP")
    private Sach sach;

    @ManyToOne
    @JoinColumn(name = "maKH")
    private KhachHang khachHang;
}
