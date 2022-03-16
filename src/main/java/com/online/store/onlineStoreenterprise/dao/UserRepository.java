package com.online.store.onlineStoreenterprise.dao;

import com.online.store.onlineStoreenterprise.models.authorization.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByEmail(String email);
}
