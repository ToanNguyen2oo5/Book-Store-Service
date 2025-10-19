package com.bookstore.book_sell_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SACH_GIAM_GIA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SachGiamGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer soLuongApDungGiamGia;

    @ManyToOne
    @JoinColumn(name = "maSach")
    private Sach sach;

    @ManyToOne
    @JoinColumn(name = "maGiamGia")
    private GiamGia giamGia;
}
