package com.example.loanbackend.controller;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import com.example.loanbackend.service.EmailService;
import com.example.loanbackend.model.Loan;
import com.example.loanbackend.repository.LoanRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private LoanRepository loanRepository;

    // ✅ CREATE LOAN
    @PostMapping
    public Loan saveLoan(@RequestBody Loan loan) {

        loan.setStatus("Active");

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

    // ✅ GET ALL LOANS
    @GetMapping
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    // ✅ SEARCH LOANS
    @GetMapping("/search")
    public List<Loan> searchLoans(@RequestParam String name) {
        return loanRepository.findByNameContainingAndStatus(name, "Active");
    }

    // ✅ EDIT LOAN
    @PutMapping("/edit/{id}")
    public Loan editLoan(@PathVariable Long id, @RequestBody Loan updatedLoan) {

        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        loan.setName(updatedLoan.getName());
        loan.setPhone(updatedLoan.getPhone());
        loan.setAmount(updatedLoan.getAmount());
        loan.setItem(updatedLoan.getItem());

        return loanRepository.save(loan);
    }

    // ✅ CLOSE LOAN
    @PutMapping("/close/{id}")
    public Loan closeLoan(@PathVariable Long id, @RequestBody Loan updatedLoan) {

        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        loan.setStatus("Closed");
        loan.setRepaymentDate(updatedLoan.getRepaymentDate());
        loan.setAmountPaid(updatedLoan.getAmountPaid());

        return loanRepository.save(loan);
    }

    // ✅ DELETE LOAN
    @DeleteMapping("/{id}")
    public String deleteLoan(@PathVariable Long id) {
        loanRepository.deleteById(id);
        return "Loan deleted successfully";
    }

    // ✅ EXPIRING LOANS
    @GetMapping("/expiring")
    public List<Loan> getExpiringLoans() {

        List<Loan> allLoans = loanRepository.findAll();
        List<Loan> expiringLoans = new ArrayList<>();

        LocalDate today = LocalDate.now();

        for (Loan loan : allLoans) {

            if (loan.getDate() == null || loan.getStatus() == null) continue;

            LocalDate loanDate = LocalDate.parse(loan.getDate().substring(0, 10));
            LocalDate expiryDate = loanDate.plusMonths(11);

            if (!today.isBefore(expiryDate) && "Active".equalsIgnoreCase(loan.getStatus())) {
                expiringLoans.add(loan);
            }
        }

        return expiringLoans;
    }

    // ✅ SEND REMINDER (CLEAN VERSION)
    @GetMapping("/send-reminder/{id}")
    public ResponseEntity<String> sendReminder(@PathVariable Long id) {

        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getEmail() != null && !loan.getEmail().isEmpty()) {
            emailService.sendReminderEmail(
                    loan.getEmail(),
                    loan.getName(),
                    loan.getAmount()
            );
        }

        return ResponseEntity.ok("Email sent successfully");
    }
}