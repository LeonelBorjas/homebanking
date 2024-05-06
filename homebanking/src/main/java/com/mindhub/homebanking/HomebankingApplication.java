package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository){
		return (args) ->{
			Client clientOne = new Client("Melba", "Morel", "melba@mindhub.com");
			clientRepository.save(clientOne);

			Client clientTwo = new Client("Leonel", "Borjas", "leonelborjas@outlook.es");
			clientRepository.save(clientTwo);

			LocalDate localDateToday = LocalDate.now();
			LocalDate localDateTomorrow = localDateToday.plusDays(1);

			Account accountOne = new Account("VIN001", localDateToday, 5000);
			Account accountTwo = new Account("VIN002", localDateTomorrow, 7500);
			Account accountThree = new Account("VIN003",localDateToday, 40000);

			clientOne.addAccount(accountOne);
			clientOne.addAccount(accountTwo);
			clientTwo.addAccount(accountThree);
			accountRepository.save(accountOne);
			accountRepository.save(accountTwo);
			accountRepository.save(accountThree);
		};
	}

}
