package com.online.store.onlineStoreenterprise.services.authorization;

import com.online.store.onlineStoreenterprise.dto.UserRegistrationRequest;
import com.online.store.onlineStoreenterprise.models.authorization.ConfirmationToken;
import com.online.store.onlineStoreenterprise.models.authorization.User;
import com.online.store.onlineStoreenterprise.models.authorization.UserRole;
import com.online.store.onlineStoreenterprise.services.email.EmailService;
import com.online.store.onlineStoreenterprise.validation.exceptions.EmailAlreadyConfirmedException;
import com.online.store.onlineStoreenterprise.validation.exceptions.InvalidEmailException;
import com.online.store.onlineStoreenterprise.validation.exceptions.TokenAlreadyInUseException;
import com.online.store.onlineStoreenterprise.validation.exceptions.ExpiredTokenException;
import com.online.store.onlineStoreenterprise.validation.validators.EmailValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

  private final EmailValidator emailValidator;
  private final UserService userService;
  private final ConfirmationTokenService confirmationTokenService;
  private final EmailService emailService;
  private final String link = "http://localhost:8080/api/auth/sign-up/confirm?token=";

  public String register(UserRegistrationRequest request) {
    String email = request.getEmail();
    if (!emailValidator.isValid(email)) {
      throw new InvalidEmailException(email + " is not valid");
    }
    User user = new User(request, UserRole.USER);
    String token = userService.signUpUser(user);
    String message = buildEmail(user.getFirstName(), user.getLastName(), link + token);
    emailService.send(user.getEmail(), message);
    return token;
  }

  @Transactional
  public String confirm(String token) {
    ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
        .orElseThrow(() -> new TokenAlreadyInUseException(token + " is already in use"));
    if (confirmationToken.getConfirmedAt() != null) {
      throw new EmailAlreadyConfirmedException("email is already confirmed");
    }
    LocalDateTime expiresAt = confirmationToken.getExpiresAt();
    if (expiresAt.isBefore(LocalDateTime.now())) {
      throw new ExpiredTokenException();
    }
    confirmationTokenService.setConfirmedAt(token);
    userService.enableUser(confirmationToken.getUser().getEmail());
    return "confirmed";
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
}
