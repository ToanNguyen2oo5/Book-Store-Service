package com.bookstore.book_sell_service.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum TrangThai {
    CHO_XAC_NHAN(1,"Chờ xác nhận"),
    DA_XAC_NHAN(2,"Đã xác nhận"),
    DANG_CHUAN_BI(3,"Đang chuẩn bị"),
    DANG_GIAO(4,"Đang giao"),
    DA_GIAO(5,"Đã giao"),
    DA_HUY(6,"Đã hủy"),
    TRA_HANG(7,"Trả hàng");

    private final String moTa;
    private final int value;
    TrangThai(int value ,String moTa) {
        this.value = value;
        this.moTa = moTa;
    }

    @JsonValue
    public String getMoTa() {
        return moTa;
    }

    public int getValue() {return value;}


}
