package com.online.store.onlineStoreenterprise.handlers;

import com.online.store.onlineStoreenterprise.handlers.errors.ErrorMessage;
import com.online.store.onlineStoreenterprise.validation.exceptions.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(value = {AuthorizationException.class, UsernameNotFoundException.class})
  public ResponseEntity<ErrorMessage> handleAuthorizationException(Exception ex) {
    ErrorMessage message = new ErrorMessage(ex.getMessage(), LocalDateTime.now());
    return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
  }
}
