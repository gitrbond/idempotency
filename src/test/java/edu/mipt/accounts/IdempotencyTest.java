package edu.mipt.accounts;

import edu.mipt.accounts.impl.Account;
import edu.mipt.accounts.impl.AccountRepository;
import edu.mipt.accounts.impl.ResponseRecordRepository;
import io.vavr.Function3;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import static java.lang.Runtime.getRuntime;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class IdempotencyTest {
    private final ExecutorService executorService = newFixedThreadPool(getRuntime().availableProcessors());
    private final Map<String, AccountResponse> rqUidToAccountResponse = new ConcurrentHashMap<>();

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ResponseRecordRepository responseRecordRepository;
    @Autowired
    private Accounts accounts;

    private record Transfer(String rqUid, long accountId, long amount) {
    }

    @Test
    void testTransfer() {
        //given
        insertAccount();
        var withdrawTransfers = createTransfers(1500);
        var depositTransfers = createTransfers(2000);

        //execute withdraw transfers
        executeTransfers(withdrawTransfers, accounts::withdraw, 0);

        //execute deposit transfers
        executeTransfers(depositTransfers, accounts::deposit, 20_000);

        //execute withdraw transfers again
        executeTransfers(withdrawTransfers, accounts::withdraw, 20_000);

        //execute deposit transfers again
        executeTransfers(depositTransfers, accounts::deposit, 20_000);
    }

    private void insertAccount() {
        var account = new Account();
        account.setBalance(10_000);
        accountRepository.saveAndFlush(account);
    }

    private void executeTransfers(List<Transfer> withdrawTransfers, Function3<String, Long, Long, AccountResponse> transfer, int expectedBalance) {
        Account account;
        executeTransfers(withdrawTransfers, transfer);
        account = accountRepository.findById(0);
        assertEquals(expectedBalance, account.getBalance());
    }

    private List<Transfer> createTransfers(int count) {
        var transfers = new ArrayList<Transfer>();
        for (int i = 0; i < count; i++) {
            Transfer transfer = new Transfer(UUID.randomUUID().toString(), 0, 10);
            transfers.add(transfer);
        }
        return transfers;
    }

    private void executeTransfers(List<Transfer> transfers, Function3<String, Long, Long, AccountResponse> transfer) {
        List<CompletableFuture<Void>> futures = transfers.stream()
                .map(t -> runAsync(() -> executeTransfer(transfer, t), executorService))
                .toList();
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private void executeTransfer(Function3<String, Long, Long, AccountResponse> transfer, Transfer t) {
        var response = transfer.apply(t.rqUid, t.accountId, t.amount);
        var previousResponse = rqUidToAccountResponse.putIfAbsent(t.rqUid, response);
        if (previousResponse != null) {
            assertEquals(previousResponse, response);
        }
    }
}
