package com.online.store.onlineStoreenterprise.validation.validators;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailValidator {

  public boolean isValid(String email) {
    Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
  }
}
