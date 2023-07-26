package com.southouse.jasminbell.controller;

import com.southouse.jasminbell.dto.Result;
import com.southouse.jasminbell.entity.Product;
import com.southouse.jasminbell.entity.ProductLog;
import com.southouse.jasminbell.entity.StockedStatus;
import com.southouse.jasminbell.service.ProductLogService;
import com.southouse.jasminbell.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

            if (product.getReservedCount() == product.getStockedWaiting())
                product.setIsUpdate(true);
        }

        productService.save(products);

        model.addAttribute("productList", products);
        return "index";
    }

    @GetMapping("detail")
    public String detail(@RequestParam String code, Model model) {
        List<ProductLog> productLogs = productLogService.getProductLogsByCode(code);

        Product product = productService.getProduct(code);

        model.addAttribute("createProductLog", new ProductLog());
        model.addAttribute("product", product);
        model.addAttribute("productLogList", productLogs);

        return "detail";
    }

    @PostMapping("log/create")
    public String createProductLog(
            @ModelAttribute ProductLog createProductLog
            , @RequestParam String date
            , @RequestParam String productCode
            , Model model
    ) {
        createProductLog.setRequestDate(LocalDateTime.parse(date + "T00:00:00"));
        createProductLog.setProduct(productService.getProduct(productCode));

        Result result = productLogService.createProductLog(createProductLog);

        model.addAttribute("result", result);
        model.addAttribute("createProductLog", createProductLog);
        model.addAttribute("date", date);

        return "redirect:/detail?code=" + createProductLog.getProduct().getCode();
    }

    @PutMapping("log/update")
    @ResponseBody
    public Result updateLog(@RequestParam Long no
                            , @RequestParam String memo
                            , @RequestParam int stockedCount
                            , Model model) {
        Result result = productLogService.updateLog(no, memo, stockedCount);

        model.addAttribute("result", result);

        return result;
    }

    @PutMapping("log/complete")
    @ResponseBody
    public Result completeLog(@RequestParam Long no, Model model) {
        Result result = productLogService.completeLog(no);

        model.addAttribute("result", result);

        return result;
    }

    @DeleteMapping("log/delete")
    @ResponseBody
    public Result deleteLog(@RequestParam Long no, Model model) {
        Result result = productLogService.deleteLog(no);

        model.addAttribute("result", result);

        return result;
    }

}
