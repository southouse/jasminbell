package com.southouse.jasminbell.service;

import com.southouse.jasminbell.entity.Excel;
import com.southouse.jasminbell.entity.Product;
import com.southouse.jasminbell.repository.ExcelRepository;
import com.southouse.jasminbell.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.Iterator;
import java.util.List;

/**
 * packageName    : com.southouse.jasminbell.service
 * fileName       : ExcelService
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
public class ExcelService {

    private final ProductRepository productRepository;
    private final ExcelRepository excelRepository;

    public Excel getExcelByLastSync() {
        return excelRepository.findFirst1ByOrderBySyncTimeDesc();
    }

    public void uploadExcelFile(MultipartFile file) {
        List<Product> productList = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            DataFormatter dataFormatter = new DataFormatter();

            Workbook workbook;
            if (file.getOriginalFilename().endsWith(".xls")) {
                // OLE2 포맷인 경우
                workbook = new HSSFWorkbook(inputStream);
            } else if (file.getOriginalFilename().endsWith(".xlsx")) {
                // OOXML 포맷인 경우
                workbook = new XSSFWorkbook(inputStream);
            } else {
                // 유효하지 않은 파일 포맷인 경우
                throw new IllegalArgumentException("Invalid file format");
            }

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Skip the header row
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                Iterator<Cell> cellIterator = row.cellIterator();

                String code = getStringCellValue(cellIterator.next());
                String supplier = getStringCellValue(cellIterator.next());
                String name = getStringCellValue(cellIterator.next());
                String supplierOption = getStringCellValue(cellIterator.next());
                int stockedWaiting = (int) getNumericCellValue(cellIterator.next());
                int stockedToday = (int) getNumericCellValue(cellIterator.next());

                Product productByCode = productRepository.findByCode(code);

                if (productByCode == null) {
                    Product product = Product.builder()
                            .code(code)
                            .supplier(supplier)
                            .name(name)
                            .supplierOption(supplierOption)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .stockedWaiting(stockedWaiting)
                            .stockedToday(stockedToday)
                            .isUpdate(stockedWaiting == 0 ? true : false) // 신규 상품 등록의 경우에 입고 대기가 0이 아니면 최신화 대상
                            .build();

                    productList.add(product);
                } else {
                    Boolean isUpdate = productByCode.getReservedCount() == stockedWaiting ? true : false;

                    productByCode.updateProductByExcel(code, supplier, name, supplierOption, stockedWaiting, stockedToday, isUpdate);
                    productList.add(productByCode);
                }

            }
        } catch (IOException e) {
            // Handle the exception
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // productList에 추출한 데이터가 들어있습니다.
        // 여기서 원하는 로직을 추가하여 데이터 처리 가능
        productRepository.saveAll(productList);
        excelRepository.save(Excel.builder()
                .syncTime(LocalDateTime.now())
                .name(file.getOriginalFilename())
                .path(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build());
    }

    private String excelTypeMapping(Cell cell) {

        switch (cell.getCellType()){
            case FORMULA:
                return cell.getCellFormula();
            case NUMERIC:
                return cell.getNumericCellValue()+"";
            case STRING:
                return cell.getStringCellValue()+"";
            case BOOLEAN:
                return cell.getBooleanCellValue()+"";
            case ERROR:
                return cell.getErrorCellValue()+"";
        }

        return "null";
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC || cell.getCellType() == CellType.FORMULA) {
            return String.valueOf((int) cell.getNumericCellValue());
        } else {
            return "";
        }
    }

    private double getNumericCellValue(Cell cell) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) {
            return 0.0;
        }

        return cell.getNumericCellValue();
    }
}
