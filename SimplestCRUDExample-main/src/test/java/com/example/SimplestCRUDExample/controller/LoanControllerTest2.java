package com.example.SimplestCRUDExample.controller;

import com.example.SimplestCRUDExample.model.Loan;
import com.example.SimplestCRUDExample.repo.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import javax.persistence.EntityManager;
import java.util.*;
import javax.persistence.Query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class LoanControllerTest2 {

    @InjectMocks
    private LoanController loanController;

    @Mock
    private LoanRepository loanRepository;

    private MockMvc mockMvc;




    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        MockitoAnnotations.openMocks(this);
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loanController).build();
    }

    @Test
    public void testGetAllLoans() {
        Loan loan1 = new Loan();
        Loan loan2 = new Loan();
        List<Loan> loanList = new ArrayList<>();
        loanList.add(loan1);
        loanList.add(loan2);

        when(loanRepository.findAll()).thenReturn(loanList);

        List<Loan> result = loanController.getAllLoans();

        assertEquals(2, result.size());
        verify(loanRepository, times(1)).findAll();
    }

    @Test
    public void testGetLoanById() {
        Long loanId = 1L;
        Loan loan = new Loan();
        loan.setLoanId(String.valueOf(loanId));

        when(loanRepository.findById(loanId)).thenReturn(java.util.Optional.of(loan));

        ResponseEntity<Loan> responseEntity = loanController.getLoanById(loanId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(loanId.toString(), responseEntity.getBody().getLoanId());
    }

    @Test
    public void testGetLoanById_NotFound() {
        Long loanId = 1L;

        when(loanRepository.findById(loanId)).thenReturn(java.util.Optional.empty());

        ResponseEntity<Loan> responseEntity = loanController.getLoanById(loanId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testCreateLoan_ValidLoan() {
        Loan loan = new Loan();
        loan.setPaymentDate(new Date());
        loan.setDueDate(new Date(System.currentTimeMillis() + 10000));

        ResponseEntity<Loan> responseEntity = loanController.createLoan(loan);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(loanRepository, times(1)).save(loan);
    }

    @Test
    public void testCreateLoan_InvalidLoan() {
        Loan loan = new Loan();
        loan.setPaymentDate(new Date(System.currentTimeMillis() + 10000));
        loan.setDueDate(new Date());

        ResponseEntity<Loan> responseEntity = loanController.createLoan(loan);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        System.out.println("Log Message: " + responseEntity.getBody());
    }

    @Test
    public void testUpdateLoan_ValidLoan() {
        Long loanId = 1L;
        Loan existingLoan = new Loan();
        existingLoan.setLoanId(String.valueOf(loanId));

        Loan updatedLoan = new Loan();
        updatedLoan.setCustomerId("new_customer_id");
        updatedLoan.setAmount(1000.0);
        updatedLoan.setRemainingAmount(800.0);
        updatedLoan.setPaymentDate(new Date());
        updatedLoan.setInterestPerDay(0.05);
        updatedLoan.setDueDate(new Date(System.currentTimeMillis() + 10000));
        updatedLoan.setPenaltyPerDay(0.01);
        updatedLoan.setCanceled(true);

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(existingLoan));

        ResponseEntity<Loan> responseEntity = loanController.updateLoan(loanId, updatedLoan);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(loanRepository, times(1)).save(existingLoan);

        assertEquals(updatedLoan.getCustomerId(), existingLoan.getCustomerId());
        assertEquals(updatedLoan.getAmount(), existingLoan.getAmount());
        assertEquals(updatedLoan.getRemainingAmount(), existingLoan.getRemainingAmount());
        assertEquals(updatedLoan.getPaymentDate(), existingLoan.getPaymentDate());
        assertEquals(updatedLoan.getInterestPerDay(), existingLoan.getInterestPerDay());
        assertEquals(updatedLoan.getDueDate(), existingLoan.getDueDate());
        assertEquals(updatedLoan.getPenaltyPerDay(), existingLoan.getPenaltyPerDay());
        assertEquals(updatedLoan.isCanceled(), existingLoan.isCanceled());
    }

@Test
    public void testDeleteLoan() {
        Long loanId = 1L;
        Loan loan = new Loan();
        loan.setLoanId(String.valueOf(loanId));

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));

        ResponseEntity<Void> responseEntity = loanController.deleteLoan(loanId);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(loanRepository, times(1)).delete(loan);
    }

    @Test
    public void testDeleteLoan_NotFound() {
        Long loanId = 1L;

        when(loanRepository.findById(loanId)).thenReturn(Optional.empty());

        ResponseEntity<Void> responseEntity = loanController.deleteLoan(loanId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(loanRepository, never()).delete(any());
    }


    @Test
    public void testAggregateByLenderInvalidEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/invalidEndpoint")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testAggregateByLender() {
        EntityManager entityManager = mock(EntityManager.class);
        Query query = mock(Query.class);

        List<Object[]> results = new ArrayList<>();
        results.add(new Object[] { 1L, 1000.0 });
        results.add(new Object[] { 2L, 2000.0 });

        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(results);

        LoanController loanController = new LoanController();
        loanController.entityManager = entityManager;

        ResponseEntity<?> responseEntity = loanController.aggregateByLender();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());

        List<Map<String, Object>> responseBody = (List<Map<String, Object>>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());

        Map<String, Object> entry1 = responseBody.get(0);
        assertEquals(1L, entry1.get("lender_id"));
        assertEquals(1000.0, entry1.get("total_remaining_amount"));

        Map<String, Object> entry2 = responseBody.get(1);
        assertEquals(2L, entry2.get("lender_id"));
        assertEquals(2000.0, entry2.get("total_remaining_amount"));
    }

    @Test
    public void testAggregateByInterest() {
        EntityManager entityManager = mock(EntityManager.class);
        Query query = mock(Query.class);

        List<Object[]> results = new ArrayList<>();
        results.add(new Object[] { 0.05, 1000.0 });
        results.add(new Object[] { 0.03, 2000.0 });

        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(results);

        LoanController loanController = new LoanController();
        loanController.entityManager = entityManager;

        ResponseEntity<?> responseEntity = loanController.aggregateByInterest();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        List<Map<String, Object>> responseBody = (List<Map<String, Object>>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());

        Map<String, Object> entry1 = responseBody.get(0);
        assertEquals(0.05, entry1.get("interest_per_day"));
        assertEquals(1000.0, entry1.get("total_interest"));

        Map<String, Object> entry2 = responseBody.get(1);
        assertEquals(0.03, entry2.get("interest_per_day"));
        assertEquals(2000.0, entry2.get("total_interest"));
    }


    @Test
    public void testAggregateByCustomerId() {
        EntityManager entityManager = mock(EntityManager.class);
        Query query = mock(Query.class);

        List<Object[]> results = new ArrayList<>();
        results.add(new Object[] { 1L, 1000.0 });
        results.add(new Object[] { 2L, 2000.0 });

        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(results);

        LoanController loanController = new LoanController();
        loanController.entityManager = entityManager;

        ResponseEntity<?> responseEntity = loanController.aggregateByCustomerId();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        List<Map<String, Object>> responseBody = (List<Map<String, Object>>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());

        Map<String, Object> entry1 = responseBody.get(0);
        assertEquals(1L, entry1.get("customer_id"));
        assertEquals(1000.0, entry1.get("total_remaining_amount"));

        Map<String, Object> entry2 = responseBody.get(1);
        assertEquals(2L, entry2.get("customer_id"));
        assertEquals(2000.0, entry2.get("total_remaining_amount"));
    }

}
