package com.enao.team2.quanlynhanvien.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangePasswordDTO {
    private String userName;
    private String email;
    private String oldPassword;
    private String newPassword;
}
