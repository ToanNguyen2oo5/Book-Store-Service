package com.bookstore.book_sell_service.services;

import com.bookstore.book_sell_service.dto.request.ApiResponse;
import com.bookstore.book_sell_service.dto.request.QuanHuyenRequest;
import com.bookstore.book_sell_service.entity.QuanHuyen;
import com.bookstore.book_sell_service.mapper.QuanHuyenMapper;
import com.bookstore.book_sell_service.repositories.QuanHuyenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class QuanHuyenService {
    QuanHuyenRepository quanHuyenRepository;
    QuanHuyenMapper quanHuyenMapper;
    public QuanHuyen createQuanHuyen(QuanHuyenRequest request){
        QuanHuyen quanHuyen = quanHuyenMapper.toQuanHuyen(request);
        return quanHuyenRepository.save(quanHuyen);
    }
}
