package edu.mipt.accounts;

public interface Accounts {
    AccountResponse withdraw(String rqUid, long accountId, long amount);

    AccountResponse deposit(String rqUid, long accountId, long amount);
}
