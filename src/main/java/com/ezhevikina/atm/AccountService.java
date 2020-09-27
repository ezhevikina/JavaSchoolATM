package com.ezhevikina.atm;

public interface AccountService {

  void withdraw(int accountId, int amount) throws
      NotEnoughMoneyException, UnknownAccountException;

  void balance(int accountId) throws UnknownAccountException;

  void deposit(int accountId, int amount) throws UnknownAccountException;

  void transfer(int from, int to, int amount) throws
      NotEnoughMoneyException, UnknownAccountException;
}
