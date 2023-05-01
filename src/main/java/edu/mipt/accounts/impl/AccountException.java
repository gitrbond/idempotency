package edu.mipt.accounts.impl;

import edu.mipt.accounts.AccountResponse;
import lombok.AllArgsConstructor;

import static edu.mipt.accounts.AccountResponseStatus.ERROR;

@AllArgsConstructor
public class AccountException extends RuntimeException {
    public long balance;

    public AccountResponse toAccountResponse() {
        return new AccountResponse(ERROR, balance);
    }
}
