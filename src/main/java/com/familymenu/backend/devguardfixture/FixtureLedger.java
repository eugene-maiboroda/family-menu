package com.familymenu.backend.devguardfixture;

import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class FixtureLedger {

    private final Map<String, BigDecimal> balances = new HashMap<>(Map.of(
            "household", new BigDecimal("100.00"),
            "groceries", BigDecimal.ZERO
    ));

    public synchronized BigDecimal getAccountBalance(String accountId) {
        BigDecimal balance = balances.get(accountId);
        if (balance == null) {
            throw new IllegalArgumentException("Unknown fixture account: " + accountId);
        }
        return balance;
    }

    public synchronized void debitAccount(String accountId, BigDecimal amount) {
        validateAmount(amount);
        BigDecimal balance = getAccountBalance(accountId);
        if (balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient fixture funds");
        }
        changeAccountBalance(accountId, amount.negate());
    }

    public synchronized void creditAccount(String accountId, BigDecimal amount) {
        validateAmount(amount);
        getAccountBalance(accountId);
        changeAccountBalance(accountId, amount);
    }

    private void changeAccountBalance(String accountId, BigDecimal change) {
        if (TransactionSynchronizationManager.isActualTransactionActive()
                && TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCompletion(int status) {
                    if (status == STATUS_ROLLED_BACK) {
                        synchronized (FixtureLedger.this) {
                            balances.put(accountId, balances.get(accountId).subtract(change));
                        }
                    }
                }
            });
        }

        balances.put(accountId, balances.get(accountId).add(change));
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }
}
