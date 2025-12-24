package org.commitlink.procure.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({ MethodArgumentNotValidException.class, InvalidInputException.class })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public HttpMessage exceptionHandler(Exception ex, HttpServletRequest req) {
    if (ex instanceof MethodArgumentNotValidException me) return new HttpMessage(
      me.getBindingResult().getFieldErrors().get(0).getDefaultMessage(),
      HttpStatus.BAD_REQUEST.value(),
      req.getServletPath()
    );
    return new HttpMessage(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), req.getServletPath());
  }

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public HttpMessage exceptionHandler(NotFoundException ex, HttpServletRequest req) {
    return new HttpMessage(ex.getLocalizedMessage(), HttpStatus.NOT_FOUND.value(), req.getServletPath());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public HttpMessage exceptionHandler(DataIntegrityViolationException ex, HttpServletRequest req) {
    return new HttpMessage(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), req.getServletPath());
  }

  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public HttpMessage exceptionHandler(AuthenticationException ex, HttpServletRequest req) {
    return new HttpMessage(ex.getMessage(), HttpStatus.FORBIDDEN.value(), req.getServletPath());
  }
}
