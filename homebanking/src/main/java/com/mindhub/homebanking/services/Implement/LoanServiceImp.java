package com.mindhub.homebanking.services.Implement;

import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoanServiceImp implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Override
    public List<Loan> getLoans() {
        return loanRepository.findAll();
    }

    @Override
    public List<LoanDTO> getLoansDTO(){
        return getLoans().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return loanRepository.existsById(id);
    }

    @Override
    public Optional<Loan> findById(Long id) {
        return loanRepository.findById(id);
    }

}
