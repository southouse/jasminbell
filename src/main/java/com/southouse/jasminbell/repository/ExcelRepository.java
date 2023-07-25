package com.southouse.jasminbell.repository;

import com.southouse.jasminbell.entity.Excel;
import com.southouse.jasminbell.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName    : com.southouse.jasminbell.repository
 * fileName       : ProductRepository
 * author         : southouse
 * date           : 2023/07/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/24        southouse       최초 생성
 */
public interface ExcelRepository extends JpaRepository<Excel, Long> {
}
