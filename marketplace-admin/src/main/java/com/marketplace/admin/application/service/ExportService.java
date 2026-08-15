package com.marketplace.admin.application.service;

import com.marketplace.admin.application.dto.*;
import com.marketplace.admin.domain.entity.TopVendor;
import com.marketplace.admin.domain.entity.TopProduct;
import com.marketplace.admin.domain.repository.DashboardRepository;
import com.marketplace.admin.domain.valueobject.ExportFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExportService {

    private final DashboardRepository dashboardRepository;
    private static final String EXPORT_DIR = "exports/";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public byte[] exportTopVendors(List<TopVendor> vendors, ExportFormat format) throws IOException {
        return switch (format) {
            case CSV -> exportVendorsToCsv(vendors);
            case EXCEL -> exportVendorsToExcel(vendors);
            default -> exportVendorsToCsv(vendors);
        };
    }

    public byte[] exportTopProducts(List<TopProduct> products, ExportFormat format) throws IOException {
        return switch (format) {
            case CSV -> exportProductsToCsv(products);
            case EXCEL -> exportProductsToExcel(products);
            default -> exportProductsToCsv(products);
        };
    }

    private byte[] exportVendorsToCsv(List<TopVendor> vendors) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CSVWriter writer = new CSVWriter(new OutputStreamWriter(bos));

        writer.writeNext(new String[]{
                "Rank", "Vendor ID", "Store Name", "Owner Name", "Total Products",
                "Total Orders", "Total Revenue", "Commission Paid", "Average Rating", "Joined At"
        });

        for (TopVendor vendor : vendors) {
            writer.writeNext(new String[]{
                    String.valueOf(vendor.getRank()),
                    String.valueOf(vendor.getVendorId()),
                    vendor.getStoreName(),
                    vendor.getOwnerName(),
                    String.valueOf(vendor.getTotalProducts()),
                    String.valueOf(vendor.getTotalOrders()),
                    vendor.getTotalRevenue().toString(),
                    vendor.getCommissionPaid().toString(),
                    vendor.getAverageRating() != null ? vendor.getAverageRating().toString() : "N/A",
                    vendor.getJoinedAt() != null ? vendor.getJoinedAt().toString() : "N/A"
            });
        }

        writer.close();
        return bos.toByteArray();
    }

    private byte[] exportVendorsToExcel(List<TopVendor> vendors) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Top Vendors");

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        String[] headers = {"Rank", "Vendor ID", "Store Name", "Owner Name", "Total Products",
                "Total Orders", "Total Revenue", "Commission Paid", "Average Rating", "Joined At"};

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (TopVendor vendor : vendors) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(vendor.getRank());
            row.createCell(1).setCellValue(vendor.getVendorId());
            row.createCell(2).setCellValue(vendor.getStoreName());
            row.createCell(3).setCellValue(vendor.getOwnerName());
            row.createCell(4).setCellValue(vendor.getTotalProducts());
            row.createCell(5).setCellValue(vendor.getTotalOrders());
            row.createCell(6).setCellValue(vendor.getTotalRevenue().doubleValue());
            row.createCell(7).setCellValue(vendor.getCommissionPaid().doubleValue());
            row.createCell(8).setCellValue(vendor.getAverageRating() != null ? vendor.getAverageRating() : 0);
            row.createCell(9).setCellValue(vendor.getJoinedAt() != null ? vendor.getJoinedAt().toString() : "N/A");
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();
        return bos.toByteArray();
    }

    private byte[] exportProductsToCsv(List<TopProduct> products) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CSVWriter writer = new CSVWriter(new OutputStreamWriter(bos));

        writer.writeNext(new String[]{
                "Rank", "Product ID", "Product Name", "Category", "Vendor",
                "Total Sold", "Total Revenue", "Average Price", "Reviews", "Rating"
        });

        for (TopProduct product : products) {
            writer.writeNext(new String[]{
                    String.valueOf(product.getRank()),
                    String.valueOf(product.getProductId()),
                    product.getProductName(),
                    product.getCategoryName(),
                    product.getVendorName(),
                    String.valueOf(product.getTotalSold()),
                    product.getTotalRevenue().toString(),
                    product.getAveragePrice().toString(),
                    String.valueOf(product.getTotalReviews()),
                    product.getAverageRating() != null ? product.getAverageRating().toString() : "N/A"
            });
        }

        writer.close();
        return bos.toByteArray();
    }

    private byte[] exportProductsToExcel(List<TopProduct> products) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Top Products");

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        String[] headers = {"Rank", "Product ID", "Product Name", "Category", "Vendor",
                "Total Sold", "Total Revenue", "Average Price", "Reviews", "Rating"};

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (TopProduct product : products) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(product.getRank());
            row.createCell(1).setCellValue(product.getProductId());
            row.createCell(2).setCellValue(product.getProductName());
            row.createCell(3).setCellValue(product.getCategoryName());
            row.createCell(4).setCellValue(product.getVendorName());
            row.createCell(5).setCellValue(product.getTotalSold());
            row.createCell(6).setCellValue(product.getTotalRevenue().doubleValue());
            row.createCell(7).setCellValue(product.getAveragePrice().doubleValue());
            row.createCell(8).setCellValue(product.getTotalReviews());
            row.createCell(9).setCellValue(product.getAverageRating() != null ? product.getAverageRating() : 0);
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();
        return bos.toByteArray();
    }
}