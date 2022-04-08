package com.online.store.onlineStoreenterprise.services.authorization;

import com.online.store.onlineStoreenterprise.dao.UserRepository;
import com.online.store.onlineStoreenterprise.models.authorization.ConfirmationToken;
import com.online.store.onlineStoreenterprise.models.authorization.User;
import com.online.store.onlineStoreenterprise.models.authorization.UserRole;
import com.online.store.onlineStoreenterprise.validation.exceptions.AuthorizationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

  private static final String USER_EMAIL_NOT_FOUND_MSG = "Email '%s' not found.";
  private static final String DEFAULT_USER_DATA = "admin";

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;
  private final ConfirmationTokenService confirmationTokenService;

  public UserService(UserRepository userRepository,
                     BCryptPasswordEncoder passwordEncoder,
                     ConfirmationTokenService confirmationTokenService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.confirmationTokenService = confirmationTokenService;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository.findByEmail(email)
        .orElseThrow(() ->
            new UsernameNotFoundException(
                String.format(USER_EMAIL_NOT_FOUND_MSG, email)));
  }

  public String signUpUser(User user) {
    String email = user.getEmail();
    userRepository.findByEmail(email).ifPresent((u) -> {
      //TODO: check if attributes are the same and if email not confirmed send confirmation message
      throw new AuthorizationException("Email '" + email + "' is already taken");
    });
    encodePassword(user);
    userRepository.save(user);
    return confirmationTokenService.createConfirmationToken(user).getToken();
  }

  private void encodePassword(User user) {
    String encodedPassword = passwordEncoder.encode(user.getPassword());
    user.setPassword(encodedPassword);
  }

  public void enableUser(User user) {
    user.setEnabled(true);
    userRepository.save(user);
  }

  public User getAuthenticatedUserFromRequest(HttpServletRequest request) {
    UsernamePasswordAuthenticationToken principal =
        (UsernamePasswordAuthenticationToken) request.getUserPrincipal();
    return (User) principal.getPrincipal();
  }

  @PostConstruct
  private void initDefaultAdmin() {
    Optional<User> defaultAdmin = userRepository.findByEmail(DEFAULT_USER_DATA);
    if (defaultAdmin.isEmpty()) {
      User user = createDefaultAdmin();
      configureDefaultAdmin(user);
    }
  }

  private User createDefaultAdmin() {
    User user = new User();
    user.setFirstName(DEFAULT_USER_DATA);
    user.setLastName(DEFAULT_USER_DATA);
    user.setEmail(DEFAULT_USER_DATA);
    user.setPassword(DEFAULT_USER_DATA);
    user.setRole(UserRole.ADMIN);
    user.setEnabled(true);
    user.setLocked(false);
    return user;
  }

  private void configureDefaultAdmin(User user) {
    encodePassword(user);
    userRepository.save(user);
    ConfirmationToken confirmationToken = confirmationTokenService.createConfirmationToken(user);
    confirmationTokenService.confirmToken(confirmationToken);
  }
}
