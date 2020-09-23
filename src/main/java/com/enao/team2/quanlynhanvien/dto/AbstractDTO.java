package com.enao.team2.quanlynhanvien.dto;

import lombok.Data;

import java.util.UUID;

@Data
public abstract class AbstractDTO {
    private UUID id;
    private Boolean active;
}
