package com.enao.team2.quanlynhanvien.validatation;

import com.enao.team2.quanlynhanvien.constants.Constants;
import com.enao.team2.quanlynhanvien.dto.AddUserDTO;

import java.util.ArrayList;
import java.util.List;

public class ValidateUser {
    public static List<String> check(AddUserDTO userDTO) {
        List<String> error = new ArrayList<>();
        if (userDTO.getUsername().length() < 3) {
            error.add("Length of username must greater than 3 character!");
        }
        if (userDTO.getPassword().length() < 6) {
            error.add("Length of password must greater than 6 character!");
        }
        if (userDTO.getFullName() == null || userDTO.getFullName().isEmpty()) {
            error.add("Full name is required!");
        }
        if (userDTO.getGender() == null) {
            error.add("Gender is required!");
        }
        if (userDTO.getEmail() == null || !Constants.VALID_EMAIL_ADDRESS_REGEX.matcher(userDTO.getEmail()).matches()) {
            error.add("Email is invalid!");
        }
        if (userDTO.getPositionName() == null) {
            error.add("Position is required!");
        }
        if(userDTO.getRoleName().size() ==0){
            error.add("Role name is required!");
        }
        return error;
    }
}
