package com.online.store.onlineStoreenterprise.errors;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ErrorMessage {

  private String message;
  private String happenedAt;

  public ErrorMessage(String message, LocalDateTime happenedAt) {
    this.message = message;
    this.happenedAt = DateTimeFormatter.ISO_DATE_TIME
        .format(happenedAt);
  }
}
