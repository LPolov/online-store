package com.online.store.onlineStoreenterprise.dao;

import com.online.store.onlineStoreenterprise.models.authorization.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, UUID> {
  Optional<ConfirmationToken> findByToken(String token);
}
