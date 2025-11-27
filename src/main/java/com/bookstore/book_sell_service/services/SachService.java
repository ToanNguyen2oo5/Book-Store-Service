package com.bookstore.book_sell_service.services;

import com.bookstore.book_sell_service.dto.request.Sach.SachFilterRequest;
import com.bookstore.book_sell_service.dto.request.Sach.SachUpdateRequest;
import com.bookstore.book_sell_service.dto.responses.SachResponse;
import com.bookstore.book_sell_service.entity.Sach;
import com.bookstore.book_sell_service.mapper.SachMapper;
import com.bookstore.book_sell_service.repositories.SachRepository;
import com.bookstore.book_sell_service.specification.SachSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SachService {
    SachRepository sachRepository;
    SachMapper sachMapper;

    // get all sach theo khoang gia hoac sap xep theo gia giam dan hay tang dan
    @PreAuthorize("isAuthenticated()")
    public List<Sach> getAllSachs(SachFilterRequest request){
        return sachRepository.findAll
                (SachSpecification.filterByPrice(request.getMinPrice(),request.getMaxPrice(),request.getOrderBy(),request.getOrder()));
    }

    @PreAuthorize("isAuthenticated()")
    public SachResponse getSach (@PathVariable  Long maSach){
        return sachMapper.toSachResponse(sachRepository.findById(maSach)
                .orElseThrow(()->new RuntimeException("book not found")));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public SachResponse updateSach(SachUpdateRequest request, Long maSach){
            Sach sach = sachRepository.findById(maSach).orElseThrow(()-> new RuntimeException("book not found"));
            sachMapper.updateSach(sach,request);
            return sachMapper.toSachResponse(sachRepository.save(sach));
    }
    public void deleteSach(Long maSach){
        sachRepository.deleteById(maSach);
    }
}
