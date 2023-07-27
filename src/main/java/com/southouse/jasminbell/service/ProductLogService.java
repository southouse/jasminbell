package com.southouse.jasminbell.service;

import com.southouse.jasminbell.dto.Result;
import com.southouse.jasminbell.entity.Product;
import com.southouse.jasminbell.entity.ProductLog;
import com.southouse.jasminbell.entity.StockedStatus;
import com.southouse.jasminbell.repository.ProductLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        return productLogRepository.findByProductAndIsDeleteFalse(product);
    }

    public Page<ProductLog> getProductLogsByCode(String code, Pageable pageable) {
        return productLogRepository.findByProductCodeAndIsDeleteFalse(code, pageable);
    }

    public Result createProductLog(ProductLog productLog) {
        Result result = new Result();

        result.setSuccess(true);
        productLogRepository.save(productLog);

        return result;
    }

    public Result updateLog(Long no, String memo, int stockedCount) {
        Result result = new Result();

        ProductLog productLog = productLogRepository.findByNo(no).orElseThrow(() -> new RuntimeException("로그를 찾을 수 없습니다."));
        productLog.updateProductLog(memo, stockedCount);

        if (productLog.getRequestCount() <= productLog.getStockedCount())
            productLog.setStockedStatus(StockedStatus.COMPLETE);
        else if (productLog.getStockedCount() > 0)
            productLog.setStockedStatus(StockedStatus.A_PART_OF_STOCKED);
        else if (productLog.getStockedCount() == 0)
            productLog.setStockedStatus(StockedStatus.INITIAL);

        productLogRepository.save(productLog);

        result.setSuccess(true);

        return result;
    }

    public Result completeLog(Long no) {
        Result result = new Result();

        ProductLog productLog = productLogRepository.findByNo(no).orElseThrow(() -> new RuntimeException("로그를 찾을 수 없습니다."));

//        요청 수량과 입고 수량이 같아도 완료 처리할 수 있어야 함
//        if (productLog.getRequestCount() != productLog.getStockedCount()) {
//            result.setMessage("요청 수량과 입고 수량이 같아 완료 처리할 수 없습니다.");
//            return result;
//        }

        result.setSuccess(true);
        productLog.setStockedStatus(StockedStatus.COMPLETE);
        productLogRepository.save(productLog);

        return result;
    }

    public Result deleteLog(Long no) {
        Result result = new Result();

        ProductLog productLog = productLogRepository.findByNo(no).orElseThrow(() -> new RuntimeException("로그를 찾을 수 없습니다."));

        result.setSuccess(true);
        productLog.setDelete(true);
        productLogRepository.save(productLog);

        return result;
    }
}
