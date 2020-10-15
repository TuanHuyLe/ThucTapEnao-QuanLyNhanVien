package com.enao.team2.quanlynhanvien.validatation;

import com.enao.team2.quanlynhanvien.dto.GroupDTO;

import java.util.ArrayList;
import java.util.List;

public class ValidateGroup {
    public static List<String> check(GroupDTO groupDTO){
        List<String> error = new ArrayList<>();
        if (groupDTO.getName() == null || groupDTO.getName().equals("")){
            error.add("Name is required!");
        }
        return error;
    }
}
