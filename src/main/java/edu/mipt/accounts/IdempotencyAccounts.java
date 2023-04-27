package edu.mipt.accounts;

import edu.mipt.controller.Accounts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class IdempotencyAccounts implements Accounts {
    private final AccountRepository accountRepository;

    @Override
    public String withdraw(long accountId, long amount) {
        return process(accountId, acc -> acc.withdraw(amount));
    }

    @Override
    public String deposit(long accountId, long amount) {
        return process(accountId, acc -> acc.deposit(amount));
    }

    private String process(long accountId, Consumer<Account> processing) {
        var account = accountRepository.findById(accountId);
        processing.accept(account);
        accountRepository.saveAndFlush(account);
        return "Успешно, текущий баланс: " + account.getBalance();
    }
}