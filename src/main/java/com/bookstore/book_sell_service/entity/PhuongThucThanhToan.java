package com.bookstore.book_sell_service.entity;

import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "PHUONG_THUC_THANH_TOAN")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhuongThucThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maPTTT;

    private String tenPTTT;

    @OneToMany(mappedBy = "phuongThucThanhToan")
    private List<DonHang> donHangList;
}
