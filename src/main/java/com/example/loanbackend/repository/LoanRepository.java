package com.example.loanbackend.repository;

import com.example.loanbackend.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}