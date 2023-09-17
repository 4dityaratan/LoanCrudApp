package com.example.SimplestCRUDExample.controller;

import com.example.SimplestCRUDExample.model.Loan;
import com.example.SimplestCRUDExample.repo.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/loans")
public class LoanController {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    EntityManager entityManager;

    private final Logger logger = Logger.getLogger(LoanController.class.getName());


    @GetMapping
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
        Loan loan = loanRepository.findById(id).orElse(null);
        if (loan == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(loan, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Loan> createLoan(@RequestBody Loan loan) {
        if (loan.getPaymentDate().after(loan.getDueDate())) {
            logger.log(Level.SEVERE, "Loan rejected: Payment date is greater than due date.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        loanRepository.save(loan);
        return new ResponseEntity<>(loan, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Loan> updateLoan(@PathVariable Long id, @RequestBody Loan updatedLoan) {
        Loan existingLoan = loanRepository.findById(id).orElse(null);
        if (existingLoan == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        existingLoan.setCustomerId(updatedLoan.getCustomerId());
        existingLoan.setAmount(updatedLoan.getAmount());
        existingLoan.setRemainingAmount(updatedLoan.getRemainingAmount());
        existingLoan.setPaymentDate(updatedLoan.getPaymentDate());
        existingLoan.setInterestPerDay(updatedLoan.getInterestPerDay());
        existingLoan.setDueDate(updatedLoan.getDueDate());
        existingLoan.setPenaltyPerDay(updatedLoan.getPenaltyPerDay());
        existingLoan.setCanceled(updatedLoan.isCanceled());

        if (existingLoan.getPaymentDate().after(existingLoan.getDueDate())) {
            logger.log(Level.SEVERE, "Loan rejected: Payment date is greater than due date.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        loanRepository.save(existingLoan);
        return new ResponseEntity<>(existingLoan, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        Loan loan = loanRepository.findById(id).orElse(null);
        if (loan == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        loanRepository.delete(loan);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/aggregateByLender")
    public ResponseEntity<?> aggregateByLender() {
        try {
            String sql = "SELECT lender_id, SUM(remaining_amount) AS total_remaining_amount FROM loan GROUP BY lender_id";
            Query query = entityManager.createNativeQuery(sql);

            List<Object[]> results = query.getResultList();

            List<Map<String, Object>> response = new ArrayList<>();
            for (Object[] row : results) {
                Map<String, Object> entry = new HashMap<>();
                entry.put("lender_id", row[0]);
                entry.put("total_remaining_amount", row[1]);
                response.add(entry);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/aggregateByInterest")
    public ResponseEntity<?> aggregateByInterest() {
        try {
            String sql = "SELECT interest_per_day, SUM(amount * interest_per_day) AS total_interest FROM loan GROUP BY interest_per_day";
            Query query = entityManager.createNativeQuery(sql);

            List<Object[]> results = query.getResultList();

            List<Map<String, Object>> response = new ArrayList<>();
            for (Object[] row : results) {
                Map<String, Object> entry = new HashMap<>();
                entry.put("interest_per_day", row[0]);
                entry.put("total_interest", row[1]);
                response.add(entry);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/aggregateByCustomerId")
    public ResponseEntity<?> aggregateByCustomerId() {
        try {
            String sql = "SELECT customer_id, SUM(remaining_amount) AS total_remaining_amount FROM loan GROUP BY customer_id";
            Query query = entityManager.createNativeQuery(sql);

            List<Object[]> results = query.getResultList();

            List<Map<String, Object>> response = new ArrayList<>();
            for (Object[] row : results) {
                Map<String, Object> entry = new HashMap<>();
                entry.put("customer_id", row[0]);
                entry.put("total_remaining_amount", row[1]);
                response.add(entry);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Scheduled(fixedRate = 24 * 60 * 60 * 1000) // Run every day
    public void checkDueDateAlerts() {
        List<Loan> loans = loanRepository.findAll();
        for (Loan loan : loans) {
            if (loan.getPaymentDate().after(loan.getDueDate())) {
                logger.log(Level.WARNING, "Loan crossed due date: Loan ID " + loan.getLoanId());
            }
        }
    }


}
