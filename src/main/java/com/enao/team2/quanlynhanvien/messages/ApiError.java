package com.enao.team2.quanlynhanvien.messages;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class ApiError {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;    //current time error
    private Integer status;             //http status code
    private String message;             //error message associated with exception
    private List<String> errors;        //list of constructed error messages

    public ApiError(LocalDateTime timestamp, Integer status, String message, List<String> errors) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiError(LocalDateTime timestamp, Integer status, String message, String error) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.errors = Collections.singletonList(error);
    }

    @Override
    public String toString() {
        return "{\n" +
                "    \"timestamp\": \"" + timestamp + "\",\n" +
                "    \"status\": " + status + ",\n" +
                "    \"message\": \"" + message + "\",\n" +
                "    \"errors\": \"" + errors + "\"\n" +
                "}";
    }
}
