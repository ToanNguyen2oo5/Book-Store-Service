package com.bookstore.book_sell_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
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
