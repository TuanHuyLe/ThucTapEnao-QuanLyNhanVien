package com.enao.team2.quanlynhanvien.dto;

import com.enao.team2.quanlynhanvien.model.PermissionEntity;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO extends AbstractDTO {
    private String name;
    private String description;
    private String slug;
    private List<String> permissionIds;
}
