package com.online.store.onlineStoreenterprise.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserLoginRequest {
  private final String email;
  private final String password;
}
