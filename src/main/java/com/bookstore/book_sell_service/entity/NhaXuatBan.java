package com.bookstore.book_sell_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "NHA_XUAT_BAN")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhaXuatBan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maNXB;
    private String tenNXB;

    @OneToMany(mappedBy = "nhaXuatBan")
    private List<Sach> sachList;
}
