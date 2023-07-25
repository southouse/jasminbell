package com.southouse.jasminbell.controller;

import com.southouse.jasminbell.entity.Product;
import com.southouse.jasminbell.entity.ProductLog;
import com.southouse.jasminbell.service.ProductLogService;
import com.southouse.jasminbell.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * packageName    : com.southouse.jasminbell.controller
 * fileName       : IndexController
 * author         : southouse
 * date           : 2023/07/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/24        southouse       최초 생성
 */

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class IndexController {

    private final ProductService productService;
    private final ProductLogService productLogService;

    @GetMapping
    public String index(Model model) {
        List<Product> products = productService.getProducts();

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
        }
        
        model.addAttribute("productList", products);
        return "index";
    }

    @GetMapping("detail")
    public String update(@RequestParam String code, Model model) {
        List<ProductLog> productLogs = productLogService.getProductLogsByCode(code);
        model.addAttribute("productLogList", productLogs);

        return "detail";
    }

}
