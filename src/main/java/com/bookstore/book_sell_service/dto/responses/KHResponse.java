package com.bookstore.book_sell_service.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KHResponse {
    private String maKH;
    private String hoTen;
    private String matKhau;
    private String email;
    private String soDT;
    private String diaChi;
    private String tenQuanHuyen;
    private String tenTinh;
    }
