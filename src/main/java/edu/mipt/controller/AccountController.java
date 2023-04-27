package edu.mipt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final Accounts accounts;

    @PostMapping
    String withdraw(long accountId, long amount) {
        return accounts.withdraw(accountId, amount);
    }

    @PostMapping
    String deposit(long accountId, long amount) {
        return accounts.deposit(accountId, amount);
    }
}
