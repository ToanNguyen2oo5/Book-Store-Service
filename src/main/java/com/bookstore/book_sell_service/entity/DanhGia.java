package com.bookstore.book_sell_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "DANH_GIA")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DanhGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maDanhGia;

    private Integer soSao;
    private String binhLuan;
    private LocalDate ngayBL;

    @ManyToOne
    @JoinColumn(name = "maSP")
    private Sach sach;

    @ManyToOne
    @JoinColumn(name = "maKH")
    @JsonIgnore
    private KhachHang khachHang;
}
