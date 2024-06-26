package com.mindhub.homebanking.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;

    private LocalDate creationDate;

    private double balance;

    @ManyToOne(fetch = FetchType.EAGER) //Muchas cuentas pueden pertenecer a un client, se recupera de forma automatica cuando acceda a la cuenta
    @JoinColumn(name="owner_id")
    private Client client;

    @OneToMany(mappedBy="account", fetch=FetchType.EAGER) // Muchas transacciones pueden estar asociadas a un cuenta
    private Set<Transaction> transactions = new HashSet<>();

    public Account(String numberAccount, LocalDate date, double balanceAccount) {
        number = numberAccount;
        creationDate = date;
        balance = balanceAccount;
    }

    public Account() {
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction transaction){
        transaction.setAccount(this); //this hace referencia al objeto que llama al metodo addTransaction
        transactions.add(transaction); // donde se guarda
    }

    public String toString(){ //Sobre escribe los datos que tiene y no los datos en donde esta
        return  id + " " + number + " " + creationDate + " " + balance;
    }

}

