package com.southouse.jasminbell.service;

import com.southouse.jasminbell.entity.Product;
import com.southouse.jasminbell.entity.ProductLog;
import com.southouse.jasminbell.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * packageName    : com.southouse.jasminbell.service
 * fileName       : IndexService
 * author         : southouse
 * date           : 2023/07/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/24        southouse       최초 생성
 */

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductLogService productLogService;
    private final ProductRepository productRepository;

    public Page<Product> getProducts(String name, String supplierOption, Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);

        // 상품 로그에서 현재 잔미송 체크 후 저장
        this.reservedSaveFromLog(products.getContent());

        if (name != null)
            products = productRepository.findByNameContaining(name, pageable);
        else if (supplierOption != null)
            products = productRepository.findBySupplierOptionContaining(supplierOption, pageable);

        return products;
    }

    public Page<Product> getProductsByNeedUpdate(String name, String supplierOption, Pageable pageable) {
        Page<Product> products = productRepository.findAllByIsUpdateFalse(pageable);

        // 상품 로그에서 현재 잔미송 체크 후 저장
        this.reservedSaveFromLog(products.getContent());

        if (name != null)
            products = productRepository.findAllByNameContainingAndIsUpdateFalse(name, pageable);
        else if (supplierOption != null)
            products = productRepository.findAllBySupplierOptionContainingAndIsUpdateFalse(name, pageable);

        return products;
    }

    public Page<Product> getProductsByReserved(String name, String supplierOption, Pageable pageable) {
        Page<Product> products = productRepository.findAllByReservedCountGreaterThanOrReservedCaseGreaterThan(pageable, 0, 0);

        // 상품 로그에서 현재 잔미송 체크 후 저장
        this.reservedSaveFromLog(products.getContent());

        if (name != null)
            products = productRepository.findAllByReservedCountGreaterThanOrReservedCaseGreaterThanAndNameContaining(pageable, 0, 0, name);
        else if (supplierOption != null)
            products = productRepository.findAllByReservedCountGreaterThanOrReservedCaseGreaterThanAndSupplierOptionContaining(pageable, 0, 0, supplierOption);

        return products;
    }

    public Product getProduct(String code) {
        return productRepository.findByCode(code);
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public void reservedSaveFromLog(List<Product> products) {
        for (Product product : products) {
            int reservedCount = 0;
            int reservedCase = 0;

            List<ProductLog> productLogsByCode = productLogService.getProductLogsByProduct(product);

            for (ProductLog productLog : productLogsByCode) {
                reservedCount += productLog.getReservedCount();
                reservedCase += productLog.getReservedCase();
            }

            product.setReservedCount(reservedCount);
            product.setReservedCase(reservedCase);

            if (product.getReservedCount() == product.getStockedWaiting())
                product.setIsUpdate(true);
        }

        productRepository.saveAll(products);
    }

}
