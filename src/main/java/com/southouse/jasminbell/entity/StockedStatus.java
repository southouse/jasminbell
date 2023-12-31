package com.southouse.jasminbell.entity;

import lombok.Getter;

/**
 * packageName    : com.southouse.jasminbell.entity
 * fileName       : StockedStatus
 * author         : southouse
 * date           : 2023/07/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/24        southouse       최초 생성
 */

@Getter
public enum StockedStatus {

    INITIAL("대기"),
    A_PART_OF_STOCKED("부분입고"),
    COMPLETE("완료"),
    ;

    private final String displayName;


    StockedStatus(String displayName) {
        this.displayName = displayName;
    }
}
