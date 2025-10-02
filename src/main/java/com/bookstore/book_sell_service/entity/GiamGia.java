package com.bookstore.book_sell_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "GIAM_GIA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiamGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maGiamGia;

    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private Double chietKhau;
    private String moTa;

    @OneToMany(mappedBy = "giamGia")
    private List<DonHang> donHangList;

    @OneToMany(mappedBy = "giamGia")
    private List<SachDanhGia> sachDanhGiaList;

}
