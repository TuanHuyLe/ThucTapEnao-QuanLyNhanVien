package com.enao.team2.quanlynhanvien.controller;

import com.enao.team2.quanlynhanvien.constants.Constants;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/group1")
public class AdminController {

    @GetMapping("/list")
    @PreAuthorize("@appAuthorizer.authorize(authentication, \"" +
            Constants.ACTION_VIEW + "_" + Constants.MODULE_GROUP + "\")")
    public String listGroup() {
        return "group";
    }

    @GetMapping("/add")
    @PreAuthorize("@appAuthorizer.authorize(authentication, \"" +
            Constants.ACTION_ADD + "_" + Constants.MODULE_GROUP + "\")")
    public String addGroup() {
        return "add";
    }
}
