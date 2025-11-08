package com.bookstore.book_sell_service.dto.responses;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DanhGiaResponse {

    private String hoTen;
    private String tenSach;
    private Integer soSao;
    private String binhLuan;
    private LocalDate ngayBL;
    private Long maDanhGia;
}
