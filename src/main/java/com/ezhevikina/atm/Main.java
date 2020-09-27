package com.ezhevikina.atm;

public class Main {

  public static void main(String[] args) {
    DbAccountManager manager = new DbAccountManager();
    manager.initialize();
    ATM atm = new ATM(new DbAccountManagerFactory().getManager());
    atm.start();
  }
}
