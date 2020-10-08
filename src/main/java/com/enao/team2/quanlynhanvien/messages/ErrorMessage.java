package com.enao.team2.quanlynhanvien.messages;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorMessage<T> {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private Integer status;
    private T message;
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
