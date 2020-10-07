package com.enao.team2.quanlynhanvien.dto;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PositionDTO extends AbstractDTO {
    private String name;
    private String description;
    private List<String> userName;
}
