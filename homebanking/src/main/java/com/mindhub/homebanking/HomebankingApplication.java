package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.fasterxml.jackson.databind.cfg.CoercionInputShape.Array;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository){
		return (args) ->{
			Client clientOne = new Client("Melba", "Morel", "melba@mindhub.com");
			Client clientTwo = new Client("Leonel", "Borjas", "leonelborjas@outlook.es");

			LocalDate localDateToday = LocalDate.now();
			LocalDate localDateTomorrow = localDateToday.plusDays(1);

			Account accountOne = new Account("VIN001", localDateToday, 5000);
			Account accountTwo = new Account("VIN002", localDateTomorrow, 7500);
			Account accountThree = new Account("VIN003",localDateToday, 40000);

			clientOne.addAccount(accountOne);
			clientOne.addAccount(accountTwo);
			clientTwo.addAccount(accountThree);

			LocalDateTime localDateTimeNow = LocalDateTime.now();
			Transaction transactionOne = new Transaction(TransactionType.DEBIT, 2000, localDateTimeNow, "Steam");
			Transaction transactionTwo = new Transaction(TransactionType.CREDIT, 500, localDateTimeNow, "Perls");
			Transaction transactionThree = new Transaction(TransactionType.CREDIT, 400, localDateTimeNow, "Skin of black desert");
			Transaction transactionFort = new Transaction(TransactionType.DEBIT, 3000, localDateTimeNow, "Pedidos Ya");
			Transaction transactionFive = new Transaction(TransactionType.CREDIT, 1400, localDateTimeNow, "Rappi");
			Transaction transactionSix = new Transaction(TransactionType.CREDIT, 500, localDateTimeNow, "Eggs");
			Transaction transactionSeven = new Transaction(TransactionType.CREDIT, 4000, localDateTimeNow, "Books");
			Transaction transactionEight = new Transaction(TransactionType.DEBIT, 2000, localDateTimeNow, "PetFood");
			Transaction transactionNine = new Transaction(TransactionType.CREDIT, 20000, localDateTimeNow, "VideoGames");

			accountOne.addTransaction(transactionOne);
			accountOne.addTransaction(transactionTwo);
			accountOne.addTransaction(transactionThree);
			accountTwo.addTransaction(transactionFort);
			accountTwo.addTransaction(transactionFive);
			accountTwo.addTransaction(transactionSix);
			accountThree.addTransaction(transactionSeven);
			accountThree.addTransaction(transactionEight);
			accountThree.addTransaction(transactionNine);


			List<Integer> mortgageInstallments = Arrays.asList(12, 24, 36, 48, 60);
			List<Integer> personalInstallments = Arrays.asList(6, 12, 24);
			List<Integer> automotiveInstallments = Arrays.asList(6, 12, 24, 36);

			Loan mortgageLoan = new Loan("Mortgage Loan", 5000000, mortgageInstallments);
			Loan personalLoan = new Loan("Personal Loan",100000, personalInstallments);
			Loan automotiveLoan = new Loan("Automotive Loan", 300000, automotiveInstallments);

			ClientLoan clientLoanOne = new ClientLoan(60, 4000000);
			ClientLoan clientLoanTwo = new ClientLoan(12, 50000);
			ClientLoan clientLoanTree = new ClientLoan(24, 1000000);
			ClientLoan clientLoanFort = new ClientLoan(36, 200000);

			mortgageLoan.addClientLoan(clientLoanOne);
			personalLoan.addClientLoan(clientLoanTwo);
			personalLoan.addClientLoan(clientLoanTree);
			automotiveLoan.addClientLoan(clientLoanFort);

			clientOne.addClientLoan(clientLoanOne);
			clientOne.addClientLoan(clientLoanTwo);
			clientTwo.addClientLoan(clientLoanTree);
			clientTwo.addClientLoan(clientLoanFort);

			clientRepository.save(clientOne);
			clientRepository.save(clientTwo);

			accountRepository.save(accountOne);
			accountRepository.save(accountTwo);
			accountRepository.save(accountThree);

			transactionRepository.save(transactionOne);
			transactionRepository.save(transactionTwo);
			transactionRepository.save(transactionThree);
			transactionRepository.save(transactionFort);
			transactionRepository.save(transactionFive);
			transactionRepository.save(transactionSix);
			transactionRepository.save(transactionSeven);
			transactionRepository.save(transactionEight);
			transactionRepository.save(transactionNine);

			loanRepository.save(mortgageLoan);
			loanRepository.save(personalLoan);
			loanRepository.save(automotiveLoan);

			clientLoanRepository.save(clientLoanOne);
			clientLoanRepository.save(clientLoanTwo);
			clientLoanRepository.save(clientLoanTree);
			clientLoanRepository.save(clientLoanFort);

			mortgageLoan.getClients();
			System.out.println(mortgageLoan.getClients());

			clientTwo.getLoans();
			System.out.println(clientTwo.getLoans());


		};
	}

}
