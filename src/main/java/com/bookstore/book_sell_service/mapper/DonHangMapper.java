package com.bookstore.book_sell_service.mapper;

import com.bookstore.book_sell_service.dto.request.DonHang.DonHangCreate;
import com.bookstore.book_sell_service.entity.DonHang;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DonHangMapper {
    DonHang toDonHang (DonHangCreate donHangCreate);
}
