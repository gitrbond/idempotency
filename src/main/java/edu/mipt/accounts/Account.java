package edu.mipt.accounts;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Account {
    @Id
    private long id;
    private long balance;

    public Account() {
    }

    public Account(long id, long balance) {
        this.id = id;
        this.balance = balance;
    }

    public void withdraw(long amount) {
        if (balance < amount) {
            throw new IllegalStateException("Недостаточно средств на счету: " + balance);
        }
        balance -= amount;
    }

    public void deposit(long amount) {
        balance += amount;
    }
}