package com.enao.team2.quanlynhanvien.controller;

import com.enao.team2.quanlynhanvien.dto.ModuleDTO;
import com.enao.team2.quanlynhanvien.model.Module;
import com.enao.team2.quanlynhanvien.repository.IModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ModuleController {
    @Autowired
    private IModuleRepository moduleRepository;

    @GetMapping("/api/enao/module")
    public List<ModuleDTO> listAllModule(){
        List<Module> listModuleEntity = moduleRepository.findAll();
        List<ModuleDTO> listModuleDTO = new ArrayList<>();
        for (Module module :listModuleEntity) {
            ModuleDTO moduleDTO = new ModuleDTO();
            moduleDTO.setCode(module.getCode());
            moduleDTO.setName(module.getName());
            listModuleDTO.add(moduleDTO);
        }
        return listModuleDTO;
    }
}
