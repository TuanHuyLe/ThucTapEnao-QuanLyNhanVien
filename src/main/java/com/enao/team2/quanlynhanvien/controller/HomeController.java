package com.enao.team2.quanlynhanvien.controller;

import com.enao.team2.quanlynhanvien.converter.UserConverter;
import com.enao.team2.quanlynhanvien.dto.UserDTO;
import com.enao.team2.quanlynhanvien.generic.GenericPageable;
import com.enao.team2.quanlynhanvien.generic.GenericSort;
import com.enao.team2.quanlynhanvien.model.UserEntity;
import com.enao.team2.quanlynhanvien.service.IUserService;
import com.enao.team2.quanlynhanvien.service.impl.CriteriaBuilderImpl;
import com.enao.team2.quanlynhanvien.utils.excel.ExcelImporter;
import com.enao.team2.quanlynhanvien.utils.excel.UserExcelExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/home")
public class HomeController {
    @Autowired
    private IUserService userService;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private CriteriaBuilderImpl<UserEntity> criteriaBuilder;

    @GetMapping("/users")
    public ResponseEntity<?> home(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "page", required = false, defaultValue = "1") String page,
            @RequestParam(value = "limit", required = false, defaultValue = "5") String limit,
            @RequestParam(value = "sb", required = false, defaultValue = "") String sortBy,
            @RequestParam(value = "asc", required = false, defaultValue = "true") String asc) {
        Pageable pageable;
        //sort and pagination using pageable
        if (!sortBy.isEmpty()) {
            if (Boolean.parseBoolean(asc)) {
                pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(limit), Sort.by(sortBy).ascending());
            } else {
                pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(limit), Sort.by(sortBy).descending());
            }
        } else {
            pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(limit));
        }
        Page<UserEntity> usersPage;
        //search using predicate
        if (keyword != null && type != null) {  //tim kiem co type
            String[] types = type.split("-");
            usersPage = userService.findUsersWithPredicate(keyword, types, pageable);
        } else if (keyword != null) {           //tim kiem khong co type
            usersPage = userService.findUsersWithPredicate(keyword, pageable);
        } else {                                //lay tat ca
            usersPage = userService.findAll(pageable);
        }
        //response page
        if (usersPage.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<UserDTO> userDTOs = new ArrayList<>();
        usersPage.forEach(x -> userDTOs.add(userConverter.toDTO(x)));
        Map<String, Object> response = new HashMap<>();
        response.put("totalPages", usersPage.getTotalPages());
        response.put("totalItems", usersPage.getTotalElements());
        response.put("currentPage", usersPage.getNumber() + 1);
        response.put("users", userDTOs);
        return ResponseEntity.ok(response);
    }

    /*
     * export excel
     */
    @GetMapping("/users/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<UserEntity> listUsers = userService.findAll();

        UserExcelExporter excelExporter = new UserExcelExporter(listUsers);

        excelExporter.export(response);
    }

    /*
     * import excel
     */
    @GetMapping("/users/import/excel")
    public ResponseEntity<List<UserDTO>> importFromExcel(
            @RequestParam("file") MultipartFile files) throws IOException {
        ExcelImporter excelImporter = new ExcelImporter(files);
        List<UserDTO> userDTOs = excelImporter.readBooksFromExcelFile();
        return ResponseEntity.ok(userDTOs);
    }

    /*
     * test search by criteria builder
     */
    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam String attr,
            @RequestParam String type,
            @RequestParam String value,
            @RequestParam String sb,
            @RequestParam(defaultValue = "true") Boolean asc,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "1") Integer limit) {
        List<UserDTO> userDTOs = new ArrayList<>();
        List<UserEntity> userEntities;
        //sort
        GenericSort genericSort = new GenericSort(sb, asc);
        //pageable
        GenericPageable genericPageable = new GenericPageable(page, limit);

        userEntities = criteriaBuilder.builder(UserEntity.class, attr, type,
                value, genericSort, genericPageable);

        if (userEntities.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        userEntities.forEach(x -> userDTOs.add(userConverter.toDTO(x)));
        Map<String, Object> response = new HashMap<>();
        response.put("currentPage", page);
        response.put("pageSize", limit);
        response.put("users", userDTOs);
        return ResponseEntity.ok(response);
    }

}
