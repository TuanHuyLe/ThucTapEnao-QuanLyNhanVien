package com.enao.team2.quanlynhanvien.controller;

import com.enao.team2.quanlynhanvien.converter.UserConverter;
import com.enao.team2.quanlynhanvien.dto.AddUserDTO;
import com.enao.team2.quanlynhanvien.dto.PositionDTO;
import com.enao.team2.quanlynhanvien.dto.UserDTO;
import com.enao.team2.quanlynhanvien.messages.MessageResponse;
import com.enao.team2.quanlynhanvien.model.PositionEntity;
import com.enao.team2.quanlynhanvien.model.UserEntity;
import com.enao.team2.quanlynhanvien.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/enao")
public class UserController {
    @Autowired
    IUserService userService;

    @Autowired
    UserConverter userConverter;

    @GetMapping("/user")
    public ResponseEntity<?> search(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "type", required = false, defaultValue = "all") String type,
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
        Page<UserEntity> userPage;
        //search
        if (keyword != null) {
            userPage = userService.findUsersWithPredicate(keyword, type, pageable);
        } else {
            userPage = userService.findAll(pageable);
        }
        //response page
        if (userPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<UserDTO> userDTOS = new ArrayList<>();
        userPage.forEach(x -> userDTOS.add(userConverter.toDTO(x)));
        Map<String, Object> response = new HashMap<>();
        response.put("totalPages", userPage.getTotalPages());
        response.put("totalItems", userPage.getTotalElements());
        response.put("currentPage", userPage.getNumber() + 1);
        response.put("user", userDTOS);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/user")
    public ResponseEntity<?> add(@RequestBody AddUserDTO addUserDTO){
        UserEntity entity;
        MessageResponse responseMessage = new MessageResponse();
        Optional<UserEntity> userEntity = userService.findByUsername(addUserDTO.getUsername());
        if(userEntity.isPresent()){
            return new ResponseEntity<>(HttpStatus.valueOf("duplicate user name"));
        }
        entity = this.userService.save(this.userConverter.toEntityWhenAdd(addUserDTO));
        responseMessage.setMessage("Lưu thành công!");
        return new ResponseEntity(userConverter.toDTO(entity), HttpStatus.OK);
    }

    @PutMapping("/user")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userRequest){
        Optional<UserEntity> dataMustBeUpdate = userService.findById(userRequest.getId());
        if(dataMustBeUpdate.isPresent()){
            UserEntity dataUpdate = dataMustBeUpdate.get();
            if(dataUpdate.getUsername() == userRequest.getUsername()){
                return new ResponseEntity<>(HttpStatus.valueOf("duplicate user name"));
            }else {
                return new ResponseEntity<>(userService.save(userConverter.toEntity(userRequest)), HttpStatus.OK);
            }
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id){
        Optional<UserEntity> dataMustBeDelete = userService.findById(id);
        if(dataMustBeDelete.isPresent()){
            UserEntity dataDelete = dataMustBeDelete.get();
            return new ResponseEntity(userService.deleteSoftById(dataDelete), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
