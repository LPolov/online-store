package com.online.store.onlineStoreenterprise.services.authorization;

import com.online.store.onlineStoreenterprise.dao.UserRepository;
import com.online.store.onlineStoreenterprise.models.authorization.ConfirmationToken;
import com.online.store.onlineStoreenterprise.models.authorization.User;
import com.online.store.onlineStoreenterprise.validation.exceptions.EmailAlreadyTaken;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

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
      throw new EmailAlreadyTaken(email + " is already taken");
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

//  public boolean isEmailRegistered(String email) {
//    Optional<User> user = repository.findByEmail(email);
//    return user.isPresent();
//  }
//
//  public boolean isPasswordCorrect(String email) {
//    Optional<User> user = repository.findByEmail(email);
//    return user.isPresent();
//  }
}
