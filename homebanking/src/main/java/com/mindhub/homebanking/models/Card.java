//package com.mindhub.homebanking.models;
//
//import jakarta.persistence.*;
//
//import java.time.LocalDate;
//
//@Entity
//public class Card {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String number;
//
//    private int securityCode;
//
//    private LocalDate from;
//
//    private LocalDate until;
//
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    private Client client;
//
//    public Card(String number, int securityCode, LocalDate until, LocalDate from) {
//        this.number = number;
//        this.securityCode = securityCode;
//        this.until = until;
//        this.from = from;
//    }
//
//    public Card() {
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getNumber() {
//        return number;
//    }
//
//    public void setNumber(String number) {
//        this.number = number;
//    }
//
//    public int getSecurityCode() {
//        return securityCode;
//    }
//
//    public void setSecurityCode(int securityCode) {
//        this.securityCode = securityCode;
//    }
//
//    public LocalDate getFrom() {
//        return from;
//    }
//
//    public void setFrom(LocalDate from) {
//        this.from = from;
//    }
//
//    public LocalDate getUntil() {
//        return until;
//    }
//
//    public void setUntil(LocalDate until) {
//        this.until = until;
//    }
//
//    public Client getClient() {
//        return client;
//    }
//
//
//    public void setClient(Client client) {
//        this.client = client;
//    }
//
//
//
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
//}
