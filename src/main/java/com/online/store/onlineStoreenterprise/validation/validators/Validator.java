package com.online.store.onlineStoreenterprise.validation.validators;

import com.online.store.onlineStoreenterprise.validation.exceptions.AuthorizationException;

public interface Validator<T> {

  void validate(T object) throws AuthorizationException;
}
