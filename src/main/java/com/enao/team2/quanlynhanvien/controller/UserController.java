package com.enao.team2.quanlynhanvien.controller;

import com.enao.team2.quanlynhanvien.converter.UserConverter;
import com.enao.team2.quanlynhanvien.dto.AddUserDTO;
import com.enao.team2.quanlynhanvien.dto.UserDTO;
import com.enao.team2.quanlynhanvien.exception.BadRequestException;
import com.enao.team2.quanlynhanvien.exception.ResourceNotFoundException;
import com.enao.team2.quanlynhanvien.messages.ApiError;
import com.enao.team2.quanlynhanvien.messages.MessageResponse;
import com.enao.team2.quanlynhanvien.model.UserEntity;
import com.enao.team2.quanlynhanvien.service.IUserService;
import com.enao.team2.quanlynhanvien.service.impl.UserDetailsImpl;
import com.enao.team2.quanlynhanvien.validatation.ValidateUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
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

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getOne(@PathVariable UUID id) {
        UserEntity userEntity = userService.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not found user with id: " + id.toString()));
        UserDTO dto = userConverter.toDTO(userEntity);
        return new ResponseEntity(dto, HttpStatus.OK);
    }

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
        return new ResponseEntity(userConverter.toDTO(entity), HttpStatus.OK);
    }

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
        userEntity.setPassword(dataMustBeUpdate.getPassword());
        userEntity.setGroup(dataMustBeUpdate.getGroup());
        userEntity.setPositions(dataMustBeUpdate.getPositions());
        return new ResponseEntity<>(userConverter.toDTO(userService.save(userEntity)), HttpStatus.OK);
    }

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

}
