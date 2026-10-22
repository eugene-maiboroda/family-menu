package com.familymenu.backend.devguardfixture;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class FixtureTransferService {

    private final FixtureLedger fixtureLedger;

    public FixtureTransferService(FixtureLedger fixtureLedger) {
        this.fixtureLedger = fixtureLedger;
    }

    public void transferFunds(String sourceAccount, String targetAccount, BigDecimal amount) {
        if (sourceAccount == null || targetAccount == null || sourceAccount.equals(targetAccount)) {
            throw new IllegalArgumentException("Two distinct accounts are required");
        }
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

        this.transferAtomically(sourceAccount, targetAccount, amount);
    }

    @Transactional
    public void transferAtomically(String sourceAccount, String targetAccount, BigDecimal amount) {
        fixtureLedger.debitAccount(sourceAccount, amount);
        fixtureLedger.creditAccount(targetAccount, amount);
    }
}
