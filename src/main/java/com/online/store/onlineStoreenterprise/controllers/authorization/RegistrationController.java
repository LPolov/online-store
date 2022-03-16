package com.online.store.onlineStoreenterprise.controllers.authorization;

import com.online.store.onlineStoreenterprise.dto.RegistrationRequest;
import com.online.store.onlineStoreenterprise.services.authorization.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("api/auth/sign-up")
@AllArgsConstructor
public class RegistrationController {

  private final RegistrationService registrationService;

  @PostMapping
  public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {
    return new ResponseEntity<>(registrationService.register(request), HttpStatus.OK);
  }

  @GetMapping
  public ModelAndView getRegistrationPage() {
    return new ModelAndView("registration");
  }

  @GetMapping("/confirm")
  public String confirm(@RequestParam("token") String token) {
    return registrationService.confirm(token);
  }
}
