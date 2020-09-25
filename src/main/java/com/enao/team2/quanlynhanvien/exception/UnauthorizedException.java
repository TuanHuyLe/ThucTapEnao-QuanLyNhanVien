package com.enao.team2.quanlynhanvien.exception;

import com.enao.team2.quanlynhanvien.messages.MessageResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UnauthorizedException extends RuntimeException{
    private MessageResponse messageResponse;
}
