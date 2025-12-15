package com.bookstore.book_sell_service.mapper;

import com.bookstore.book_sell_service.dto.request.Sach.SachCreationalRequest;
import com.bookstore.book_sell_service.dto.request.Sach.SachUpdateRequest;
import com.bookstore.book_sell_service.dto.responses.SachResponse;
import com.bookstore.book_sell_service.entity.Sach;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SachMapper {
  //  @Mapping(target = "tacGiaSet",source = "tacGiaSet")
    SachResponse toSachResponse(Sach sach);
    Sach toSach(SachCreationalRequest request);
    void updateSach(@MappingTarget Sach sach,SachUpdateRequest request);
    List<SachResponse> toListSachResponse (List<Sach>sach);
}
