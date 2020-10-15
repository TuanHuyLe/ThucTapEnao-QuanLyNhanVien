package com.enao.team2.quanlynhanvien.controller;

import com.enao.team2.quanlynhanvien.constants.Constants;
import com.enao.team2.quanlynhanvien.dto.EmailDTO;
import com.enao.team2.quanlynhanvien.exception.BadRequestException;
import com.enao.team2.quanlynhanvien.messages.MessageResponse;
import com.enao.team2.quanlynhanvien.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private IMessageService<EmailDTO> emailService;

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

    /**
     * send email
     *
     * @param emailDTO
     * @return
     */
    @PostMapping("/email")
    public ResponseEntity<?> sendEmail(@RequestBody EmailDTO emailDTO) {
        if (emailService.sendMessage(emailDTO)) {
            return ResponseEntity.ok(new MessageResponse("Sent email"));
        }
        throw new BadRequestException("Email is invalid!");
    }

}
