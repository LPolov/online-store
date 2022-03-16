package com.online.store.onlineStoreenterprise.services.authorization;

import com.online.store.onlineStoreenterprise.dao.ConfirmationTokenRepository;
import com.online.store.onlineStoreenterprise.models.authorization.ConfirmationToken;
import com.online.store.onlineStoreenterprise.models.authorization.User;
import com.online.store.onlineStoreenterprise.validation.exceptions.AuthorizationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConfirmationTokenService {

  private final ConfirmationTokenRepository repository;
  private final Long tokenValidityMinutes;

  public ConfirmationTokenService(ConfirmationTokenRepository repository, @Value("${confirmation.token.validity.period.minutes}") Long tokenValidityMinutes) {
    this.repository = repository;
    this.tokenValidityMinutes = tokenValidityMinutes;
  }

  public Optional<ConfirmationToken> getConfirmationTokenByToken(String token) {
    return repository.findByToken(token);
  }

  public void confirmToken(ConfirmationToken confirmationToken) {
    confirmationToken.setConfirmedAt(LocalDateTime.now());
    repository.save(confirmationToken);
  }

  public ConfirmationToken createConfirmationToken(User user) {
    String token = UUID.randomUUID().toString();
    ConfirmationToken confirmationToken = new ConfirmationToken(
        token,
        LocalDateTime.now(),
        null,
        LocalDateTime.now().plusMinutes(tokenValidityMinutes),
        user
    );
    repository.save(confirmationToken);
    return confirmationToken;
  }

  public ConfirmationToken getConfirmationTokenIfTokenValid(String token) {
    ConfirmationToken confirmationToken = getConfirmationTokenByToken(token)
        .orElseThrow(() -> new AuthorizationException("Token '" + token + "' not found"));

    if (!ObjectUtils.isEmpty(confirmationToken.getConfirmedAt())) {
      throw new AuthorizationException("email is already confirmed");
    }

    LocalDateTime expiresAt = confirmationToken.getExpiresAt();
    if (expiresAt.isBefore(LocalDateTime.now())) {
      throw new AuthorizationException( token + " token is expired");
    }

    return confirmationToken;
  }
}
