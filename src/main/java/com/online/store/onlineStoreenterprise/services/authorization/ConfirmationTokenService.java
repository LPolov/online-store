package com.online.store.onlineStoreenterprise.services.authorization;

import com.online.store.onlineStoreenterprise.dao.ConfirmationTokenRepository;
import com.online.store.onlineStoreenterprise.models.authorization.ConfirmationToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

  private final ConfirmationTokenRepository repository;

  public void saveConfirmationToken(ConfirmationToken confirmationToken) {
    repository.save(confirmationToken);
  }

  public Optional<ConfirmationToken> getToken(String token) {
    return repository.findByToken(token);
  }

  public void setConfirmedAt(String token) {
    ConfirmationToken confirmationToken = repository.findByToken(token).get();
    confirmationToken.setConfirmedAt(LocalDateTime.now());
    repository.save(confirmationToken);
  }
}
