package com.enao.team2.quanlynhanvien.controller;

import com.enao.team2.quanlynhanvien.converter.GroupConverter;
import com.enao.team2.quanlynhanvien.dto.GroupDTO;
import com.enao.team2.quanlynhanvien.dto.ResponseMessage;
import com.enao.team2.quanlynhanvien.model.GroupEntity;
import com.enao.team2.quanlynhanvien.service.IGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/group")
public class GroupController {

    @Autowired
    IGroupService groupService;

    @Autowired
    GroupConverter groupConverter;

    @GetMapping("/all")
    public List<GroupDTO> listAll(){
        List<GroupEntity> itr = this.groupService.findAll();
        List<GroupDTO> list = new ArrayList();
        itr.forEach(x -> list.add(groupConverter.toDTO(x)));
        return list;
    }

    @GetMapping("/{pageIndex}/{pageSize}")
    public Page<GroupDTO> getList(@PathVariable int pageIndex, @PathVariable int pageSize){
        Page<GroupEntity> page = this.groupService.getByPage(pageIndex, pageSize);
//        Page<GroupDTO> pagee = this.groupConverter
        Page<GroupDTO> pagee = this.groupConverter.toPageDTO(page);
        return pagee;
    }

    @GetMapping("/{id}")
    public GroupDTO getOne(@PathVariable UUID id){
        return this.groupConverter.toDTO(this.groupService.findById(id));
    }

    @PostMapping("/save")
    public GroupDTO save(@RequestBody GroupDTO dto){
        GroupEntity entity = new GroupEntity();
        entity = (GroupEntity) this.groupService.save(this.groupConverter.toEntity(dto));
        return this.groupConverter.toDTO(entity);
    }

    @PutMapping("/update/{id}")
    public GroupDTO update(@RequestBody GroupDTO dto, @PathVariable UUID id){
        GroupEntity entity = this.groupService.findById(id);
        dto.setId(entity.getId());
        dto.setActive(entity.getActive());
        this.groupService.save(this.groupConverter.toEntity(dto));
        return dto;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> delete(@PathVariable(value = "id") UUID id){
        ResponseMessage responseMessage = this.groupService.delete(id);
        return new ResponseEntity(responseMessage.getMessage(), HttpStatus.OK);
    }

}

