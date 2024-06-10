package com.mindhub.homebanking.Utils;

public class NumberAccount {
    public static String generateRandomCardNumber() {
        return getCardNumber();
    }

    public static String getCardNumber() {
        StringBuilder cardNumber = new StringBuilder(); //cadenas de caracteres de manera eficiente
        for (int i = 0; i < 4; i++) { //genera 4 grupos de 4 digitos
            int section = (int) (Math.random() * 9000 + 1000); //numero randon entre 1000 y 9999
            cardNumber.append(section).append("-"); //agrega el grupo de 4 digitos y un guion
        }
        return cardNumber.substring(0, cardNumber.length() - 1);
    }


}
