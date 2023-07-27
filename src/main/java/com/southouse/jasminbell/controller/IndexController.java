package com.southouse.jasminbell.controller;

import com.southouse.jasminbell.dto.Result;
import com.southouse.jasminbell.entity.Excel;
import com.southouse.jasminbell.entity.Product;
import com.southouse.jasminbell.entity.ProductLog;
import com.southouse.jasminbell.entity.StockedStatus;
import com.southouse.jasminbell.service.ExcelService;
import com.southouse.jasminbell.service.ProductLogService;
import com.southouse.jasminbell.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    private final ExcelService excelService;

    @GetMapping
    public String index(Model model
            , @RequestParam(required = false) String name
            , @RequestParam(required = false) String supplierOption
            , Pageable pageable) {
        Page<Product> products;
        Page<Product> productsByNeedUpdate;
        Page<Product> productsByReserved;

        products = productService.getProducts(name, supplierOption, pageable);
        productsByNeedUpdate = productService.getProductsByNeedUpdate(name, supplierOption, pageable);
        productsByReserved = productService.getProductsByReserved(name, supplierOption, pageable);

        Excel excel = excelService.getExcelByLastSync();

        model.addAttribute("excel", excel);
        model.addAttribute("productList", products);
        model.addAttribute("productListByNeedUpdate", productsByNeedUpdate);
        model.addAttribute("productListByReserved", productsByReserved);
        model.addAttribute("currentPage", products.getNumber());
        model.addAttribute("totalPage", products.getTotalPages());
        return "index";
    }

    @GetMapping("detail")
    public String detail(@RequestParam String code, Model model, Pageable pageable) {
        Page<ProductLog> productLogs = productLogService.getProductLogsByCode(code, pageable);

        Product product = productService.getProduct(code);

        model.addAttribute("createProductLog", new ProductLog());
        model.addAttribute("product", product);
        model.addAttribute("productLogList", productLogs);
        model.addAttribute("currentPage", productLogs.getNumber());
        model.addAttribute("totalPage", productLogs.getTotalPages());
        model.addAttribute("totalElements", productLogs.getTotalElements());

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

        return "redirect:/detail?code=" + createProductLog.getProduct().getCode() + "&page=0&size=30&sort=requestDate,asc";
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
