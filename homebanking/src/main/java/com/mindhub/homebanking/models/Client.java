package com.mindhub.homebanking.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity //Con esto quiero generar una tabla
public class Client {
    @Id //Indicamos cual va hacer la clave primaria en la tabla de la base de datos
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Con esto generamos el valor
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();


    public Client(String first, String last, String emaill) {
        firstName = first;
        lastName = last;
        email = emaill;
    }

    public Client() {
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addAccount (Account account){
        account.setClient(this);
        accounts.add(account);
    }

    public String toString() {
        return id + " " + firstName + " " + lastName + ", " + email;
    }
}
