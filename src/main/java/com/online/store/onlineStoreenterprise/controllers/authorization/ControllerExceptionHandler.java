package com.online.store.onlineStoreenterprise.controllers.authorization;

import com.online.store.onlineStoreenterprise.validation.errors.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorMessage> resourceNotFoundException(RuntimeException ex, WebRequest request) {
    ErrorMessage message = new ErrorMessage(ex.getMessage());
    return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
  }
}
