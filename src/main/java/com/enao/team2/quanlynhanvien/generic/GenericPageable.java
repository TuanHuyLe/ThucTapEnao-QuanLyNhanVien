package com.enao.team2.quanlynhanvien.generic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GenericPageable {
    private Integer pageIndex;
    private Integer pageSize;

    //first result per page
    public Integer firstResult() {
        return (pageIndex - 1) * pageSize;
    }
}
