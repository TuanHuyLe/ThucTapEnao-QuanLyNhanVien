package com.enao.team2.quanlynhanvien.validatation;

import com.enao.team2.quanlynhanvien.constants.Constants;
import com.enao.team2.quanlynhanvien.dto.LoginDTO;

import java.util.ArrayList;
import java.util.List;

public class ValidateLogin {
    public static List<String> check(LoginDTO loginDTO) {
        List<String> error = new ArrayList<>();
        if (loginDTO.getUsername().length() < 4) {
            error.add("Length of username must greater than 3 character!");
        } else if (!Constants.VALID_USERNAME_PASSWORD_REGEX.matcher(loginDTO.getUsername()).matches()) {
            error.add("Username is invalid!");
        }
        if (loginDTO.getPassword().length() < 6) {
            error.add("Length of password must greater than 5 character!");
        } else if (!Constants.VALID_USERNAME_PASSWORD_REGEX.matcher(loginDTO.getPassword()).matches()) {
            error.add("Password is invalid!");
        }
        return error;
    }
}
