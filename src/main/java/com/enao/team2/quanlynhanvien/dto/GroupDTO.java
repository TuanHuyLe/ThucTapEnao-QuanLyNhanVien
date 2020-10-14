package com.enao.team2.quanlynhanvien.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDTO extends AbstractDTO {
    private String name;
    private String description;
    private String slugName;
    private String slugDescription;
}