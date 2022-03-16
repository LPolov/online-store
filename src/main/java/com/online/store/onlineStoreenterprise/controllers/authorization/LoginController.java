package com.online.store.onlineStoreenterprise.controllers.authorization;

import com.online.store.onlineStoreenterprise.dto.LoginRequest;
import com.online.store.onlineStoreenterprise.services.authorization.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/auth/sign-in")
@AllArgsConstructor
public class LoginController {

  private final LoginService loginService;

  @PostMapping
  public String login(@RequestBody LoginRequest request) {
    return loginService.login(request);
  }

  @GetMapping
  public String login() {
    return "login";
  }

  @GetMapping("/error")
  public String errorHandling(Model model) {
    model.addAttribute("loginError", true);
    return "login";
  }
}
