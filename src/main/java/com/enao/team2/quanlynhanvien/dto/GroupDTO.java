package com.enao.team2.quanlynhanvien.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDTO extends AbstractDTO {
    private String name;
    private String description;
    private Set<String> userNames;
}
