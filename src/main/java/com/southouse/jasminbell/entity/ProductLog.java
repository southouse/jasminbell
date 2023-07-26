package com.southouse.jasminbell.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * packageName    : com.southouse.jasminbell.entity
 * fileName       : ProductLog
 * author         : southouse
 * date           : 2023/07/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/24        southouse       최초 생성
 */

@Entity
@Table(name = "product_log")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ProductLog {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @JoinColumn
    @ManyToOne
    private Product product;

    @Column(name = "request_date")
    private LocalDateTime requestDate;

    // 요청 수량
    @Column(name = "request_count")
    private int requestCount;

    @Column
    private String memo;

    @Column(name = "stocked_status")
    @Enumerated(EnumType.STRING)
    private StockedStatus stockedStatus = StockedStatus.INITIAL;

    // 입고 수량
    @Column(name = "stocked_count")
    private int stockedCount;

    @Column(name = "is_delete")
    private boolean isDelete;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    // 현재 잔미송 개수
    public int getReservedCount() {
        if (this.stockedStatus.name().equals("COMPLETE"))
            return 0;
        else
            return requestCount - stockedCount;
    }

    // 현재 잔미송 건수
    public int getReservedCase() {
        if (this.stockedStatus.name().equals("COMPLETE"))
            return 0;
        else
            return 1;
    }

    public void updateProductLog(String memo, int stockedCount) {
        this.memo = memo;
        this.stockedCount = stockedCount;
    }

    @Builder
    public ProductLog(Long no, Product product, LocalDateTime requestDate, int requestCount, String memo, StockedStatus stockedStatus, int stockedCount, boolean isDelete, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.no = no;
        this.product = product;
        this.requestDate = requestDate;
        this.requestCount = requestCount;
        this.memo = memo;
        this.stockedStatus = stockedStatus;
        this.stockedCount = stockedCount;
        this.isDelete = isDelete;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
