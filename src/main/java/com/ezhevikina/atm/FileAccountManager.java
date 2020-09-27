package com.ezhevikina.atm;

import java.io.*;

public class FileAccountManager implements AccountService {

  private final static String DIR = "accounts";

  private void create(Account account) {
    File accountFile = new File(
        DIR
            + File.separator
            + account.getId());
    try {
      accountFile.createNewFile();
      write(account, accountFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void initialize() {
    File accounts = new File(DIR);
    if (!accounts.exists()) {
      accounts.mkdir();
      System.out.println("accounts dir created");

      for (int i = 0; i < 10; i++) {
        this.create(new Account(
            1000 + i, "HolderName" + i, 100 * i));
      }
    }
  }

  @Override
  public void withdraw(int accountId, int amount) throws NotEnoughMoneyException, UnknownAccountException {
    Account account = read(accountId);
    if (account != null) {
      if (account.getAmount() < Math.abs(amount)) {
        throw new NotEnoughMoneyException(String.format("Insufficient funds on the account №%d", accountId));
      }
      account.setAmount(account.getAmount() - Math.abs(amount));
      save(account);
    }
  }

  @Override
  public void balance(int accountId) throws UnknownAccountException {
    Account account = read(accountId);
    if (account != null) {
      System.out.println(account.getAmount());
    }
  }

  @Override
  public void deposit(int accountId, int amount) throws UnknownAccountException {
    Account account = read(accountId);
    if (account != null) {
      account.setAmount(account.getAmount() + Math.abs(amount));
      save(account);
    }
  }

  @Override
  public void transfer(int from, int to, int amount) throws NotEnoughMoneyException, UnknownAccountException {
    withdraw(from, amount);
    deposit(to, amount);
  }

  private Account read(int accountId) throws UnknownAccountException {
    File accountFile = new File(DIR + File.separator + accountId);
    if (!accountFile.exists()) {
      throw new UnknownAccountException(String.format("Account №%d not found", accountId));
    }
    try (FileReader fr = new FileReader(accountFile);
         BufferedReader br = new BufferedReader(fr)) {
      int id = Integer.parseInt(br.readLine());
      String holder = br.readLine();
      int amount = Integer.parseInt(br.readLine());
      return new Account(id, holder, amount);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private void write(Account account, File accountFile) {
    try (FileWriter fw = new FileWriter(accountFile.getAbsoluteFile());
         BufferedWriter bw = new BufferedWriter(fw)) {
      bw.write(String.valueOf(account.getId()));
      bw.newLine();
      bw.write(account.getHolder());
      bw.newLine();
      bw.write(String.valueOf(account.getAmount()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void save(Account account) throws UnknownAccountException {
    File accountFile = new File(
        DIR
            + File.separator
            + account.getId());
    if (!accountFile.exists()) {
      throw new UnknownAccountException(String.format("Account №%d not found", account.getId()));
    }
    write(account, accountFile);
  }
}
