package com.bookstore.book_sell_service.services;

import com.bookstore.book_sell_service.dto.request.DonHang.DonHangCreate;
import com.bookstore.book_sell_service.dto.request.DonHang.UpdateTrangThai;
import com.bookstore.book_sell_service.dto.responses.DHResponse;
import com.bookstore.book_sell_service.dto.responses.KHResponse;
import com.bookstore.book_sell_service.dto.responses.ThongKeResponse;
import com.bookstore.book_sell_service.entity.*;
import com.bookstore.book_sell_service.enums.TrangThai;
import com.bookstore.book_sell_service.mapper.DonHangMapper;
import com.bookstore.book_sell_service.mapper.UserMapper;
import com.bookstore.book_sell_service.repositories.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
public class DonHangService {

    AuthenticationService authenticationService;
    KhachHangRepository khachHangRepository;
    ChiTietGioHangRepository chiTietGioHangRepository;
    DonHangMapper donHangMapper;
    GiamGiaRepository giamGiaRepository;
    NhanVienRepository nhanVienRepository;
    QuanHuyenRepository quanHuyenRepository;
    PTTTRepository ptttRepository;
    DonHangRepository donHangRepository;
    ChiTietDHRepository chiTietDHRepository;
    private final GioHangRepository gioHangRepository;
    UserMapper userMapper;

    @Transactional // Thêm transaction để đảm bảo tính toàn vẹn dữ liệu
    public void createDonHang(DonHangCreate request) {

        KhachHang khachHang = authenticationService.khachHang();

        if (khachHang.getGioHang() == null) {
            throw new RuntimeException("Giỏ hàng rỗng");
        }

        List<ChiTietGioHang> chiTietGioHangList = khachHang.getGioHang().getChiTietGioHangList();
        if (chiTietGioHangList.isEmpty()) {
            throw new RuntimeException("Giỏ hàng rỗng");
        }

        // Tính tổng tiền
        double tongTien = chiTietGioHangList.stream()
                .mapToDouble(ct -> ct.getSoLuongMua() * ct.getSach().getDonGia())
                .sum();

        // Áp dụng giảm giá nếu có
        GiamGia giamGia = null;
        if (request.getMaGiamGia() != null) {
            giamGia = giamGiaRepository.findById(request.getMaGiamGia())
                    .orElse(null);

            if (giamGia != null) {
                LocalDate ngayHienTai = request.getNgayDat();
                boolean check = (ngayHienTai.isAfter(giamGia.getNgayBatDau()) || ngayHienTai.isEqual(giamGia.getNgayBatDau()))
                        && (ngayHienTai.isEqual(giamGia.getNgayKetThuc()) || ngayHienTai.isBefore(giamGia.getNgayKetThuc()));

                if (check) {
                    tongTien = tongTien * (100 - giamGia.getChietKhau()) / 100;
                }
            }
        }

        tongTien = tongTien + request.getPhiGiaoHang();

        // Tạo đơn hàng
        DonHang donHang = donHangMapper.toDonHang(request);

        donHang.setQuanHuyen(quanHuyenRepository.findById(request.getMaQuanHuyen())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quận huyện")));

        donHang.setPhuongThucThanhToan(ptttRepository.findById(request.getMaPTTT())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phương thức thanh toán")));

        donHang.setKhachHang(khachHangRepository.findById(request.getMaKH())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng")));

        donHang.setNhanVien(nhanVienRepository.findById(request.getMaNV())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên")));
        donHang.setNgayDat(LocalDate.now());
        if (giamGia != null) {
            donHang.setGiamGia(giamGia);
        }

        donHang.setTrangThai(TrangThai.CHO_XAC_NHAN.getMoTa());
        donHang.setTongTien(tongTien);
        donHang.setNgayDat(LocalDate.now());

        // QUAN TRỌNG: Lưu đơn hàng chỉ 1 LẦN trước khi tạo chi tiết
        donHang = donHangRepository.save(donHang);

        // Tạo chi tiết đơn hàng
        List<DonHangChiTiet> chiTietDonHangList = new ArrayList<>();

        for (ChiTietGioHang ctgh : chiTietGioHangList) {
            DonHangChiTietId donHangChiTietId = new DonHangChiTietId(
                    donHang.getMaDonHang(),
                    ctgh.getId().getMaSach()
            );

            DonHangChiTiet donHangChiTiet = DonHangChiTiet.builder()
                    .id(donHangChiTietId)
                    .donHang(donHang) // Set reference đến đơn hàng
                    .sach(ctgh.getSach()) // Set reference đến sách
                    .soLuongMua(ctgh.getSoLuongMua())
                    .giaMua(ctgh.getSoLuongMua() * ctgh.getSach().getDonGia())
                    .build();

            chiTietDonHangList.add(donHangChiTiet);
        }

        // Lưu tất cả chi tiết đơn hàng
        chiTietDHRepository.saveAll(chiTietDonHangList);

        // Xóa giỏ hàng
        GioHang gioHang = khachHang.getGioHang();

        gioHang.getChiTietGioHangList().clear();
        chiTietGioHangRepository.deleteByGioHang_MaGioHang(gioHang.getMaGioHang());

        gioHangRepository.save(gioHang);
    }

    // lay danh sach cac don hang tu admin
    @PreAuthorize("hasRole('ADMIN')")
    public List<DHResponse> getALLDonHang(){
        List<DonHang> donHangList = donHangRepository.findAll();

        return donHangList.stream()
                .map(ct -> {
                    KhachHang kh = ct.getKhachHang();
                    KHResponse khResponse = userMapper.toKHResponse(kh);

                    DHResponse dhResponse = donHangMapper.togetDH(ct);

                    // Gán KHResponse vào DHResponse
                    dhResponse.setKhResponse(khResponse);

                    return dhResponse;
                })
                .collect(Collectors.toList());
    }


    // xem chi tiet don hang
    @PreAuthorize("hasRole('ADMIN')")
    public DHResponse getDonHang(Long maDH){
        DonHang donHang = donHangRepository.findById(maDH)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        // Map KhachHang → KHResponse
        KhachHang kh = donHang.getKhachHang();
        KHResponse khResponse = userMapper.toKHResponse(kh);

        DHResponse dhResponse = donHangMapper.togetDH(donHang);

        // Gán KHResponse vào DHResponse
        dhResponse.setKhResponse(khResponse);

        return dhResponse;
    }

    // cap nhat trang thai don hang
    @PreAuthorize("hasRole('ADMIN')")
    public void updateTrangThai(UpdateTrangThai updateTrangThai)
    {
        DonHang donHang =  donHangRepository.findById(updateTrangThai.getMaDonHang())
                .orElseThrow(() -> new RuntimeException("Ko tim thay don hang"));

        TrangThai tt = TrangThai.valueOf(updateTrangThai.getTrangThaiMoi());
        donHang.setTrangThai(tt.getMoTa());

        donHangRepository.save(donHang);
    }

    // lay danh sach don hang cua khach hang
    public List<DHResponse> getDHofKH (){
        KhachHang khachHang = authenticationService.khachHang();
        List<DonHang> donHangList = donHangRepository.findAllByKhachHang_maKH(khachHang.getMaKH());

        return donHangList.stream()
                .map(ct -> {
                    KhachHang kh = ct.getKhachHang();
                    KHResponse khResponse = userMapper.toKHResponse(kh);

                    DHResponse dhResponse = donHangMapper.togetDH(ct);

                    // Gán KHResponse vào DHResponse
                    dhResponse.setKhResponse(khResponse);

                    return dhResponse;
                })
                .collect(Collectors.toList());
    }


}