package com.southouse.jasminbell.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
    private StockedStatus stockedStatus;

    // 입고 수량
    @Column(name = "stocked_count")
    private int stockedCount;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

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

    public void setStockedStatus(StockedStatus stockedStatus) {
        this.stockedStatus = stockedStatus;
    }

}
