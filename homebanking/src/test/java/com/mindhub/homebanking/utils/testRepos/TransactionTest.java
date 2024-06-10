package com.mindhub.homebanking.utils.testRepos;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.hamcrest.MatcherAssert.assertThat;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.Matchers.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class TransactionTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void saveTransactionReturnsSavedTransaction() {
        Transaction transaction = new Transaction(TransactionType.CREDIT, 100.0, LocalDateTime.now(), "Deposit transaction");
        Transaction savedTransaction = transactionRepository.save(transaction);
        assertNotNull(savedTransaction);
        assertNotNull(savedTransaction.getId());
        assertEquals(transaction.getType(), savedTransaction.getType());
        assertEquals(transaction.getAmount(), savedTransaction.getAmount());
        assertEquals(transaction.getDate(), savedTransaction.getDate());
        assertEquals(transaction.getDescripton(), savedTransaction.getDescripton());
    }

    @Test
    public void transactionExists() {
        boolean transactionExists = transactionRepository.existsById(1L);
        assertThat(transactionExists, is(true));
    }


}
