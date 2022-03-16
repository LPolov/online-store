package com.online.store.onlineStoreenterprise.services.authorization;

import com.online.store.onlineStoreenterprise.dto.RegistrationRequest;
import com.online.store.onlineStoreenterprise.models.authorization.ConfirmationToken;
import com.online.store.onlineStoreenterprise.models.authorization.User;
import com.online.store.onlineStoreenterprise.models.authorization.UserRole;
import com.online.store.onlineStoreenterprise.services.email.EmailService;
import com.online.store.onlineStoreenterprise.validation.validators.RegistrationRequestValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class RegistrationService {

  private static final String LINK = "http://localhost:8080/api/auth/sign-up/confirm?token=";

  private final RegistrationRequestValidator registrationRequestValidator;
  private final UserService userService;
  private final ConfirmationTokenService confirmationTokenService;
  private final EmailService emailService;

  @Transactional
  public String register(RegistrationRequest request) {
    registrationRequestValidator.validate(request);
    User user = new User(request, UserRole.USER);
    String token = userService.signUpUser(user);
    String message = buildEmail(user.getFirstName(), user.getLastName(), LINK + token);
    emailService.send(user.getEmail(), message);
    return token;
  }

  private String buildEmail(String firstName, String lastName, String link) {
    String message = "<div>\n" +
        "<p>Hello, %s %s! " +
        "It's great to see that you have registered a new account!</p>\n" +
        "<p>Confirm your account using a link below.</p>\n" +
        "<button><a href=\"%s\">Confirm</a></button>\n" +
        "</div>";
    return String.format(message, firstName, lastName, link);
  }

  @Transactional
  public String confirm(String token) {
    ConfirmationToken confirmationToken = confirmationTokenService.getConfirmationTokenIfTokenValid(token);
    confirmationTokenService.confirmToken(confirmationToken);
    userService.enableUser(confirmationToken.getUser());
    return "confirmed";
  }
}
