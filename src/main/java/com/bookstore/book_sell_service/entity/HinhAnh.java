package com.bookstore.book_sell_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "HINH_ANH" )
public class HinhAnh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maHinhAnh")
    private Long maHinhAnh; // Mã hình ảnh (PK)

    // FK: MaSP (Giả định MaSP là Mã Sách, Many-to-One với Entity Book)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maSach", nullable = false)
    private Sach sach;
}
