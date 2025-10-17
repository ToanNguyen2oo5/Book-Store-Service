package com.bookstore.book_sell_service.controller;


import com.bookstore.book_sell_service.dto.request.ApiResponse;
import com.bookstore.book_sell_service.dto.request.GioHang.GioHangRequest;
import com.bookstore.book_sell_service.entity.GioHang;
import com.bookstore.book_sell_service.services.GioHangService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/kh")
public class GioHangController {

    GioHangService gioHangService;

    @PostMapping("/addGH")
    public ApiResponse<GioHang> gioHangApiResponse (@RequestBody GioHangRequest request){
        GioHang gioHang = gioHangService.addSachGioHang(request);
        return  ApiResponse.<GioHang>builder()
                .message("oko")
                .result(gioHang)
                .build();
    }
}
