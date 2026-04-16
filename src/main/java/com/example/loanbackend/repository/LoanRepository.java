package com.example.loanbackend.repository;

import com.example.loanbackend.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    // 🔍 Partial search (BEST - use this)
    List<Loan> findByNameContaining(String name);

    // 🔍 Partial search with status (used in your app)
    List<Loan> findByNameContainingAndStatus(String name, String status);

    // 🔍 Exact match (optional - rarely needed)
    List<Loan> findByName(String name);
}