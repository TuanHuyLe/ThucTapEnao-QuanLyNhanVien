package com.enao.team2.quanlynhanvien.validatation;

import com.enao.team2.quanlynhanvien.constants.Constants;
import com.enao.team2.quanlynhanvien.dto.AddUserDTO;

public class ValidateUser {
    public static String[] check(AddUserDTO userDTO) {
        StringBuilder sb = new StringBuilder();
        if (userDTO.getUsername().length() < 3) {
            sb.append("Length of username must greater than 3 character!-");
        }
        if (userDTO.getPassword().length() < 6) {
            sb.append("Length of username must greater than 6 character!-");
        }
        if (userDTO.getFullName() == null || userDTO.getFullName().isEmpty()) {
            sb.append("Full name is required!-");
        }
        if (userDTO.getGender() == null) {
            sb.append("Gender is required!-");
        }
        if (userDTO.getEmail() == null || !Constants.VALID_EMAIL_ADDRESS_REGEX.matcher(userDTO.getEmail()).matches()) {
            sb.append("Email is invalid!-");
        }
        return sb.toString().split("-");
    }
}
