package com.bookstore.book_sell_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "SACH")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maSach;
    private String tenSach;
    private Double donGia;
    private String donViTinh;
    private Integer soLuongCo;
    private String khoHang;
    private String moTa;

    @ManyToOne
    @JoinColumn(name = "maLoai")
    private LoaiSach loaiSach;

    @ManyToOne
    @JoinColumn(name = "maNXB")
    private NhaXuatBan nhaXuatBan;

    @OneToMany(mappedBy = "sach")
    private List<HinhAnh> hinhAnhList;

    @OneToMany(mappedBy = "sach")
    private List<ChiTietGioHang> chiTietGioHangList;

    @OneToMany(mappedBy = "sach")
    private List<DonHangChiTiet> donHangChiTietList;

    @ManyToMany
    @JoinTable(
            name = "SACH_TAC_GIA",
            joinColumns = @JoinColumn(name = "maSach"),
            inverseJoinColumns = @JoinColumn(name = "maTG")
    )
    private Set<TacGia> tacGiaSet;


}
