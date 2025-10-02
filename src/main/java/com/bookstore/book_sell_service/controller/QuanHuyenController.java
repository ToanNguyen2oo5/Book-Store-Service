package com.bookstore.book_sell_service.controller;

import com.bookstore.book_sell_service.dto.request.ApiResponse;
import com.bookstore.book_sell_service.dto.request.QuanHuyenRequest;

import com.bookstore.book_sell_service.entity.QuanHuyen;
import com.bookstore.book_sell_service.services.QuanHuyenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quan-huyen")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class QuanHuyenController {
    QuanHuyenService quanHuyenService;

    @PostMapping
    ApiResponse<QuanHuyen> createQuanHuyen(@RequestBody QuanHuyenRequest request){
        ApiResponse<QuanHuyen> apiResponse =new ApiResponse<>();
        apiResponse.setResult(quanHuyenService.createQuanHuyen(request));
        return apiResponse;
    }
}
