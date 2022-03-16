package com.online.store.onlineStoreenterprise.validation.validators;

import com.online.store.onlineStoreenterprise.dto.RegistrationRequest;
import com.online.store.onlineStoreenterprise.validation.exceptions.AuthorizationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@AllArgsConstructor
public class RegistrationRequestValidator implements Validator<RegistrationRequest>{

  private final EmailValidator emailValidator;
  private final PasswordValidator passwordValidator;

  @Override
  public void validate(RegistrationRequest request) {
    if (isStringEmptyOrContainsSpaces(request.getFirstName())) {
      throw new AuthorizationException("First name cannot be empty and cannot contain spaces.");
    }

    if (isStringEmptyOrContainsSpaces(request.getLastName())) {
      throw new AuthorizationException("Last name cannot be empty and cannot contain spaces.");
    }
    emailValidator.validate(request.getEmail());
    passwordValidator.validate(request.getPassword());
  }

  private boolean isStringEmptyOrContainsSpaces(String string) {
    return ObjectUtils.isEmpty(string) || string.matches(".*\\s+.*");
  }
}
