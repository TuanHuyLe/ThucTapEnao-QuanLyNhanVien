package com.enao.team2.quanlynhanvien.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class JwtExceptionController {
    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity<?> exception(UnauthorizedException exception) {
        return new ResponseEntity<>(exception.getMessageResponse(), HttpStatus.UNAUTHORIZED);
    }
}
