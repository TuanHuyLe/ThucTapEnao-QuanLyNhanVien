package com.enao.team2.quanlynhanvien.controller;

import com.enao.team2.quanlynhanvien.converter.UserConverter;
import com.enao.team2.quanlynhanvien.dto.UserDTO;
import com.enao.team2.quanlynhanvien.model.UserEntity;
import com.enao.team2.quanlynhanvien.service.IUserService;
import com.enao.team2.quanlynhanvien.utils.UserExcelExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/flush-cache")
    public String flushCache() {
        userService.flushCache();
        return "Flush cache successfully";
    }

    @GetMapping("/users")
    public ResponseEntity<?> home(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") String page,
            @RequestParam(value = "limit", required = false, defaultValue = "5") String limit,
            @RequestParam(value = "sb", required = false, defaultValue = "") String sortBy,
            @RequestParam(value = "asc", required = false, defaultValue = "true") String asc) {
        Pageable pageable;
        //sort
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
        //search
        if (keyword != null) {
            usersPage = userService.findUsersWithPredicate(keyword, pageable);
        } else {
            usersPage = userService.findAll(pageable);
        }
        //response page
        if (usersPage.isEmpty()) {
            return ResponseEntity.noContent().build();
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
}
