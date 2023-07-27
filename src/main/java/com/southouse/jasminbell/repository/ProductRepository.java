package com.southouse.jasminbell.repository;

import com.southouse.jasminbell.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAll(Pageable pageable);
    Page<Product> findAllByIsUpdateFalse(Pageable pageable);
    Page<Product> findAllByNameContainingAndIsUpdateFalse(String name, Pageable pageable);
    Page<Product> findAllBySupplierOptionContainingAndIsUpdateFalse(String supplierOption, Pageable pageable);
    Page<Product> findAllByReservedCountGreaterThanOrReservedCaseGreaterThan(Pageable pageable, int reservedCount, int reservedCase);
    Page<Product> findAllByReservedCountGreaterThanOrReservedCaseGreaterThanAndNameContaining(Pageable pageable, int reservedCount, int reservedCase, String name);
    Page<Product> findAllByReservedCountGreaterThanOrReservedCaseGreaterThanAndSupplierOptionContaining(Pageable pageable, int reservedCount, int reservedCase, String supplierOption);

    Page<Product> findByNameContaining(String name, Pageable pageable);
    Page<Product> findBySupplierOptionContaining(String supplierOption, Pageable pageable);

    Product findByCode(String code);

}
