package edu.mipt.accounts.impl;

import edu.mipt.accounts.AccountResponse;
import edu.mipt.accounts.Accounts;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static edu.mipt.accounts.AccountResponse.okResponse;

@Service
@Transactional(noRollbackFor = AccountException.class)
@RequiredArgsConstructor
@Retryable(maxAttempts = 20)
public class IdempotencyAccounts implements Accounts {
    private final AccountRepository accountRepository;

    private Map<String, AccountResponse> indempotencyStore = new HashMap<>();

    @Override
    public AccountResponse withdraw(String rqUid, long accountId, long amount) {
        return processIfNotProcessed(rqUid, accountId, acc -> acc.withdraw(amount));
    }

    @Override
    public AccountResponse deposit(String rqUid, long accountId, long amount) {
        return processIfNotProcessed(rqUid, accountId, acc -> acc.deposit(amount));
//        processIfNotProcessed()
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

    private AccountResponse processIfNotProcessed(String rqUid, long accountId, Consumer<Account> processing) {
        if (indempotencyStore.containsKey(rqUid)) {
            return indempotencyStore.get(rqUid);
        }
        AccountResponse result = process(accountId, processing);
        indempotencyStore.put(rqUid, result);
        return result;
    }
}