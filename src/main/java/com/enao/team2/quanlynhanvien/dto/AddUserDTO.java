package com.enao.team2.quanlynhanvien.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddUserDTO extends AbstractDTO{
    private String username;
    private String password;
    private String fullName;
    private Boolean gender;
    private String email;
    private String slug;
    private String groupName;
    private String positionName;
    private List<String> roleName;
}
