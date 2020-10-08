package com.enao.team2.quanlynhanvien.controller;

import com.enao.team2.quanlynhanvien.converter.GroupConverter;
import com.enao.team2.quanlynhanvien.dto.GroupDTO;
import com.enao.team2.quanlynhanvien.exception.ResourceNotFoundException;
import com.enao.team2.quanlynhanvien.messages.MessageResponse;
import com.enao.team2.quanlynhanvien.model.GroupEntity;
import com.enao.team2.quanlynhanvien.service.IGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/enao")
public class GroupController {

    @Autowired
    IGroupService groupService;

    @Autowired
    GroupConverter groupConverter;

    @PreAuthorize("@appAuthorizer.authorize(authentication, \"VIEW_GROUP\")")
    @GetMapping("/group/{id}")

    public ResponseEntity<GroupDTO> getOne(@PathVariable UUID id) {
        GroupEntity groupEntity = groupService.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not found group with id: " + id.toString()));
        GroupDTO dto = groupConverter.toDTO(groupEntity);
        return new ResponseEntity(dto, HttpStatus.OK);
    }
    @PreAuthorize("@appAuthorizer.authorize(authentication, \"ADD_GROUP\")")
    @PostMapping("/group")
    public ResponseEntity<?> save(@RequestBody GroupDTO dto) {
        GroupEntity entity;
        MessageResponse responseMessage = new MessageResponse();
        Optional<GroupEntity> name = groupService.findByName(dto.getName());
        if (name.isPresent()) {
            responseMessage.setMessage("Trùng tên!");
            return new ResponseEntity(responseMessage.getMessage(), HttpStatus.OK);
        }
        entity = this.groupService.save(this.groupConverter.toEntity(dto));
        responseMessage.setMessage("Lưu thành công!");
        return new ResponseEntity(groupConverter.toDTO(entity), HttpStatus.OK);
    }

    @PreAuthorize("@appAuthorizer.authorize(authentication, \"EDIT_GROUP\")")
    @PutMapping("/group")
    public ResponseEntity<?> update(@RequestBody GroupDTO dto) {
        Optional<GroupEntity> entity1 = this.groupService.findById(dto.getId());
        MessageResponse responseMessage = new MessageResponse();
        if (!entity1.isPresent()) {
            responseMessage.setMessage("Không tìm thấy bản ghi!");
            return new ResponseEntity(responseMessage.getMessage(), HttpStatus.OK);
        }
        GroupEntity entity = entity1.get();
        Optional<GroupEntity> name = this.groupService.findByName(dto.getName());
        if (name.isPresent() && !dto.getName().equals(entity.getName())) {
            responseMessage.setMessage("Trùng tên!");
            return new ResponseEntity(responseMessage.getMessage(), HttpStatus.OK);
        }
        entity = this.groupService.save(groupConverter.toEntity(dto));
        responseMessage.setMessage("Sửa thành công");
        return new ResponseEntity(groupConverter.toDTO(entity), HttpStatus.OK);
    }

    @PreAuthorize("@appAuthorizer.authorize(authentication, \"REMOVE_GROUP\")")
    @DeleteMapping("/group/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") UUID id) {
        if (!groupService.checkId(id)){
            return new ResponseEntity(new MessageResponse("Không tìm thấy bản ghi!"), HttpStatus.OK);
        }
        GroupEntity groupEntity = groupService.findById(id).get();
        groupEntity.setActive(false);
        groupService.save(groupEntity);
        return new ResponseEntity(groupConverter.toDTO(groupEntity), HttpStatus.OK);
    }

    @PreAuthorize("@appAuthorizer.authorize(authentication, \"VIEW_GROUP\")")
    @GetMapping("/group")
    public ResponseEntity<?> search(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "type", required = false) String type,
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
        Page<GroupEntity> groupsPage;
        //search
        if (keyword != null && type != null) {
            String[] types = type.split("-");
            groupsPage = groupService.findGroupsWithPredicate(keyword, types, pageable);
        } else if (keyword != null) {
            groupsPage = groupService.findGroupsWithPredicate(keyword, pageable);
        } else {                  
            groupsPage = groupService.findAll(pageable);
        }
        //response page
        if (groupsPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<GroupDTO> groupDTOS = new ArrayList<>();
        groupsPage.forEach(x -> groupDTOS.add(groupConverter.toDTO(x)));
        Map<String, Object> response = new HashMap<>();
        response.put("totalPages", groupsPage.getTotalPages());
        response.put("totalItems", groupsPage.getTotalElements());
        response.put("currentPage", groupsPage.getNumber() + 1);
        response.put("groups", groupDTOS);
        return ResponseEntity.ok(response);
    }
}