package com.enao.team2.quanlynhanvien.utils;

import com.enao.team2.quanlynhanvien.model.UserEntity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class UserExcelExporter {
    private final XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private final List<UserEntity> listUsers;
    private Integer rowCount;

    public UserExcelExporter(List<UserEntity> listUsers) {
        this.listUsers = listUsers;
        workbook = new XSSFWorkbook();
    }


    private void writeHeaderLine() {
        sheet = workbook.createSheet("DANH SACH NHAN VIEN");

        //init row start
        rowCount = 0;

        //create row title header style
        Row row = sheet.createRow(rowCount++);

        CellStyle styleHeader = cellStyle(30, true, true);
        createCell(row, 1, "Danh sách nhân viên", styleHeader);

        //create row header style
        row = sheet.createRow(rowCount++);
        CellStyle style = cellStyle(16, true, true);
        setBorder(style);
        style.setAlignment(HorizontalAlignment.CENTER);

        //create row header title
        createCell(row, 0, "STT", style);
        createCell(row, 1, "User ID", style);
        createCell(row, 2, "Username", style);
        createCell(row, 3, "Full Name", style);
        createCell(row, 4, "Gender", style);
        createCell(row, 5, "Email", style);
        createCell(row, 6, "Status", style);

    }

    private void setBorder(CellStyle cellStyle) {
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
    }

    private CellStyle cellStyle(int size, boolean bold, boolean alignCenter) {
        CellStyle styleHeader = workbook.createCellStyle();
        XSSFFont fontHeader = workbook.createFont();
        fontHeader.setFontHeight(size);
        fontHeader.setBold(bold);
        fontHeader.setFontName("Times New Roman");
        styleHeader.setFont(fontHeader);
        if (alignCenter){
            styleHeader.setAlignment(HorizontalAlignment.CENTER);
        }
        return styleHeader;
    }


    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof UUID) {
            cell.setCellValue(String.valueOf(value));
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int stt = 1;

        //create style body table
        CellStyle style = cellStyle(14, false, false);
        CellStyle styleStt = cellStyle(14, false, true);
        setBorder(styleStt);
        setBorder(style);

        Row row;
        for (UserEntity user : listUsers) {
            row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, stt++, styleStt);
            createCell(row, columnCount++, user.getId(), style);
            createCell(row, columnCount++, user.getUsername(), style);
            createCell(row, columnCount++, user.getFullName(), style);
            createCell(row, columnCount++, user.getGender() ? "Male" : "Female", style);
            createCell(row, columnCount++, user.getEmail(), style);
            createCell(row, columnCount++, user.getActive() ? "Active" : "Lock", style);

        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }
}
