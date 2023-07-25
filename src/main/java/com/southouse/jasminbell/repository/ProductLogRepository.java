package com.southouse.jasminbell.repository;

import com.southouse.jasminbell.entity.Product;
import com.southouse.jasminbell.entity.ProductLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

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
public interface ProductLogRepository extends JpaRepository<ProductLog, Long> {

    List<ProductLog> findByProduct(Product product);

    List<ProductLog> findByProductCode(String code);

}
