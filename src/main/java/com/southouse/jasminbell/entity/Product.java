package com.southouse.jasminbell.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

/**
 * packageName    : com.southouse.jasminbell.entity
 * fileName       : Product
 * author         : southouse
 * date           : 2023/07/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/24        southouse       최초 생성
 */

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public class Product {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(nullable = false, unique = true)
    private String code;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String supplier;

    @Column(name = "supplier_option")
    private String supplierOption;

    // 현재 잔미송 수량
    @Column(name = "reserved_count")
    private int reservedCount;

    // 현재 잔미송 건수
    @Column(name = "reserved_case")
    private int reservedCase;

    // 입고 대기
    @Column(name = "stocked_waiting")
    private int stockedWaiting;

    // 금일 입고
    @Column(name = "stocked_today")
    private int stockedToday;

    // 최신화 여부
    @Column(name = "is_update")
    private Boolean isUpdate;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Product(Long no, String code, String name, String description, String supplier, String supplierOption, int reservedCount, int reservedCase, int stockedWaiting, int stockedToday, Boolean isUpdate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.no = no;
        this.code = code;
        this.name = name;
        this.description = description;
        this.supplier = supplier;
        this.supplierOption = supplierOption;
        this.reservedCount = reservedCount;
        this.reservedCase = reservedCase;
        this.stockedWaiting = stockedWaiting;
        this.stockedToday = stockedToday;
        this.isUpdate = isUpdate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void updateProductByExcel(String code, String supplier, String name, String supplierOption, int stockedWaiting, int stockedToday, Boolean isUpdate) {
        this.code = code;
        this.supplier = supplier;
        this.name = name;
        this.supplierOption = supplierOption;
        this.stockedWaiting = stockedWaiting;
        this.stockedToday = stockedToday;
        this.isUpdate = isUpdate;
    }
}
