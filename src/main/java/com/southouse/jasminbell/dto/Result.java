package com.southouse.jasminbell.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * packageName    : com.southouse.jasminbell.dto
 * fileName       : Result
 * author         : southouse
 * date           : 2023/07/25
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/25        southouse       최초 생성
 */

@Getter
@Setter
@NoArgsConstructor
public class Result {

    private boolean isSuccess = false;
    private String message;

}
