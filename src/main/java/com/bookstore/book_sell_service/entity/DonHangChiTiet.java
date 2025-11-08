package com.bookstore.book_sell_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "DON_HANG_CHI_TIET")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonHangChiTiet {
    @EmbeddedId
    private DonHangChiTietId id;

    private Integer soLuongMua;
    private Double giaMua;

    @ManyToOne
    @MapsId("maDonHang")
    @JoinColumn(name = "ma_don_hang")
    private DonHang donHang;

    @ManyToOne
    @MapsId("maSach")
    @JoinColumn(name = "ma_sach")
    @JsonIgnore
    private Sach sach;

    public DonHangChiTietId getId() {
        return id;
    }
    public void setId(DonHangChiTietId id) {
        this.id = id;
    }
    public Integer getSoLuongMua() {
        return soLuongMua;
    }
    public void setSoLuongMua(Integer soLuongMua) {
        this.soLuongMua = soLuongMua;
    }
    public Double getGiaMua() {
        return giaMua;
    }
    public void setGiaMua(Double giaMua) {
        this.giaMua = giaMua;
    }
    public DonHang getDonHang() {
        return donHang;
    }
    public void setDonHang(DonHang donHang) {
        this.donHang = donHang;
    }
    public Sach getSach() {
        return sach;
    }
    public void setSach(Sach sach) {
        this.sach = sach;
    }
}
