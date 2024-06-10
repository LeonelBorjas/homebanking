package com.mindhub.homebanking.utils.testRepos;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class LoanTest {
    @Autowired
    LoanRepository loanRepository;


    @Test

    public void existLoans(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans,is(not(empty())));
    }

    @Test

    public void existPersonalLoan(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans, hasItem(hasProperty("name", is("Personal Loan"))));

    }

    @Test
    public void loanExists() {
        boolean loanExists = loanRepository.existsById(1L);
        assertThat(loanExists, is(true));
    }

    @Test
    public void createLoan(){
        List<Integer> dues4 = Arrays.asList(60, 48, 36, 24, 12);
        Loan loan = new Loan("Mortgage", 1000, dues4);
        loanRepository.save(loan);
        assertThat(loan, is(not(nullValue())));
    }



}
