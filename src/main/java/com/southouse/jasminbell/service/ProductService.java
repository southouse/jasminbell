package com.southouse.jasminbell.service;

import com.southouse.jasminbell.entity.Product;
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

    private final ProductRepository productRepository;

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> getProducts(String searchKeyWord, Pageable pageable) {
        return productRepository.findByNameContaining(searchKeyWord, pageable);
    }

    public Page<Product> getProductsByNeedUpdate(Pageable pageable) {
        return productRepository.findAllByIsUpdateFalse(pageable);
    }

    public Page<Product> getProductsByNeedUpdate(String searchKeyWord, Pageable pageable) {
        return productRepository.findAllByNameContainingAndIsUpdateFalse(searchKeyWord, pageable);
    }

    public Page<Product> getProductsByReserved(Pageable pageable, int reservedCount, int reservedCase) {
        return productRepository.findAllByReservedCountGreaterThanOrReservedCaseGreaterThan(pageable, reservedCount, reservedCase);
    }

    public Page<Product> getProductsByReserved(Pageable pageable, int reservedCount, int reservedCase, String searchKeyWord) {
        return productRepository.findAllByReservedCountGreaterThanOrReservedCaseGreaterThanAndNameContaining(pageable, reservedCount, reservedCase, searchKeyWord);
    }

    public Product getProduct(String code) {
        return productRepository.findByCode(code);
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public void save(List<Product> products) {
        productRepository.saveAll(products);
    }

}
