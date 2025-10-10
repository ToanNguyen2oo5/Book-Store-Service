package com.bookstore.book_sell_service.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "KHACH_HANG")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String maKH;
    private String hoTen;
    private String matKhau;
    private String email;
    //@Column(unique = true)
    private String soDT;
    private String diaChi;
    private Set<String> roles;

    @ManyToOne
    @JoinColumn(name = "maQuanHuyen")
    private QuanHuyen quanHuyen;

    @OneToMany(mappedBy = "khachHang")

    private List<GioHang> gioHangList;

    @OneToMany(mappedBy = "khachHang")

    private List<DanhGia> danhGiaList;

    @OneToMany(mappedBy = "khachHang")
    private List<DonHang> donHangList;
}
