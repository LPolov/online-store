package com.online.store.onlineStoreenterprise.services.authorization;

import com.online.store.onlineStoreenterprise.dto.LoginRequest;
import com.online.store.onlineStoreenterprise.validation.exceptions.AuthorizationException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService {

  private final UserService userService;

  public String login(LoginRequest request) {
    UserDetails userDetails = userService.loadUserByUsername(request.getEmail());
    passwordMatches(userDetails.getPassword(), request.getPassword());
    return "home";
  }

  private void passwordMatches(String passwordFromDb, String passwordFromRequest) {
    if (!passwordFromDb.equals(passwordFromRequest)) {
      throw new AuthorizationException(passwordFromRequest + " is not correct");
    }
  }
}
