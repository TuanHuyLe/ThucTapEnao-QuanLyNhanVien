package com.enao.team2.quanlynhanvien.controller;

import com.enao.team2.quanlynhanvien.converter.UserConverter;
import com.enao.team2.quanlynhanvien.dto.AddUserDTO;
import com.enao.team2.quanlynhanvien.dto.ChangePasswordDTO;
import com.enao.team2.quanlynhanvien.dto.EmailDTO;
import com.enao.team2.quanlynhanvien.dto.UserDTO;
import com.enao.team2.quanlynhanvien.exception.BadRequestException;
import com.enao.team2.quanlynhanvien.exception.ResourceNotFoundException;
import com.enao.team2.quanlynhanvien.messages.ApiError;
import com.enao.team2.quanlynhanvien.messages.MessageResponse;
import com.enao.team2.quanlynhanvien.model.UserEntity;
import com.enao.team2.quanlynhanvien.service.IUserService;
import com.enao.team2.quanlynhanvien.service.excel.ExcelImporter;
import com.enao.team2.quanlynhanvien.service.excel.UserExcelExporter;
import com.enao.team2.quanlynhanvien.service.impl.UserDetailsImpl;
import com.enao.team2.quanlynhanvien.service.mail.EmailService;
import com.enao.team2.quanlynhanvien.validatation.ValidateUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/enao")
public class UserController {
    @Autowired
    IUserService userService;

    @Autowired
    UserConverter userConverter;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    @PreAuthorize("@appAuthorizer.authorize(authentication, \"VIEW_USER\")")
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getOne(@PathVariable UUID id) {
        UserEntity userEntity = userService.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not found user with id: " + id.toString()));
        UserDTO dto = userConverter.toDTO(userEntity);
        return new ResponseEntity(dto, HttpStatus.OK);
    }

    @PreAuthorize("@appAuthorizer.authorize(authentication, \"VIEW_USER\")")
    @GetMapping("/user")
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

    @PreAuthorize("@appAuthorizer.authorize(authentication, \"ADD_USER\")")
    @PostMapping("/user")
    public ResponseEntity<?> add(@RequestBody AddUserDTO addUserDTO) {
        UserEntity entity;
        MessageResponse responseMessage = new MessageResponse();
        List<String> error = ValidateUser.check(addUserDTO);
        if (!error.isEmpty()) {
            ApiError errorMessage = new ApiError(
                    LocalDateTime.now(),
                    HttpStatus.BAD_REQUEST.value(),
                    "Add user failed!",
                    error
            );
            return ResponseEntity.badRequest().body(errorMessage);
        }
        Optional<UserEntity> userEntity = userService.findByUsername(addUserDTO.getUsername());
        if (userEntity.isPresent()) {
            throw new BadRequestException("Username is exists!");
        }
        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> rolesName = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        boolean isAdmin = rolesName.contains("ROLE_FULL");
        if (isAdmin) {
            if (addUserDTO.getGroupName() == null) {
                throw new BadRequestException("Group is required!");
            } else {
                entity = this.userService.save(this.userConverter.toEntityWhenAdd(addUserDTO));
            }
        } else {
            UserEntity currentUser = userService.findById(user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Can not found group with id: " + user.getId()));
            UserEntity newUser = this.userConverter.toEntityWhenAdd(addUserDTO);
            newUser.setGroup(currentUser.getGroup());
            entity = this.userService.save(newUser);
        }
        responseMessage.setMessage("Save successfully!");

        return new ResponseEntity(userConverter.toDTO(entity), HttpStatus.CREATED);
    }

    @PutMapping("/user/changePassword")
    public ResponseEntity<?> changPassword(@RequestBody ChangePasswordDTO newPassword) {
        MessageResponse responseMessage = new MessageResponse();
        UserEntity dataChangePassword = userService.findByUsername(newPassword.getUserName()).orElseThrow(
                () -> new ResourceNotFoundException("can not find user with username: " + newPassword.getUserName())
        );
        if (!passwordEncoder.matches(newPassword.getOldPassword(), dataChangePassword.getPassword())) {
            throw new BadRequestException("Old password is incorrect!");
        } else {
            if (newPassword.getNewPassword().isEmpty()) {
                throw new BadRequestException("New password is required!");
            } else if (newPassword.getNewPassword().equals(newPassword.getOldPassword())) {
                throw new BadRequestException("New password must be unequal with old password");
            } else {
                userService.save(userConverter.toEntityWhenChangePass(dataChangePassword, newPassword));
                responseMessage.setMessage("Change password is successfully!");
                EmailDTO emailDTO = new EmailDTO();
                emailDTO.setSubject("Change password");
                emailDTO.setRecipients(newPassword.getEmail());
                String sb = "Password has changed by <b>" + dataChangePassword.getUsername() +
                        "</b><br>New password is: <b>" + newPassword.getNewPassword() +
                        "</b><br>Time: " + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
                emailDTO.setContent(sb);
                emailService.sendMessage(emailDTO);
            }
        }
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PreAuthorize("@appAuthorizer.authorize(authentication, \"EDIT_USER\")")
    @PutMapping("/user")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userRequest) {
        UserEntity dataMustBeUpdate = userService.findById(userRequest.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Can not update user with id: " + userRequest.getId())
        );
        if (!dataMustBeUpdate.getUsername().equals(userRequest.getUsername())) {
            Optional<UserEntity> user = userService.findByUsername(userRequest.getUsername());
            if (user.isPresent() && user.get().getUsername().equals(userRequest.getUsername())) {
                throw new BadRequestException("Username is exists!");
            }
        }
        UserEntity userEntity = userConverter.toEntity(userRequest);

        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> rolesName = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        boolean isAdmin = rolesName.contains("ROLE_FULL");
        if (isAdmin) {
            userEntity.setPassword(dataMustBeUpdate.getPassword());
        } else {
            userEntity.setPassword(dataMustBeUpdate.getPassword());
            userEntity.setGroup(dataMustBeUpdate.getGroup());
            userEntity.setPositions(dataMustBeUpdate.getPositions());
        }
        return new ResponseEntity<>(userConverter.toDTO(userService.save(userEntity)), HttpStatus.OK);
    }

    @PreAuthorize("@appAuthorizer.authorize(authentication, \"REMOVE_USER\")")
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        Optional<UserEntity> dataMustBeDelete = userService.findById(id);
        if (dataMustBeDelete.isPresent()) {
            UserEntity dataDelete = dataMustBeDelete.get();
            return new ResponseEntity(userConverter.toDTO(userService.deleteSoftById(dataDelete)), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException("Can not find user id: " + id);
        }
    }

    @GetMapping("/user/export/excel")
    public ResponseEntity<?> exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<UserEntity> listUsers = userService.findAll();

        UserExcelExporter excelExporter = new UserExcelExporter(listUsers);

        excelExporter.export(response);
        return new ResponseEntity<>(new MessageResponse("export successfully"), HttpStatus.OK);
    }

    @GetMapping("/user/import/excel")
    public ResponseEntity<List<UserDTO>> importFromExcel(
            @RequestParam("file") MultipartFile files) throws IOException {
        ExcelImporter excelImporter = new ExcelImporter(files);
        List<UserDTO> userDTOs = excelImporter.readBooksFromExcelFile();
        return ResponseEntity.ok(userDTOs);
    }

}
