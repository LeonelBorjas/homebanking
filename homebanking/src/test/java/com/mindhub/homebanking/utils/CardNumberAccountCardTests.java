package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.models.Card;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class CardNumberAccountCardTests {
    @Test
    public void cardNumberIsCreated() { // Generar un número de tarjeta de 16 dígitos
        String cardNumber = CardUtils.getCardNumber();
        assertThat(cardNumber, is(not(emptyOrNullString())));
    }

    @Test
    public void cardNumberLengthIsCorrect() {
        String cardNumber = CardUtils.getCardNumber();
        assertThat(cardNumber.length(), is(16)); // Asumiendo que el número de tarjeta debe tener 16 dígitos
    }

    @Test
    public void cardNumberContainsOnlyDigits() {
        String cardNumber = CardUtils.getCardNumber();
        assertThat(cardNumber, matchesPattern("\\d{16}")); // Verificar que contiene solo dígitos
    }

    @Test
    public void cvvIsCreated() { // Generar un CVV de 3 dígitos
        int cvv = CardUtils.getCVV();
        assertThat(cvv, is(not(nullValue())));
    }

    @Test
    public void cvvIsWithinRange() {
        int cvv = CardUtils.getCVV();
        assertThat(cvv, is(both(greaterThanOrEqualTo(100)).and(lessThanOrEqualTo(999)))); // CVV de 3 dígitos
    }

    @Test
    public void testCardNumberGeneration() {
        String cardNumber = CardUtils.getCardNumber(); // Generar un número de tarjeta de 16 dígitos
        assertThat(cardNumber, is(not(emptyOrNullString()))); // Asumiendo que el número de tarjeta no debe estar vacío
        assertThat(cardNumber.length(), is(16)); // Asumiendo que el número de tarjeta debe tener 16 dígitos
        assertThat(cardNumber, matchesPattern("\\d{16}")); // Verificar que contiene solo dígitos
    }

    @Test
    public void testCvvGeneration() { // Generar un CVV de 3 dígitos
        int cvv = CardUtils.getCVV();
        assertThat(cvv, is(not(0)));
        assertThat(cvv, is(greaterThanOrEqualTo(100)));
        assertThat(cvv, is(lessThanOrEqualTo(999)));
    }
}
