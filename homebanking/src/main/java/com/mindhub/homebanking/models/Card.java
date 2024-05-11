package com.mindhub.homebanking.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardName;

    private String number;

    private int securityCode;

    private LocalDate since;

    private LocalDate until;

    private CardType cardType;

    private CardColor cardColor;


    @ManyToOne(fetch = FetchType.EAGER)
    private Client client;

    public Card(Client client, String number, int securityCode, LocalDate since, LocalDate until, CardType cardType, CardColor cardColor) {
        this.cardName = client.getFirstName() + " " + client.getLastName();
        this.number = number;
        this.securityCode = securityCode;
        this.since = since;
        this.until = until;
        this.cardType = cardType;
        this.cardColor = cardColor;
    }

    public Card() {
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public void setCardColor(CardColor cardColor) {
        this.cardColor = cardColor;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public LocalDate getSince() {
        return since;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(int securityCode) {
        this.securityCode = securityCode;
    }

    public LocalDate getFrom() {
        return since;
    }

    public void setSince(LocalDate since) {
        this.since = since;
    }

    public LocalDate getUntil() {
        return until;
    }

    public void setUntil(LocalDate until) {
        this.until = until;
    }

    public Client getClient() {
        return client;
    }


    public void setClient(Client client) {
        this.client = client;
    }


//    @Override
//    public String toString() {
//        return "Card{" +
//                "id=" + id +
//                ", number='" + number + '\'' +
//                ", securityCode=" + securityCode +
//                ", from=" + from +
//                ", until=" + until +
//                ", client=" + client +
//                '}';
//    }
}
