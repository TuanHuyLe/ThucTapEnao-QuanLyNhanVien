package com.enao.team2.quanlynhanvien.dto;

import com.enao.team2.quanlynhanvien.model.GroupEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDTO extends AbstractDTO {
    private String name;
    private String description;

    GroupDTO(GroupEntity groupEntity){
        this.name = groupEntity.getName();
        this.description = groupEntity.getDescription();
    }
}
