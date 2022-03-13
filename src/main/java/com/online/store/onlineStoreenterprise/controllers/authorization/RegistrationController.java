package com.online.store.onlineStoreenterprise.controllers.authorization;

import com.online.store.onlineStoreenterprise.dto.UserRegistrationRequest;
import com.online.store.onlineStoreenterprise.services.authorization.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth/sign-up")
@AllArgsConstructor
public class RegistrationController {

  private final RegistrationService registrationService;

  @PostMapping
  public String register(@RequestBody UserRegistrationRequest request) {
    return registrationService.register(request);
  }

  @GetMapping("/confirm")
  public String confirm(@RequestParam("token") String token) {
    return registrationService.confirm(token);
  }
}