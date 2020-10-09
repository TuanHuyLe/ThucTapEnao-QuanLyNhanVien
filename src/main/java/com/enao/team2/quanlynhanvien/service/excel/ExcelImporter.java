package com.enao.team2.quanlynhanvien.service.excel;


import com.enao.team2.quanlynhanvien.dto.UserDTO;
import com.enao.team2.quanlynhanvien.utils.SlugUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

public class ExcelImporter {
    private SlugUtils slugUtils;

    private MultipartFile multipartFile;

    /*
    constructor
     */
    public ExcelImporter(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
        slugUtils = new SlugUtils();
    }

    /*
    read book from excel file
     */
    public List<UserDTO> readBooksFromExcelFile() throws IOException {
        List<UserDTO> users = new ArrayList<>();

        XSSFWorkbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
        XSSFSheet firstSheet = workbook.getSheetAt(0);

        for (int index = 0; index < firstSheet.getPhysicalNumberOfRows(); index++) {
            UserDTO userDTO = new UserDTO();
            XSSFRow row = firstSheet.getRow(index);
            String fullName = row.getCell(2).getStringCellValue();
            userDTO.setId(UUID.fromString(row.getCell(0).getStringCellValue()));
            userDTO.setUsername(row.getCell(1).getStringCellValue());
            userDTO.setFullName(fullName);
            userDTO.setGender("Male".equals(row.getCell(3).getStringCellValue()));
            userDTO.setEmail(row.getCell(4).getStringCellValue());
            userDTO.setActive("Active".equals(row.getCell(5).getStringCellValue()));
            userDTO.setSlug(slugUtils.slug(fullName));
            userDTO.setGroupName(row.getCell(6).getStringCellValue());
            users.add(userDTO);
        }

        workbook.close();

        return users;
    }
}
