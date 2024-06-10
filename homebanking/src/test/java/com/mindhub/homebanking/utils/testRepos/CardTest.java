package com.mindhub.homebanking.utils.testRepos;

import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.hamcrest.Matchers.*;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CardTest {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    ClientRepository clientRepository;

    @Test
    public void cardNumberIsCreated(){
        String cardNumber = CardUtils.getCardNumber();
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }

    @Test
    public void cardNumberCVV(){
        String cardCVV = CardUtils.getCVV() + " ";
        assertThat(cardCVV,is(not(emptyOrNullString())));
    }
}
