package edu.mipt.accounts;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;

import static edu.mipt.accounts.AccountResponseStatus.OK;

@Data
@Embeddable
@AllArgsConstructor
public class AccountResponse {
    private AccountResponseStatus status;
    private long balance;

    public static AccountResponse okResponse(long balance) {
        return new AccountResponse(OK, balance);
    }
}