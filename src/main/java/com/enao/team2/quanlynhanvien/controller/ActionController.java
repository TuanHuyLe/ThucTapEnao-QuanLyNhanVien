package com.enao.team2.quanlynhanvien.controller;

import com.enao.team2.quanlynhanvien.dto.ActionDTO;
import com.enao.team2.quanlynhanvien.model.Action;
import com.enao.team2.quanlynhanvien.repository.IActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ActionController {

    @Autowired
    IActionRepository actionRepository;

    @GetMapping("api/enao/action")
    public List<ActionDTO> listAllAction(){
        List<Action> listActionEntity = actionRepository.findAll();
        List<ActionDTO> listActionDTO = new ArrayList<>();
        for (Action action : listActionEntity) {
            ActionDTO actionDTO = new ActionDTO();
            actionDTO.setCode(action.getCode());
            actionDTO.setName(action.getName());
            listActionDTO.add(actionDTO);
        }
        return listActionDTO;
    }
}
