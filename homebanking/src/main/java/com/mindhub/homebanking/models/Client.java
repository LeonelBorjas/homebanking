package com.mindhub.homebanking.models;
import io.jsonwebtoken.security.Password;
import jakarta.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity //Con esto quiero generar una tabla
public class Client {

    @Id //Indicamos cual va hacer la clave primaria en la tabla de la base de datos
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Con esto generamos el valor automaticamente
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String Password;

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "client")
    private List<ClientLoan> clientLoans = new ArrayList<>();

    @OneToMany(mappedBy = "client")
    private Set<Card> cards = new HashSet<>();


    public Client(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        Password = password;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
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

    public List<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setClientLoans(List<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }

    public void addAccount (Account account){ //objeto tipo account
        account.setClient(this);  // set asignar un cliente
        this.accounts.add(account);
    }

    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }

    public void addCard (Card card){
        card.setClient(this);
        cards.add(card);
    }

    public List<Loan> getLoans() {
        return clientLoans.stream()
                .map(clientsLoan -> clientsLoan.getLoan())
                .toList();
    }

    public String toString() {   // hacen que sea visible los datos donde estan guardados los objetos
        return id + " " + firstName + " " + lastName + ", " + email;
    }

}
