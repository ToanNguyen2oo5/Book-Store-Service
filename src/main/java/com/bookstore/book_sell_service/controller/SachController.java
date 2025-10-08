package com.bookstore.book_sell_service.controller;

import com.bookstore.book_sell_service.dto.request.ApiResponse;
import com.bookstore.book_sell_service.entity.Sach;
import com.bookstore.book_sell_service.services.SachService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping("/kh")
public class SachController {

    SachService sachService;

    @PostMapping ("/request-sach")
    public ApiResponse<List<Sach>> getAllSachs(@RequestBody SachFilterRequest request){
        List<Sach> sachList = sachService.getAllSachs(request);
        return ApiResponse.<List<Sach>>builder()
                .message("Lay thanh cong")
                .result(sachList)
                .build();
    }

}
