package com.enao.team2.quanlynhanvien.controller;

import com.enao.team2.quanlynhanvien.converter.RoleConverter;
import com.enao.team2.quanlynhanvien.dto.GroupDTO;
import com.enao.team2.quanlynhanvien.dto.RoleDTO;
import com.enao.team2.quanlynhanvien.messages.MessageResponse;
import com.enao.team2.quanlynhanvien.model.GroupEntity;
import com.enao.team2.quanlynhanvien.model.PermissionEntity;
import com.enao.team2.quanlynhanvien.model.RoleEntity;
import com.enao.team2.quanlynhanvien.service.IPermissionService;
import com.enao.team2.quanlynhanvien.service.IRoleService;
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
@RequestMapping("/api/enao")
public class RoleController {

    @Autowired
    IRoleService roleService;

    @Autowired
    RoleConverter roleConverter;

    @Autowired
    IPermissionService permissionService;

    @GetMapping("/roles")
    @PreAuthorize("@appAuthorizer.authorize(authentication, \"VIEW_ROLE\")")
    public List<RoleEntity> listAll() {
    List<RoleEntity> re = this.roleService.findAll();
    List<RoleDTO> list = new ArrayList();
    re.forEach(x -> list.add(roleConverter.toDTO(x)));
    return re;
    }

    @GetMapping("/rolesPage")
    @PreAuthorize("@appAuthorizer.authorize(authentication, \"VIEW_ROLE\")")
    public Page<RoleDTO> getList(@RequestParam(value = "page") int page, @RequestParam(value = "limit") int limit){
        Page<RoleEntity> pag = this.roleService.getByPage(page, limit);
        Page<RoleDTO> pagee = this.roleConverter.toPageDTO(pag);
        return pagee;
    }

    @GetMapping("/role/{id}")
    @PreAuthorize("@appAuthorizer.authorize(authentication, \"VIEW_ROLE\")")
    public ResponseEntity<RoleDTO> getOne(@PathVariable UUID id){
        RoleDTO dto = new RoleDTO();
//            ResponseMessage responseMessage = new ResponseMessage();
        dto = this.roleConverter.toDTO(this.roleService.getOne(id));
//            Optional<RoleEntity> optionalRoleEntity = this.roleService.findById(id);
//            if (!optionalRoleEntity.isPresent()){
//                responseMessage.setMessage("Không tìm thấy bản ghi!");
//                return new ResponseEntity(responseMessage.getMessage(), HttpStatus.OK);
//            }
//            else{
        return new ResponseEntity(dto, HttpStatus.OK);
    }

    @PostMapping("/role")
    @PreAuthorize("@appAuthorizer.authorize(authentication, \"ADD_ROLE\")")
    public ResponseEntity<?> save(@RequestBody RoleDTO dto) {
        RoleEntity entity = new RoleEntity();
        MessageResponse responseMessage = new MessageResponse();
        Optional<RoleEntity> name = this.roleService.findByName(dto.getName());
        if (name.isPresent()) {
            responseMessage.setMessage("Trùng tên");
            return new ResponseEntity(responseMessage.getMessage(), HttpStatus.OK);
        }
        else{
                entity = (RoleEntity) this.roleService.save(this.roleConverter.toEntity(dto));
                responseMessage.setMessage("Lưu thành công!");
                return new ResponseEntity(roleConverter.toDTO(entity), HttpStatus.OK);
            }
    }

    @PutMapping("/role/{id}")
    @PreAuthorize("@appAuthorizer.authorize(authentication, \"EDIT_ROLE\")")
    public ResponseEntity<?> update(@RequestBody RoleDTO dto, @PathVariable UUID id){
        RoleEntity entity = this.roleService.getOne(id);
        Optional<RoleEntity> name = this.roleService.findByName(dto.getName());
        MessageResponse responseMessage = new MessageResponse();
        if (name.isPresent() && (!dto.getName().equals(entity.getName()))){
            responseMessage.setMessage("Trùng tên");
            return new ResponseEntity(responseMessage.getMessage(), HttpStatus.OK);
        }
        else{
            dto.setId(entity.getId());
            dto.setActive(entity.getActive());
            this.roleService.save(this.roleConverter.toEntity(dto));
            responseMessage.setMessage("Sửa thành công!");
            return new ResponseEntity(responseMessage.getMessage() + entity, HttpStatus.OK);
        }

    }

    @DeleteMapping("/role/{id}")
    @PreAuthorize("@appAuthorizer.authorize(authentication, \"REMOVE_ROLE\")")
    public ResponseEntity<MessageResponse> delete(@PathVariable(value = "id") UUID id){
        MessageResponse responseMessage = this.roleService.delete(id);
        return new ResponseEntity(responseMessage.getMessage(), HttpStatus.OK);
    }

    @GetMapping("/role")
    @PreAuthorize("@appAuthorizer.authorize(authentication, \"VIEW_ROLE\")")
    public ResponseEntity<?> search(
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
        Page<RoleEntity> rolesPage;
        //search
        if (keyword != null) {
            rolesPage = roleService.findRolesWithPredicate(keyword, pageable);
        } else {
            rolesPage = roleService.findAll(pageable);
        }
        //response page
        List<RoleDTO> roleDTOs = new ArrayList<>();
        rolesPage.forEach(x -> roleDTOs.add(roleConverter.toDTO(x)));
        if (roleDTOs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        Map<String, Object> response = new HashMap<>();
        response.put("totalPages", rolesPage.getTotalPages());
        response.put("totalItems", rolesPage.getTotalElements());
        response.put("currentPage", rolesPage.getNumber() + 1);
        response.put("groups", roleDTOs);
        return ResponseEntity.ok(response);
    }
}
