package org.commitlink.procure.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpMessage exceptionHandler(BadRequestException ex, HttpServletRequest req) {
        return new HttpMessage(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), req.getServletPath());
    }

}
