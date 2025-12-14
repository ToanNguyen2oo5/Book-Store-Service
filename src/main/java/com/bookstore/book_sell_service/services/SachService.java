package com.bookstore.book_sell_service.services;

import com.bookstore.book_sell_service.dto.request.Sach.SachCreationalRequest;
import com.bookstore.book_sell_service.dto.request.Sach.SachFilterRequest;
import com.bookstore.book_sell_service.dto.request.Sach.SachUpdateRequest;
import com.bookstore.book_sell_service.dto.responses.SachResponse;
import com.bookstore.book_sell_service.entity.NhaXuatBan;
import com.bookstore.book_sell_service.entity.Sach;
import com.bookstore.book_sell_service.entity.TacGia;
import com.bookstore.book_sell_service.mapper.SachMapper;
import com.bookstore.book_sell_service.mapper.TacGiaMapper;
import com.bookstore.book_sell_service.repositories.NhaXuatBanRepository;
import com.bookstore.book_sell_service.repositories.SachRepository;
import com.bookstore.book_sell_service.repositories.TacGiaRepository;
import com.bookstore.book_sell_service.repositories.TheLoaiRepository;
import com.bookstore.book_sell_service.specification.SachSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SachService {
    private final TacGiaMapper tacGiaMapper;
    SachRepository sachRepository;
    SachMapper sachMapper;
    TacGiaRepository tacGiaRepository;
    TheLoaiRepository theLoaiRepository;
    NhaXuatBanRepository nhaXuatBanRepository;

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public SachResponse createSach(SachCreationalRequest request){
        if(sachRepository.existsByTenSach(request.getTenSach())){
            throw new RuntimeException("Da co sach nay");
        }


        NhaXuatBan nhaXuatBan = nhaXuatBanRepository.findById(request.getNhaXuatBan().getMaNXB())
                .orElseThrow(()-> new RuntimeException("Khong thay ma loai"));

        Sach sach = sachMapper.toSach(request);
        sach.setLoaiSach(theLoaiRepository.findById(request.getLoaiSach().getMaLoai())
                .orElseThrow(()-> new RuntimeException("Khong thay ma loai")));

        sach.setNhaXuatBan(nhaXuatBan);
        List<TacGia> tacGiaSet = tacGiaRepository.findAllById(request.getTacGiaIds());
        sach.setTacGiaSet(new HashSet<>(tacGiaSet));

        return sachMapper.toSachResponse(sachRepository.save(sach));

    }

    // Lấy tất cả sách (không lọc) và convert sang Response
    public List<SachResponse> getAllSachs() {
        // sachRepository.findAll() là hàm có sẵn của JPA
        return sachRepository.findAll().stream()
                .map(sachMapper::toSachResponse)
                .collect(Collectors.toList());
    }

    // get all sach theo khoang gia hoac sap xep theo gia giam dan hay tang dan

    public List<Sach> getAllSachsByPrice(SachFilterRequest request){
        return sachRepository.findAll
                (SachSpecification.filterByPrice(request.getMinPrice(),request.getMaxPrice(),request.getOrderBy(),request.getOrder()));
    }


    public SachResponse getSach (@PathVariable  Long maSach){
        return sachMapper.toSachResponse(sachRepository.findById(maSach)
                .orElseThrow(()->new RuntimeException("book not found")));
    }

    public SachResponse updateSach(SachUpdateRequest request, Long maSach){
        Sach sach = sachRepository.findById(maSach)
                .orElseThrow(()-> new RuntimeException("book not found"));

        sachMapper.updateSach(sach, request);

        // 4. Logic Update Tác Giả
        if (request.getTacGiaIds() != null) {
            // findAllById: Lấy danh sách tác giả MỚI dựa trên ID được chọn
            List<TacGia> selectedAuthors = tacGiaRepository.findAllById(request.getTacGiaIds());

            // Ghi đè danh sách cũ bằng danh sách mới được chọn
            sach.setTacGiaSet(new HashSet<>(selectedAuthors));
        }

        return sachMapper.toSachResponse(sachRepository.save(sach));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public void deleteSach(Long maSach){
        sachRepository.deleteById(maSach);
    }

    public List<SachResponse> getSachsByLoai(Long maLoai){
        // Gọi repository để lấy list Entity
        List<Sach> sachList = sachRepository.findAllByLoaiSach_MaLoai(maLoai);

        // Convert list Entity sang list Response bằng Mapper
        return sachList.stream()
                .map(sachMapper::toSachResponse)
                .collect(Collectors.toList());
    }
}
