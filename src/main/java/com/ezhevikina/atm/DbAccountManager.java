package com.ezhevikina.atm;

import java.sql.*;

public class DbAccountManager implements AccountService {

  private Connection connection;
  private PreparedStatement statement;
  private final String url = "jdbc:h2:mem:test;INIT=RUNSCRIPT FROM './schema.sql'";

  public DbAccountManager() {
    try {
      this.connection = DriverManager.getConnection(url);
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  private void create(Account account) throws SQLException {
    String createAccount = "INSERT INTO ACCOUNTS(id, holder, amount) VALUES (?, ?, ?)";
    statement = connection.prepareStatement(createAccount);
    statement.setInt(1, account.getId());
    statement.setString(2, account.getHolder());
    statement.setInt(3, account.getAmount());
    statement.executeUpdate();
  }

  public void initialize() {
    for (int i = 0; i < 10; i++) {
      try {
        this.create(new Account(
            1000 + i, "HolderName" + i, 100 * i));
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }
    }
  }

  @Override
  public void withdraw(int accountId, int amount) throws NotEnoughMoneyException, UnknownAccountException {
    String withdraw = String.format(
        "UPDATE accounts SET amount = amount - %d WHERE id = %d", amount, accountId);
    try {
      if (getBalance(accountId) < amount) {
        throw new NotEnoughMoneyException(String.format("Insufficient funds on the account №%d", accountId));
      }
      statement = connection.prepareStatement(withdraw);
      statement.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  @Override
  public void balance(int accountId) throws UnknownAccountException {
    try {
      System.out.println(getBalance(accountId));
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  @Override
  public void deposit(int accountId, int amount) throws UnknownAccountException {
    String deposit = String.format(
        "UPDATE accounts SET amount = amount + %d WHERE id = %d", amount, accountId);
    try {
      statement = connection.prepareStatement(deposit);
      int resultSet = statement.executeUpdate();
      if (resultSet == 0) {
        throw new UnknownAccountException(String.format("Account №%d not found", accountId));
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  @Override
  public void transfer(int from, int to, int amount) throws NotEnoughMoneyException, UnknownAccountException {
    withdraw(from, amount);
    deposit(to, amount);
  }

  private int getBalance(int accountId) throws UnknownAccountException, SQLException {
    String getBalance = "SELECT amount FROM accounts WHERE id = " + accountId;
    statement = connection.prepareStatement(getBalance);
    ResultSet resultSet = statement.executeQuery();
    if (resultSet.next()) {
      return resultSet.getInt("amount");
    } else {
      throw new UnknownAccountException(String.format("Account №%d not found", accountId));
    }
  }

  public void close() {
    try {
      statement.close();
      connection.close();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }
}
