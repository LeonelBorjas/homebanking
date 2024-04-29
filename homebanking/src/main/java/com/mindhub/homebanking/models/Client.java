package com.mindhub.homebanking.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity //Con esto quiero generar una tabla
public class Client {
    @Id //Indicamos cual va hacer la clave primaria en la tabla de la base de datos
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Con esto generamos el valor
    private Long id;

    private String firstName;
    private String lastName;
    private String email;

    public Client() { }

    public Client(String first, String last, String emaill) {
        firstName = first;
        lastName = last;
        email = emaill;
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

    public String toString() {
        return firstName + " " + lastName + ", " + email;
    }
}
