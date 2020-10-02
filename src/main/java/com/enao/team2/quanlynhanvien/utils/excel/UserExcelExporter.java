package com.enao.team2.quanlynhanvien.utils.excel;

import com.enao.team2.quanlynhanvien.model.RoleEntity;
import com.enao.team2.quanlynhanvien.model.UserEntity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserExcelExporter {
    private final XSSFWorkbook workbook;
    private final XSSFSheet sheet;
    private final List<UserEntity> listUsers;
    private Integer rowCount;
    private final ExcelExporter excelExporter;

    public UserExcelExporter(List<UserEntity> listUsers) {
        excelExporter = new ExcelExporter("DANH SACH NHAN VIEN");
        this.listUsers = listUsers;
        workbook = excelExporter.getWorkbook();
        sheet = excelExporter.getSheet();
    }

    /*
    write header line
     */
    private void writeHeaderLine() {
        //init row start
        rowCount = 0;

        //create row title header style
        Row row = sheet.createRow(rowCount++);

        CellStyle styleHeader = excelExporter.cellStyle(30, true, true);
        excelExporter.createCell(row, 1, "Danh sách nhân viên", styleHeader);

        //create row header style
        row = sheet.createRow(rowCount++);
        CellStyle style = excelExporter.cellStyle(16, true, true);
        excelExporter.setBorder(style);
        style.setAlignment(HorizontalAlignment.CENTER);

        //create row header title
        excelExporter.createCell(row, 0, "STT", style);
        excelExporter.createCell(row, 1, "User ID", style);
        excelExporter.createCell(row, 2, "Username", style);
        excelExporter.createCell(row, 3, "Full Name", style);
        excelExporter.createCell(row, 4, "Gender", style);
        excelExporter.createCell(row, 5, "Email", style);
        excelExporter.createCell(row, 6, "Status", style);
        excelExporter.createCell(row, 7, "Group", style);
        excelExporter.createCell(row, 8, "Role", style);

    }

    /*
    write data to table
     */
    private void writeDataLines() {
        int stt = 1;

        //create style body table
        CellStyle style = excelExporter.cellStyle(14, false, false);
        CellStyle styleStt = excelExporter.cellStyle(14, false, true);
        excelExporter.setBorder(styleStt);
        excelExporter.setBorder(style);

        Row row;
        //string builder contain roles
        StringBuilder sb;

        for (UserEntity user : listUsers) {
            row = sheet.createRow(rowCount++);
            int columnCount = 0;
            sb = new StringBuilder();
            excelExporter.createCell(row, columnCount++, stt++, styleStt);
            excelExporter.createCell(row, columnCount++, user.getId(), style);
            excelExporter.createCell(row, columnCount++, user.getUsername(), style);
            excelExporter.createCell(row, columnCount++, user.getFullName(), style);
            excelExporter.createCell(row, columnCount++, user.getGender() ? "Male" : "Female", style);
            excelExporter.createCell(row, columnCount++, user.getEmail(), style);
            excelExporter.createCell(row, columnCount++, user.getActive() ? "Active" : "Lock", style);
            excelExporter.createCell(row, columnCount++, user.getGroup().getName(), style);
            List<RoleEntity> userEntities = new ArrayList<>(user.getRoles());
            if (userEntities.isEmpty()) {
                excelExporter.createCell(row, columnCount++, "NULL", style);
            } else {
                for (int i = 0; i < userEntities.size() - 1; i++) {
                    sb.append(userEntities.get(i).getName()).append(", ");
                }
                sb.append(userEntities.get(userEntities.size() - 1).getName());
                excelExporter.createCell(row, columnCount++, sb.toString(), style);
            }
        }
    }

    /*
    export excel
     */
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }
}
