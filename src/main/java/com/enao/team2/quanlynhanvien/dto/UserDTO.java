package com.enao.team2.quanlynhanvien.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends AbstractDTO {
    private String username;
    private String fullName;
    private Boolean gender;
    private String email;
    private String slug;
    private String groupName;
    private String positionName;
    private List<String> roleName;
}
