package com.bookstore.book_sell_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "LOAI_SACH")
public class LoaiSach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maLoai;

    private String tenLoai;

    @OneToMany(mappedBy = "loaiSach")
    private List<Sach> sachList;

    public Long getMaLoai() {
        return maLoai;
    }
    public void setMaLoai(Long maLoai) {
        this.maLoai = maLoai;
    }
    public String getTenLoai() {
        return tenLoai;
    }
    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }
    public List<Sach> getSachList() {
        return sachList;
    }
    public void setSachList(List<Sach> sachList) {
        this.sachList = sachList;
    }
}
