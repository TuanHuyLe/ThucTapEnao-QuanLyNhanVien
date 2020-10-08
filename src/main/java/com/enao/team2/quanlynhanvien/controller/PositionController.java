package com.enao.team2.quanlynhanvien.controller;

import com.enao.team2.quanlynhanvien.converter.PositionConverter;
import com.enao.team2.quanlynhanvien.dto.PositionDTO;
import com.enao.team2.quanlynhanvien.messages.MessageResponse;
import com.enao.team2.quanlynhanvien.model.PositionEntity;
import com.enao.team2.quanlynhanvien.service.IPositionService;
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
public class PositionController {
    @Autowired
    IPositionService positionService;

    @Autowired
    PositionConverter positionConverter;

    @GetMapping("/position")
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
        Page<PositionEntity> positionPage;
        //search
        if (keyword != null) {
            positionPage = positionService.findGroupsWithPredicate(keyword, pageable);
        } else {
            positionPage = positionService.findAll(pageable);
        }
        //response page
        List<PositionDTO> positionDTOS = new ArrayList<>();
        positionPage.forEach(x -> positionDTOS.add(positionConverter.toDTO(x)));
        if (positionDTOS.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        Map<String, Object> response = new HashMap<>();
        response.put("totalPages", positionPage.getTotalPages());
        response.put("totalItems", positionPage.getTotalElements());
        response.put("currentPage", positionPage.getNumber() + 1);
        response.put("groups", positionDTOS);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/position")
    public ResponseEntity<?> add(@RequestBody PositionDTO positionDTO){
        PositionEntity entity;
        MessageResponse responseMessage = new MessageResponse();
        Optional<PositionEntity> name = positionService.findByName(positionDTO.getName());
        if (name.isPresent()) {
            responseMessage.setMessage("Trùng tên!");
            return new ResponseEntity(responseMessage.getMessage(), HttpStatus.OK);
        }
        entity = this.positionService.save(this.positionConverter.toEntity(positionDTO));
        responseMessage.setMessage("Lưu thành công!");
        return new ResponseEntity(positionConverter.toDTO(entity), HttpStatus.OK);
    }

    @DeleteMapping("/position/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id){
        Optional<PositionEntity> dataMustBeDelete = positionService.findById(id);
        if(dataMustBeDelete.isPresent()){
            PositionEntity dataDelete = dataMustBeDelete.get();
            return new ResponseEntity(positionService.deleteSoftById(dataDelete), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/position")
    public ResponseEntity<?> update(@RequestBody PositionDTO positionDTO){
        Optional<PositionEntity> entity1 = this.positionService.findById(positionDTO.getId());
        MessageResponse responseMessage = new MessageResponse();
        if (!entity1.isPresent()){
            responseMessage.setMessage("Không tìm thấy bản ghi!");
            return new ResponseEntity(responseMessage.getMessage(), HttpStatus.OK);
        }
        PositionEntity entity = entity1.get();
        Optional<PositionEntity> name = this.positionService.findByName(positionDTO.getName());
        if (name.isPresent() && !positionDTO.getName().equals(entity.getName())) {
            responseMessage.setMessage("Trùng tên!");
            return new ResponseEntity(responseMessage.getMessage(), HttpStatus.OK);
        }
        entity = this.positionService.save(positionConverter.toEntity(positionDTO));
        responseMessage.setMessage("Sửa thành công");
        return new ResponseEntity(positionConverter.toDTO(entity), HttpStatus.OK);
    }

}
