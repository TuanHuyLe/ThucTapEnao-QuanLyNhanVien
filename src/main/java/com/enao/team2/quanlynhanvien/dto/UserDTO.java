package com.enao.team2.quanlynhanvien.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends AbstractDTO {
    private String username;
    private String password;
    private String fullName;
    private Boolean gender;
    private String email;
}
