package com.online.store.onlineStoreenterprise.validation.validators;

import com.online.store.onlineStoreenterprise.validation.exceptions.AuthorizationException;
import org.springframework.stereotype.Service;

@Service
public class EmailValidator implements Validator<String> {

  private static final String EMAIL_REGEXP = "^[A-Za-z0-9+_.-]+@(.+)$";

  public void validate(String email) {
    if (!email.matches(EMAIL_REGEXP)) {
      throw new AuthorizationException("Email '" + email + "' is not valid");
    }
  }
}
