package edu.mipt.accounts;

import lombok.AllArgsConstructor;

import static edu.mipt.accounts.AccountResponseStatus.OK;

@AllArgsConstructor
public class AccountResponse {
    private AccountResponseStatus status;
    private long balance;

    public static AccountResponse okResponse(long balance) {
        return new AccountResponse(OK, balance);
    }
}