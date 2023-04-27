package edu.mipt.controller;

public interface Accounts {
    String withdraw(long accountId, long amount);

    String deposit(long accountId, long amount);
}
