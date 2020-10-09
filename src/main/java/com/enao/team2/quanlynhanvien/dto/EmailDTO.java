package com.enao.team2.quanlynhanvien.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailDTO {
    private String recipients;
    private String subject;
    private String content;
}
