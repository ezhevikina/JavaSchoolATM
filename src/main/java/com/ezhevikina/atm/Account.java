package com.ezhevikina.atm;

public class Account {

  private final int id;
  private final String holder;
  private int amount;

  public Account(int id, String holder, int amount) {
    this.id = id;
    this.holder = holder;
    this.amount = amount;
  }

  public int getId() {
    return id;
  }

  public String getHolder() {
    return holder;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }
}
