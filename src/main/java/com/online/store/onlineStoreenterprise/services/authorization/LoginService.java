package com.online.store.onlineStoreenterprise.services.authorization;

import com.online.store.onlineStoreenterprise.dto.UserLoginRequest;
import com.online.store.onlineStoreenterprise.validation.exceptions.AuthorizationException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService {

  private final UserService userService;

  public String login(UserLoginRequest request) {
    UserDetails userDetails = userService.loadUserByUsername(request.getEmail());
    if (!userDetails.getPassword().equals(request.getPassword())) {
      throw new AuthorizationException( request.getPassword() + " is not correct");
    }
    return "home";
  }
}
