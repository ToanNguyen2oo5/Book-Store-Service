package com.bookstore.book_sell_service.controller;

import com.bookstore.book_sell_service.dto.request.ApiResponse;
import com.bookstore.book_sell_service.dto.request.DonHang.DonHangCreate;
import com.bookstore.book_sell_service.services.DonHangService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/don-hang")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class DonHangController {

    DonHangService donHangService;

    @PostMapping
    public ApiResponse<Void> crateDonHang (@RequestBody DonHangCreate donHangCreate){
        donHangService.createDonHang(donHangCreate);
        return ApiResponse.<Void>builder()
                .message("oko")
                .build();
    }
}
