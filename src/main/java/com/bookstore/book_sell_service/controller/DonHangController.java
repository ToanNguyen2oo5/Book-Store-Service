package com.bookstore.book_sell_service.controller;

import com.bookstore.book_sell_service.dto.request.ApiResponse;
import com.bookstore.book_sell_service.dto.request.DonHang.DonHangCreate;
import com.bookstore.book_sell_service.dto.request.DonHang.UpdateTrangThai;
import com.bookstore.book_sell_service.dto.responses.DHResponse;
import com.bookstore.book_sell_service.services.DonHangService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/don-hang")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class DonHangController {

    DonHangService donHangService;

    @PostMapping("/createDH")
    public ApiResponse<Void> crateDonHang (@RequestBody DonHangCreate donHangCreate){
        donHangService.createDonHang(donHangCreate);
        return ApiResponse.<Void>builder()
                .message("oko")
                .build();
    }
    @GetMapping("/getALLDH")
    public  ApiResponse<List<DHResponse>> getALlDH(){
        return ApiResponse.<List<DHResponse>>builder()
                .result( donHangService.getALLDonHang())
                .build();
    }
    //lay don hang theo ma
    @GetMapping("/getDH/{maDH}")
    public  ApiResponse<DHResponse> getDH(@PathVariable Long maDH){
        return ApiResponse.<DHResponse>builder()
                .result( donHangService.getDonHang(maDH))
                .build();
    }

    @PutMapping("/{maDonHang}/updateTT")
    public ApiResponse<Void> updateTT(@RequestBody UpdateTrangThai updateTrangThai){
        donHangService.updateTrangThai(updateTrangThai);
        return ApiResponse.<Void>builder()
                .message("oko")
                .build();
    }
    // danh sach don hang cua khach hang
    @GetMapping("/don_hang_of_KH")
    public ApiResponse<List<DHResponse>> getDHofKH(){
        List<DHResponse>donHangServiceDHofKH = donHangService.getDHofKH();
        return ApiResponse.<List<DHResponse>>builder()
                .message("oko")
                .result(donHangServiceDHofKH)
                .build();
    }
}
