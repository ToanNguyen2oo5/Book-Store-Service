package com.bookstore.book_sell_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "TAC_GIA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TacGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maTG;

    private String tenTG;

    @ManyToMany(mappedBy = "tacGiaSet")
    private Set<Sach> sachSet;
}
