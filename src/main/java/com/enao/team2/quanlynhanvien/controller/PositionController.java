package com.enao.team2.quanlynhanvien.controller;

import com.enao.team2.quanlynhanvien.converter.PositionConverter;
import com.enao.team2.quanlynhanvien.dto.PositionDTO;
import com.enao.team2.quanlynhanvien.dto.UserDTO;
import com.enao.team2.quanlynhanvien.exception.BadRequestException;
import com.enao.team2.quanlynhanvien.exception.ResourceNotFoundException;
import com.enao.team2.quanlynhanvien.messages.MessageResponse;
import com.enao.team2.quanlynhanvien.model.PositionEntity;
import com.enao.team2.quanlynhanvien.model.UserEntity;
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

    @GetMapping("/position/{id}")
    public ResponseEntity<?> getOne(@PathVariable UUID id) {
        PositionEntity positionEntity = positionService.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not found position with id: " + id.toString()));
        PositionDTO dto = positionConverter.toDTO(positionEntity);
        return new ResponseEntity(dto, HttpStatus.OK);
    }

    @GetMapping("/position")
    public ResponseEntity<?> search(
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
        Page<PositionEntity> positionPage = positionService.findAll(pageable);

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
        response.put("position", positionDTOS);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/position")
    public ResponseEntity<?> add(@RequestBody PositionDTO positionDTO) {
        PositionEntity entity;
        positionService.findByName(positionDTO.getName())
                .orElseThrow(() -> new BadRequestException("Name is exists!"));

        entity = this.positionService.save(this.positionConverter.toEntity(positionDTO));
        return new ResponseEntity(positionConverter.toDTO(entity), HttpStatus.CREATED);
    }

    @DeleteMapping("/position/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        Optional<PositionEntity> dataMustBeDelete = positionService.findById(id);
        if (dataMustBeDelete.isPresent()) {
            PositionEntity dataDelete = dataMustBeDelete.get();
            return new ResponseEntity(positionService.deleteSoftById(dataDelete), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("Can not find position id: " + id);
    }

    @PutMapping("/position")
    public ResponseEntity<?> update(@RequestBody PositionDTO positionDTO) {
        PositionEntity entity = this.positionService.findById(positionDTO.getId())
                .orElseThrow(() -> new BadRequestException("Can not update position id: " + positionDTO.getId()));
        Optional<PositionEntity> name = this.positionService.findByName(positionDTO.getName());
        if (name.isPresent() && !positionDTO.getName().equals(entity.getName())) {
            throw new BadRequestException("Name is exists!");
        }
        entity = this.positionService.save(positionConverter.toEntity(positionDTO));
        return ResponseEntity.ok(positionConverter.toDTO(entity));
    }

}
