package com.bookstore.book_sell_service.mapper;

import com.bookstore.book_sell_service.dto.request.Sach.SachUpdateRequest;
import com.bookstore.book_sell_service.dto.responses.SachResponse;
import com.bookstore.book_sell_service.entity.Sach;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SachMapper {
    SachResponse toSachResponse(Sach sach);

    void updateSach(@MappingTarget Sach sach,SachUpdateRequest request);
}
