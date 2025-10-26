package com.bookstore.book_sell_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "DON_HANG")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maDonHang;

    private String tenNguoiNhan;
    private String soDTNguoiNhan;
    private String email;
    private String diaChiGiaoHang;
    private String ghiChu;
    private Double phiGiaoHang;
    private String trangThai;
    private LocalDate ngayDat;
    private double tongTien;
    @ManyToOne
    @JoinColumn(name = "maQuanHuyen")
    private QuanHuyen quanHuyen;

    @ManyToOne
    @JoinColumn(name = "maPTTT")
    private PhuongThucThanhToan phuongThucThanhToan;

    @ManyToOne
    @JoinColumn(name = "maKH")
    @JsonBackReference(value = "khachhang-donhang")

    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "maNV")
    private NhanVien nhanVien;

    @ManyToOne
    @JoinColumn(name = "maGiamGia")
    private GiamGia giamGia;

    @OneToMany(mappedBy = "donHang")
    @JsonManagedReference
    private List<DonHangChiTiet> chiTietDonHangList;


}
