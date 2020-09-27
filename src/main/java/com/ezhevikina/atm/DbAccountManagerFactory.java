package com.ezhevikina.atm;

public class DbAccountManagerFactory extends AccountManagerFactory {
  @Override
  public AccountService getManager() {
    return new DbAccountManager();
  }
}
