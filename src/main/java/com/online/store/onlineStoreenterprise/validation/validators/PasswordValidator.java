package com.online.store.onlineStoreenterprise.validation.validators;

import com.online.store.onlineStoreenterprise.validation.exceptions.AuthorizationException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class PasswordValidator implements Validator<String> {

  private static final String PASSWORD_REGEXP = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

  public void validate(String password) {
    if (ObjectUtils.isEmpty(password) || !password.matches(PASSWORD_REGEXP)) {
      throw new AuthorizationException("Password " + password + " is not valid. " +
          "It must be at least 8 characters length, must contain at least one digit," +
          " one special character, one uppercase character, one lowercase character. " +
          "No whitespaces allowed.");
    }
  }
}
