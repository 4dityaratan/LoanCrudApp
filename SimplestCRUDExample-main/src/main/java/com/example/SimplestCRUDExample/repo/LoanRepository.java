package com.example.SimplestCRUDExample.repo;

import com.example.SimplestCRUDExample.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

}
