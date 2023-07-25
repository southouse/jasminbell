package com.southouse.jasminbell.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * packageName    : com.southouse.jasminbell.entity
 * fileName       : Excel
 * author         : southouse
 * date           : 2023/07/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/24        southouse       최초 생성
 */

@Entity
@Table(name = "excel")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Excel {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(name = "sync_time")
    private LocalDateTime syncTime;

    @Column
    private String name;

    @Column
    private String path;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Excel(Long no, LocalDateTime syncTime, String name, String path, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.no = no;
        this.syncTime = syncTime;
        this.name = name;
        this.path = path;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
