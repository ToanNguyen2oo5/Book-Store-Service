package com.bookstore.book_sell_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private LocalDateTime ngayBL;

    @ManyToOne
    @JoinColumn(name = "maSP")
    @JsonIgnore
    private Sach sach;

    @ManyToOne
    @JoinColumn(name = "maKH")
    @JsonIgnore
    private KhachHang khachHang;
}
