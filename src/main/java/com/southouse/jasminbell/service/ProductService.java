package com.southouse.jasminbell.service;

import com.southouse.jasminbell.entity.Product;
import com.southouse.jasminbell.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
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
