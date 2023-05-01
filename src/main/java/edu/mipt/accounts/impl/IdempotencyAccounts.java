package edu.mipt.accounts.impl;

import edu.mipt.accounts.AccountResponse;
import edu.mipt.accounts.Accounts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

import static edu.mipt.accounts.AccountResponse.okResponse;

@Service
@Transactional(noRollbackFor = AccountException.class)
@RequiredArgsConstructor
public class IdempotencyAccounts implements Accounts {
    private final AccountRepository accountRepository;

    @Override
    public AccountResponse withdraw(String rqUid, long accountId, long amount) {
        return process(accountId, acc -> acc.withdraw(amount));
    }

    @Override
    public AccountResponse deposit(String rqUid, long accountId, long amount) {
        return process(accountId, acc -> acc.deposit(amount));
    }

    private AccountResponse process(long accountId, Consumer<Account> processing) {
        var account = accountRepository.findById(accountId);
        try {
            processing.accept(account);
            accountRepository.saveAndFlush(account);
            return okResponse(account.getBalance());
        } catch (AccountException e) {
            return e.toAccountResponse();
        }
    }
}