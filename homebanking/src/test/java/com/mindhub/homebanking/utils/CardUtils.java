package com.mindhub.homebanking.utils;

import java.util.Random;

public final class CardUtils {

    // Constructor privado para prevenir la instanciación
    private CardUtils() {}

    public static String getCardNumber() {
        // Generar un número de tarjeta de 16 dígitos
        StringBuilder cardNumber = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            cardNumber.append(random.nextInt(10));
        }
        return cardNumber.toString();
    }

    public static int getCVV() {
        // Generar un CVV de 3 dígitos
        Random random = new Random();
        return 100 + random.nextInt(900); // CVV estará entre 100 y 999
    }
}


