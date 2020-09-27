package com.ezhevikina.atm;

public class FileAccountManagerFactory extends AccountManagerFactory {
  @Override
  public AccountService getManager() {
    return new FileAccountManager();
  }
}
