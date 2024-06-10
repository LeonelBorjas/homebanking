package com.mindhub.homebanking.utils.testRepos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.mindhub.homebanking.Utils.NumberAccount;
import static org.hamcrest.Matchers.*;
import java.time.LocalDate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.mindhub.homebanking.Utils.NumberAccountCard;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AccountTest {

    @Autowired
    private AccountRepository accountRepository;

    //Este fallara
//    @Test
//    public void accountExists() {
//        boolean accountExists = accountRepository.existsByNumber(NumberAccount.getCardNumber());
//        assertThat(accountExists, is(true));
//    }

    //Este funcionara
    @Test
    public void accountExistsGreen() {
        //Generamos un número de cuenta aleatorio
        String accountNumber = NumberAccount.eightDigits();

        // Creamos y guardamos una nueva cuenta con ese número, fecha de hoy y saldo inicial de 1000.0
        Account account = new Account(accountNumber, LocalDate.now(), 1000.0);
        accountRepository.save(account);

        // Comprobamos si la cuenta con el número generado existe en la base de datos
        boolean accountExists = accountRepository.existsByNumber(accountNumber);

        //Verificamos que la cuenta realmente exista
        assertThat(accountExists, is(true));
    }

    @Test
    public void accountNumberIsNotNull() {
        String accountNumber = NumberAccount.eightDigits();
        assertThat(accountNumber, is(notNullValue()));
    }
    @Test
    public void accountNumberIsCreated() {
        String accountNumber = NumberAccount.eightDigits();
        assertThat(accountNumber, is(not(emptyOrNullString())));
    }
}
