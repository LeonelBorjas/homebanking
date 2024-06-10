package com.mindhub.homebanking.Utils;

import java.util.Random;

public class NumberAccount {
    public static String eightDigits() {
        Random random = new Random();
        int number = random.nextInt(100000000); // Genera un número entre 0 y 99999999
        return String.format("%08d", number);  // Completa con ceros a la izquierda si es necesario
    }
}
