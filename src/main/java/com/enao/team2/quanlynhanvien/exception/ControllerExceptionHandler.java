package com.enao.team2.quanlynhanvien.exception;

import com.enao.team2.quanlynhanvien.messages.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ControllerExceptionHandler {


    /**
     * exception resource not found - status 404
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(value = {ResourceNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ApiError resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getDescription(false));

        return error;
    }

    /**
     * exception unauthorized - status 401
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(value = {UnauthorizedException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ApiError unauthorizedException(UnauthorizedException ex, WebRequest request) {
        ApiError message = new ApiError(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                request.getDescription(false));

        return message;
    }

    /**
     * exception forbidden - status 403
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(value = {ForbiddenException.class})
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ApiError forbiddenException(ForbiddenException ex, WebRequest request) {
        ApiError message = new ApiError(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage(),
                request.getDescription(false));

        return message;
    }

    /**
     * exception internal server error - status 500
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(value = {InternalServerErrorException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError internalServerErrorException(InternalServerErrorException ex, WebRequest request) {
        ApiError message = new ApiError(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                request.getDescription(false));

        return message;
    }

    /**
     * exception bad request - status 400
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(value = {BadRequestException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiError badRequestException(BadRequestException ex, WebRequest request) {
        ApiError message = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getDescription(false));

        return message;
    }

}
