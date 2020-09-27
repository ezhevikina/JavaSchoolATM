package com.ezhevikina.atm;

public class NotEnoughMoneyException extends Exception {

  public NotEnoughMoneyException(String message) {
    super(message);
  }
}
