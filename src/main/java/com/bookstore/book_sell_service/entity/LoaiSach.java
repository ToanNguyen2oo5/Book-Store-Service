package com.bookstore.book_sell_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "loai_sach")
public class LoaiSach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maLoai;

    private String tenLoai;

    private String moTa;

    @OneToMany(mappedBy = "loaiSach")
    private List<Sach> sachList;

}
