package com.online.store.onlineStoreenterprise.models.authorization;

import com.online.store.onlineStoreenterprise.models.authorization.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {

  @Id
  @GeneratedValue
  @Type(type = "uuid-char")
  private UUID id;
  @Column(nullable = false)
  private String token;
  @Column(nullable = false)
  private LocalDateTime createdAt;
  @Column(nullable = false)
  private LocalDateTime expiresAt;
  private LocalDateTime confirmedAt;
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  public ConfirmationToken(String token, LocalDateTime createdAt,
                           LocalDateTime confirmedAt, LocalDateTime expiresAt, User user) {
    this.token = token;
    this.createdAt = createdAt;
    this.confirmedAt = confirmedAt;
    this.expiresAt = expiresAt;
    this.user = user;
  }
}

