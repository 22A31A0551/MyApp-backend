package com.example.loanbackend.controller;
import com.example.loanbackend.service.EmailService;
import com.example.loanbackend.model.Loan;
import com.example.loanbackend.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/loans")
public class LoanController {
    @Autowired
    private EmailService emailService;
    @Autowired
    private LoanRepository loanRepository;

    // SAVE DATA
    @PostMapping
    public Loan saveLoan(@RequestBody Loan loan) {

        Loan savedLoan = loanRepository.save(loan);

        if (loan.getEmail() != null && !loan.getEmail().isEmpty()) {
            emailService.sendEmail(
                    loan.getEmail(),
                    loan.getName(),
                    loan.getAmount(),
                    loan.getDate()
            );
        }

        return savedLoan;
    }

    // GET ALL DATA
    @GetMapping
    public java.util.List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }
}