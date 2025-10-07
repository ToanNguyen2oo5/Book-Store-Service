package com.bookstore.book_sell_service.controller;

import com.bookstore.book_sell_service.dto.request.ApiResponse;
import com.bookstore.book_sell_service.dto.request.KhachHangCreationRequest;
import com.bookstore.book_sell_service.dto.request.KhachHangUpdateRequest;
import com.bookstore.book_sell_service.dto.responses.KHResponse;
import com.bookstore.book_sell_service.entity.KhachHang;
import com.bookstore.book_sell_service.services.KhachHangService;
import jakarta.validation.Valid;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/kh")
public class KhachHangController {

    KhachHangService khachHangService;

    @PostMapping
    public ApiResponse<KhachHang> createKhachHang(@RequestBody @Valid KhachHangCreationRequest request){
    ApiResponse<KhachHang> apiResponse = new ApiResponse<>();
    apiResponse.setResult(khachHangService.createKhachHang(request));
    return apiResponse;
    }

    @GetMapping
    public ApiResponse<List<KHResponse>> getAllKhachHangs(){
        return ApiResponse.<List<KHResponse>>builder()
                .result(khachHangService.getAllKhachHangs())
                .build();
    }

    @GetMapping("/{maKH}")
    public ApiResponse<KhachHang> getKhachHang(@PathVariable String maKH){
        return ApiResponse.<KhachHang>builder()
                .result(khachHangService.getKhachHang(maKH))
                .build();
    }

    @PutMapping("/{maKH}")
    ApiResponse<KHResponse> updateUser(@PathVariable String maKH, @RequestBody @Valid KhachHangUpdateRequest request ){
        return ApiResponse.<KHResponse>builder()
                .result(khachHangService.updateKH(maKH,request))
                .build();
    }
    @DeleteMapping("/{maKH}")
    String deleteUser(@PathVariable String maKH){
        khachHangService.deleteKH(maKH);
        return "KH has been deleted";
    }
}
