package com.bookstore.book_sell_service.controller;

import com.bookstore.book_sell_service.dto.request.ApiResponse;
import com.bookstore.book_sell_service.dto.request.DanhGia.DanhGiaRequest;
import com.bookstore.book_sell_service.entity.DanhGia;
import com.bookstore.book_sell_service.services.DanhGiaService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/danh_gia")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class DanhGiaCotroller {

    DanhGiaService danhGiaService;

    @PostMapping
    public ApiResponse<DanhGia> creataDanhGia (@RequestBody DanhGiaRequest danhGiaRequest){
        DanhGia danhGia =  danhGiaService.createDanhGia(danhGiaRequest);
        return ApiResponse.<DanhGia>builder()
                .message("Thanh cong")
                .result(danhGia)
                .build();
    }
}
