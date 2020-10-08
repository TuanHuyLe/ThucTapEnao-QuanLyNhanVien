package com.enao.team2.quanlynhanvien.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorMessage {
    private Date timestamp;
    private Integer status;
    private String message;
    private String description;

    @Override
    public String toString() {
        return "{\n" +
                "    \"timestamp\": \"" + timestamp + "\",\n" +
                "    \"status\": " + status + ",\n" +
                "    \"message\": \"" + message + "\",\n" +
                "    \"description\": \"" + description + "\"\n" +
                "}";
    }
}
