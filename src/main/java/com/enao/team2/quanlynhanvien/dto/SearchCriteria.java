package com.enao.team2.quanlynhanvien.dto;

import com.enao.team2.quanlynhanvien.constants.ESearchOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchCriteria {
    private String key;
    private Object value;
    private ESearchOperation operation;
}
