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
import java.util.Optional;
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
        Page<GroupDTO> pagee = this.groupConverter.toPageDTO(page);
        return pagee;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDTO> getOne(@PathVariable UUID id){
            GroupDTO dto = new GroupDTO();
//            ResponseMessage responseMessage = new ResponseMessage();
            dto = this.groupConverter.toDTO(this.groupService.getOne(id));
//            Optional<GroupEntity> optionalGroupEntity = this.groupService.findById(id);
//            if (!optionalGroupEntity.isPresent()){
//                responseMessage.setMessage("Không tìm thấy bản ghi!");
//                return new ResponseEntity(responseMessage.getMessage(), HttpStatus.OK);
//            }
//            else{
                return new ResponseEntity(dto, HttpStatus.OK);
//            }
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody GroupDTO dto){
        GroupEntity entity = new GroupEntity();
        Optional<GroupEntity> name = this.groupService.findByName(dto.getName());
        ResponseMessage responseMessage = new ResponseMessage();
        if (name.isPresent()){
            responseMessage.setMessage("Trùng tên");
            return new ResponseEntity(responseMessage.getMessage(), HttpStatus.OK);
        }
        else{
            entity = (GroupEntity) this.groupService.save(this.groupConverter.toEntity(dto));
            responseMessage.setMessage("Lưu thành công!");
            return new ResponseEntity(entity, HttpStatus.OK);
        }

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody GroupDTO dto, @PathVariable UUID id){
        GroupEntity entity = this.groupService.getOne(id);
        Optional<GroupEntity> name = this.groupService.findByName(dto.getName());
        ResponseMessage responseMessage = new ResponseMessage();
        if (name.isPresent()){
            responseMessage.setMessage("Trùng tên");
            return new ResponseEntity(responseMessage.getMessage(), HttpStatus.OK);
        }
        else{
            dto.setId(entity.getId());
            dto.setActive(entity.getActive());
            this.groupService.save(this.groupConverter.toEntity(dto));
            responseMessage.setMessage("Sửa thành công!");
            return new ResponseEntity(dto, HttpStatus.OK);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> delete(@PathVariable(value = "id") UUID id){
        ResponseMessage responseMessage = this.groupService.delete(id);
        return new ResponseEntity(responseMessage.getMessage(), HttpStatus.OK);
    }

    @GetMapping("/search/{keyword}")
    public List<GroupDTO> searchByName(@PathVariable String keyword){
        if (keyword != null){
            List<GroupEntity> listEntity = this.groupService.searchByName(keyword);
            List<GroupDTO> list = new ArrayList();
            listEntity.forEach(x -> list.add(groupConverter.toDTO(x)));
            return list;
        } else{
            List<GroupEntity> itr = this.groupService.findAll();
            List<GroupDTO> list = new ArrayList();
            itr.forEach(x -> list.add(groupConverter.toDTO(x)));
            return list;
        }
    }

}

