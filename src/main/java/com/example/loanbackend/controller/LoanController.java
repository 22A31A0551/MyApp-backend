package com.example.loanbackend.controller;

import com.example.loanbackend.service.EmailService;
import com.example.loanbackend.model.Loan;
import com.example.loanbackend.repository.LoanRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private LoanRepository loanRepository;

    // ✅ 1. CREATE LOAN
    @PostMapping
    public Loan saveLoan(@RequestBody Loan loan) {

        loan.setStatus("Active"); // default status

        Loan savedLoan = loanRepository.save(loan);

        // 📧 Send email
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

    // ✅ 2. GET ALL LOANS
    @GetMapping
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    // ✅ 3. SEARCH ACTIVE LOANS
    @GetMapping("/search")
    public List<Loan> searchLoans(@RequestParam String name) {
        return loanRepository.findByNameContainingAndStatus(name, "Active");
    }

    // ✅ 4. EDIT LOAN (FINAL FIXED)
    @PutMapping("/edit/{id}")
    public Loan editLoan(@PathVariable Long id, @RequestBody Loan updatedLoan) {

        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        // update only required fields
        loan.setName(updatedLoan.getName());
        loan.setPhone(updatedLoan.getPhone());
        loan.setAmount(updatedLoan.getAmount());
        loan.setItem(updatedLoan.getItem());

        // 🔥 DO NOT TOUCH OTHER FIELDS
        // (this avoids null errors)

        return loanRepository.save(loan);
    }

    // ✅ 5. CLOSE LOAN
    @PutMapping("/close/{id}")
    public Loan closeLoan(@PathVariable Long id, @RequestBody Loan updatedLoan) {

        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        loan.setStatus("Closed");
        loan.setRepaymentDate(updatedLoan.getRepaymentDate());
        loan.setAmountPaid(updatedLoan.getAmountPaid());

        return loanRepository.save(loan);
    }

    // ✅ 6. DELETE LOAN
    @DeleteMapping("/{id}")
    public String deleteLoan(@PathVariable Long id) {
        loanRepository.deleteById(id);
        return "Loan deleted successfully";
    }
}