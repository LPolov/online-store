package com.online.store.onlineStoreenterprise.services.authorization;

import com.online.store.onlineStoreenterprise.dao.UserRepository;
import com.online.store.onlineStoreenterprise.models.authorization.ConfirmationToken;
import com.online.store.onlineStoreenterprise.models.authorization.User;
import com.online.store.onlineStoreenterprise.models.authorization.UserRole;
import com.online.store.onlineStoreenterprise.validation.exceptions.AuthorizationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService{

  private static final String USER_EMAIL_NOT_FOUND_MSG = "Email '%s' not found.";

  private final Long TOKEN_VALIDITY_PERIOD_IN_MINUTES;
  private final UserRepository repository;
  private final BCryptPasswordEncoder passwordEncoder;
  private final ConfirmationTokenService confirmationTokenService;

  public UserService(@Value("${confirmation.token.validity.period.minutes}") Long TOKEN_VALIDITY_PERIOD_IN_MINUTES,
                     UserRepository repository,
                     BCryptPasswordEncoder passwordEncoder,
                     ConfirmationTokenService confirmationTokenService) {
    this.TOKEN_VALIDITY_PERIOD_IN_MINUTES = TOKEN_VALIDITY_PERIOD_IN_MINUTES;
    this.repository = repository;
    this.passwordEncoder = passwordEncoder;
    this.confirmationTokenService = confirmationTokenService;
    initDefaultUsers();
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return repository.findByEmail(email)
        .orElseThrow(() ->
            new UsernameNotFoundException(
                String.format(USER_EMAIL_NOT_FOUND_MSG, email)));
  }

  public String signUpUser(User user) {
    String email = user.getEmail();
    boolean userExists = repository.findByEmail(email)
        .isPresent();

    if (userExists) {
      //TODO: check if attributes are the same and if email not confirmed send confirmation message
      throw new AuthorizationException("Email '" + email + "' is already taken");
    }
    String encodedPassword = passwordEncoder.encode(user.getPassword());
    user.setPassword(encodedPassword);

    repository.save(user);
    String token = UUID.randomUUID().toString();
    ConfirmationToken confirmationToken = new ConfirmationToken(
        token,
        LocalDateTime.now(),
        null,
        LocalDateTime.now().plusMinutes(TOKEN_VALIDITY_PERIOD_IN_MINUTES),
        user
    );
    confirmationTokenService.saveConfirmationToken(confirmationToken);
    return token;
  }

  public void enableUser(String email) {
    User user = repository.findByEmail(email).get();
    user.setEnabled(true);
    repository.save(user);
  }

  @PostConstruct
  private void initDefaultUsers() {
    Optional<User> defaultAdmin = repository.findByEmail("admin");
    if (defaultAdmin.isEmpty()) {
      setDefaultUser();
    }
  }

  private void setDefaultUser() {
    User user = createDefaultUser();
    String token = signUpUser(user);
    confirmationTokenService.setConfirmedAt(token);
  }

  private User createDefaultUser() {
    User user = new User();
    user.setFirstName("admin");
    user.setLastName("admin");
    user.setEmail("admin");
    user.setPassword("admin");
    user.setRole(UserRole.ADMIN);
    user.setEnabled(true);
    user.setLocked(false);
    return user;
  }
}
