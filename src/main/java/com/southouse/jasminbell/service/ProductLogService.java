package com.southouse.jasminbell.service;

import com.southouse.jasminbell.dto.Result;
import com.southouse.jasminbell.entity.Product;
import com.southouse.jasminbell.entity.ProductLog;
import com.southouse.jasminbell.entity.StockedStatus;
import com.southouse.jasminbell.repository.ProductLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * packageName    : com.southouse.jasminbell.service
 * fileName       : ProductLogService
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
public class ProductLogService {

    private final ProductLogRepository productLogRepository;

    public List<ProductLog> getProductLogs() {
        return productLogRepository.findAll();
    }

    public List<ProductLog> getProductLogsByProduct(Product product) {
        return productLogRepository.findByProduct(product);
    }

    public List<ProductLog> getProductLogsByCode(String code) {
        return productLogRepository.findByProductCode(code);
    }

    public Result completeLog(ProductLog productLog) {
        Result result = new Result();

        if (productLog.getRequestCount() != productLog.getStockedCount()) {
            result.setMessage("요청 수량과 입고 수량이 같아 완료 처리할 수 없습니다.");
            return result;
        }

        result.setSuccess(true);
        productLog.setStockedStatus(StockedStatus.COMPLETE);

        return result;
    }

    public Result deleteLog(ProductLog productLog) {
        return new Result();
    }
}
