package com.enao.team2.quanlynhanvien.service.excel;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.UUID;

@Getter
@Setter
public class ExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    /*
    constructor
     */
    public ExcelExporter(String sheetName) {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet(sheetName);
    }

    /*
    set border cell
     */
    void setBorder(CellStyle cellStyle) {
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
    }

    /*
    create style: font, table, column
     */
    CellStyle cellStyle(int size, boolean bold, boolean alignCenter) {
        CellStyle styleHeader = workbook.createCellStyle();
        XSSFFont fontHeader = workbook.createFont();
        fontHeader.setFontHeight(size);
        fontHeader.setBold(bold);
        fontHeader.setFontName("Times New Roman");
        styleHeader.setFont(fontHeader);
        if (alignCenter) {
            styleHeader.setAlignment(HorizontalAlignment.CENTER);
        }
        return styleHeader;
    }

    /*
    create cell
     */
    void createCell(Row row, int columnCount, Object value, CellStyle style) {
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
}
