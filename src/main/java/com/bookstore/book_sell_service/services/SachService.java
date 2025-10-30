package com.bookstore.book_sell_service.services;

import com.bookstore.book_sell_service.dto.request.Sach.SachFilterRequest;
import com.bookstore.book_sell_service.entity.Sach;
import com.bookstore.book_sell_service.repositories.SachRepository;
import com.bookstore.book_sell_service.specification.SachSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SachService {
    SachRepository sachRepository;

    // get all sach theo khoang gia hoac sap xep theo gia giam dan hay tang dan
    public List<Sach> getAllSachs(SachFilterRequest request){
        return sachRepository.findAll
                (SachSpecification.filterByPrice(request.getMinPrice(),request.getMaxPrice(),request.getOrderBy(),request.getOrder()));
    }

}
