package edu.mipt.accounts;

import static edu.mipt.accounts.AccountResponseStatus.OK;

public record AccountResponse(AccountResponseStatus status, long balance) {
    public static AccountResponse okResponse(long balance) {
        return new AccountResponse(OK, balance);
    }
}