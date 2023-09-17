package com.example.SimplestCRUDExample.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.SimplestCRUDExample.model.Loan;
import com.example.SimplestCRUDExample.repo.LoanRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {LoanController.class})
@ExtendWith(SpringExtension.class)
class LoanControllerTest {

    @MockBean
    private EntityManager entityManager;

    @Autowired
    private LoanController loanController;

    @MockBean
    private LoanRepository loanRepository;

    @Test
    void testAggregateByLender2() {

        ResponseEntity<?> actualAggregateByLenderResult = (new LoanController()).aggregateByLender();
        assertEquals("An error occurred", actualAggregateByLenderResult.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualAggregateByLenderResult.getStatusCode());
        assertTrue(actualAggregateByLenderResult.getHeaders().isEmpty());
    }


    @Test
    void testAggregateByInterest2() {

        ResponseEntity<?> actualAggregateByInterestResult = (new LoanController()).aggregateByInterest();
        assertEquals("An error occurred", actualAggregateByInterestResult.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualAggregateByInterestResult.getStatusCode());
        assertTrue(actualAggregateByInterestResult.getHeaders().isEmpty());
    }

    @Test
    void testAggregateByCustomerId2() {

        ResponseEntity<?> actualAggregateByCustomerIdResult = (new LoanController()).aggregateByCustomerId();
        assertEquals("An error occurred", actualAggregateByCustomerIdResult.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualAggregateByCustomerIdResult.getStatusCode());
        assertTrue(actualAggregateByCustomerIdResult.getHeaders().isEmpty());
    }

    @Test
    void testCheckDueDateAlerts() {
        when(loanRepository.findAll()).thenReturn(new ArrayList<>());
        loanController.checkDueDateAlerts();
        verify(loanRepository).findAll();
        assertTrue(loanController.getAllLoans().isEmpty());
    }

    @Test
    void testCheckDueDateAlerts2() {
        Loan loan = new Loan();
        loan.setAmount(10.0d);
        loan.setCanceled(true);
        loan.setCustomerId("42");
        loan.setDueDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        loan.setId(1L);
        loan.setInterestPerDay(10.0d);
        loan.setLenderId("42");
        loan.setLoanId("42");
        loan.setPaymentDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        loan.setPenaltyPerDay(10.0d);
        loan.setRemainingAmount(10.0d);

        ArrayList<Loan> loanList = new ArrayList<>();
        loanList.add(loan);
        when(loanRepository.findAll()).thenReturn(loanList);
        loanController.checkDueDateAlerts();
        verify(loanRepository).findAll();
        assertEquals(1, loanController.getAllLoans().size());
    }


    @Test
    void testCheckDueDateAlerts3() {
        Loan loan = mock(Loan.class);
        when(loan.getLoanId()).thenReturn("42");
        when(loan.getDueDate())
                .thenReturn(Date.from(LocalDate.ofYearDay(1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        when(loan.getPaymentDate())
                .thenReturn(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        doNothing().when(loan).setAmount(anyDouble());
        doNothing().when(loan).setCanceled(anyBoolean());
        doNothing().when(loan).setCustomerId(Mockito.<String>any());
        doNothing().when(loan).setDueDate(Mockito.<Date>any());
        doNothing().when(loan).setId(Mockito.<Long>any());
        doNothing().when(loan).setInterestPerDay(anyDouble());
        doNothing().when(loan).setLenderId(Mockito.<String>any());
        doNothing().when(loan).setLoanId(Mockito.<String>any());
        doNothing().when(loan).setPaymentDate(Mockito.<Date>any());
        doNothing().when(loan).setPenaltyPerDay(anyDouble());
        doNothing().when(loan).setRemainingAmount(anyDouble());
        loan.setAmount(10.0d);
        loan.setCanceled(true);
        loan.setCustomerId("42");
        loan.setDueDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        loan.setId(1L);
        loan.setInterestPerDay(10.0d);
        loan.setLenderId("42");
        loan.setLoanId("42");
        loan.setPaymentDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        loan.setPenaltyPerDay(10.0d);
        loan.setRemainingAmount(10.0d);

        ArrayList<Loan> loanList = new ArrayList<>();
        loanList.add(loan);
        when(loanRepository.findAll()).thenReturn(loanList);
        loanController.checkDueDateAlerts();
        verify(loanRepository).findAll();
        verify(loan).getLoanId();
        verify(loan).getDueDate();
        verify(loan).getPaymentDate();
        verify(loan).setAmount(anyDouble());
        verify(loan).setCanceled(anyBoolean());
        verify(loan).setCustomerId(Mockito.<String>any());
        verify(loan).setDueDate(Mockito.<Date>any());
        verify(loan).setId(Mockito.<Long>any());
        verify(loan).setInterestPerDay(anyDouble());
        verify(loan).setLenderId(Mockito.<String>any());
        verify(loan).setLoanId(Mockito.<String>any());
        verify(loan).setPaymentDate(Mockito.<Date>any());
        verify(loan).setPenaltyPerDay(anyDouble());
        verify(loan).setRemainingAmount(anyDouble());
    }
//
//    /**
//     * Method under test: {@link LoanController#checkDueDateAlerts()}
//     */
//    @Test
//    @Disabled("TODO: Complete this test")
//    void testCheckDueDateAlerts4() {
//        // TODO: Complete this test.
//        //   Reason: R013 No inputs found that don't throw a trivial exception.
//        //   Diffblue Cover tried to run the arrange/act section, but the method under
//        //   test threw
//        //   java.lang.NullPointerException: Cannot invoke "Object.getClass()" because "date" is null
//        //       at java.base/java.util.Date.getMillisOf(Date.java:957)
//        //       at java.base/java.util.Date.after(Date.java:930)
//        //       at com.example.SimplestCRUDExample.controller.LoanController.checkDueDateAlerts(LoanController.java:170)
//        //   See https://diff.blue/R013 to resolve this issue.
//
//        Loan loan = mock(Loan.class);
//        when(loan.getLoanId()).thenReturn("42");
//        when(loan.getDueDate()).thenReturn(null);
//        when(loan.getPaymentDate())
//                .thenReturn(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//        doNothing().when(loan).setAmount(anyDouble());
//        doNothing().when(loan).setCanceled(anyBoolean());
//        doNothing().when(loan).setCustomerId(Mockito.<String>any());
//        doNothing().when(loan).setDueDate(Mockito.<Date>any());
//        doNothing().when(loan).setId(Mockito.<Long>any());
//        doNothing().when(loan).setInterestPerDay(anyDouble());
//        doNothing().when(loan).setLenderId(Mockito.<String>any());
//        doNothing().when(loan).setLoanId(Mockito.<String>any());
//        doNothing().when(loan).setPaymentDate(Mockito.<Date>any());
//        doNothing().when(loan).setPenaltyPerDay(anyDouble());
//        doNothing().when(loan).setRemainingAmount(anyDouble());
//        loan.setAmount(10.0d);
//        loan.setCanceled(true);
//        loan.setCustomerId("42");
//        loan.setDueDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//        loan.setId(1L);
//        loan.setInterestPerDay(10.0d);
//        loan.setLenderId("42");
//        loan.setLoanId("42");
//        loan.setPaymentDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//        loan.setPenaltyPerDay(10.0d);
//        loan.setRemainingAmount(10.0d);
//
//        ArrayList<Loan> loanList = new ArrayList<>();
//        loanList.add(loan);
//        when(loanRepository.findAll()).thenReturn(loanList);
//        loanController.checkDueDateAlerts();
//    }
//
//    /**
//     * Method under test: {@link LoanController#createLoan(Loan)}
//     */
//    @Test
//    void testCreateLoan() throws Exception {
//        when(loanRepository.findAll()).thenReturn(new ArrayList<>());
//
//        Loan loan = new Loan();
//        loan.setAmount(10.0d);
//        loan.setCanceled(true);
//        loan.setCustomerId("42");
//        loan.setDueDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//        loan.setId(1L);
//        loan.setInterestPerDay(10.0d);
//        loan.setLenderId("42");
//        loan.setLoanId("42");
//        loan.setPaymentDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//        loan.setPenaltyPerDay(10.0d);
//        loan.setRemainingAmount(10.0d);
//        String content = (new ObjectMapper()).writeValueAsString(loan);
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/loans")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content);
//        MockMvcBuilders.standaloneSetup(loanController)
//                .build()
//                .perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//                .andExpect(MockMvcResultMatchers.content().string("[]"));
//    }
//
//    /**
//     * Method under test: {@link LoanController#deleteLoan(Long)}
//     */
//    @Test
//    void testDeleteLoan() throws Exception {
//        Loan loan = new Loan();
//        loan.setAmount(10.0d);
//        loan.setCanceled(true);
//        loan.setCustomerId("42");
//        loan.setDueDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//        loan.setId(1L);
//        loan.setInterestPerDay(10.0d);
//        loan.setLenderId("42");
//        loan.setLoanId("42");
//        loan.setPaymentDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//        loan.setPenaltyPerDay(10.0d);
//        loan.setRemainingAmount(10.0d);
//        Optional<Loan> ofResult = Optional.of(loan);
//        doNothing().when(loanRepository).delete(Mockito.<Loan>any());
//        when(loanRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/loans/{id}", 1L);
//        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(loanController)
//                .build()
//                .perform(requestBuilder);
//        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNoContent());
//    }
//
//    /**
//     * Method under test: {@link LoanController#deleteLoan(Long)}
//     */
//    @Test
//    void testDeleteLoan2() throws Exception {
//        doNothing().when(loanRepository).delete(Mockito.<Loan>any());
//        Optional<Loan> emptyResult = Optional.empty();
//        when(loanRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/loans/{id}", 1L);
//        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(loanController)
//                .build()
//                .perform(requestBuilder);
//        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
//    }
//
//    /**
//     * Method under test: {@link LoanController#getAllLoans()}
//     */
//    @Test
//    void testGetAllLoans() throws Exception {
//        when(loanRepository.findAll()).thenReturn(new ArrayList<>());
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/loans");
//        MockMvcBuilders.standaloneSetup(loanController)
//                .build()
//                .perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//                .andExpect(MockMvcResultMatchers.content().string("[]"));
//    }
//
//    /**
//     * Method under test: {@link LoanController#getAllLoans()}
//     */
//    @Test
//    void testGetAllLoans2() throws Exception {
//        when(loanRepository.findAll()).thenReturn(new ArrayList<>());
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/loans");
//        requestBuilder.characterEncoding("Encoding");
//        MockMvcBuilders.standaloneSetup(loanController)
//                .build()
//                .perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//                .andExpect(MockMvcResultMatchers.content().string("[]"));
//    }
//
//    /**
//     * Method under test: {@link LoanController#getLoanById(Long)}
//     */
//    @Test
//    void testGetLoanById() throws Exception {
//        Loan loan = new Loan();
//        loan.setAmount(10.0d);
//        loan.setCanceled(true);
//        loan.setCustomerId("42");
//        loan.setDueDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//        loan.setId(1L);
//        loan.setInterestPerDay(10.0d);
//        loan.setLenderId("42");
//        loan.setLoanId("42");
//        loan.setPaymentDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
//        loan.setPenaltyPerDay(10.0d);
//        loan.setRemainingAmount(10.0d);
//        Optional<Loan> ofResult = Optional.of(loan);
//        when(loanRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/loans/{id}", 1L);
//        MockMvcBuilders.standaloneSetup(loanController)
//                .build()
//                .perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//                .andExpect(MockMvcResultMatchers.content()
//                        .string(
//                                "{\"id\":1,\"loanId\":\"42\",\"customerId\":\"42\",\"lenderId\":\"42\",\"amount\":10.0,\"remainingAmount\":10.0,\"paymentDate"
//                                        + "\":0,\"interestPerDay\":10.0,\"dueDate\":0,\"penaltyPerDay\":10.0,\"canceled\":true}"));
//    }

//    /**
//     * Method under test: {@link LoanController#getLoanById(Long)}
//     */
//    @Test
//    void testGetLoanById2() throws Exception {
//        Optional<Loan> emptyResult = Optional.empty();
//        when(loanRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/loans/{id}", 1L);
//        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(loanController)
//                .build()
//                .perform(requestBuilder);
//        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
//    }

    @Test
    void testUpdateLoan() throws Exception {
        Loan loan = new Loan();
        loan.setAmount(10.0d);
        loan.setCanceled(true);
        loan.setCustomerId("42");
        loan.setDueDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        loan.setId(1L);
        loan.setInterestPerDay(10.0d);
        loan.setLenderId("42");
        loan.setLoanId("42");
        loan.setPaymentDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        loan.setPenaltyPerDay(10.0d);
        loan.setRemainingAmount(10.0d);
        Optional<Loan> ofResult = Optional.of(loan);

        Loan loan2 = new Loan();
        loan2.setAmount(10.0d);
        loan2.setCanceled(true);
        loan2.setCustomerId("42");
        loan2.setDueDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        loan2.setId(1L);
        loan2.setInterestPerDay(10.0d);
        loan2.setLenderId("42");
        loan2.setLoanId("42");
        loan2.setPaymentDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        loan2.setPenaltyPerDay(10.0d);
        loan2.setRemainingAmount(10.0d);
        when(loanRepository.save(Mockito.<Loan>any())).thenReturn(loan2);
        when(loanRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Loan loan3 = new Loan();
        loan3.setAmount(10.0d);
        loan3.setCanceled(true);
        loan3.setCustomerId("42");
        loan3.setDueDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        loan3.setId(1L);
        loan3.setInterestPerDay(10.0d);
        loan3.setLenderId("42");
        loan3.setLoanId("42");
        loan3.setPaymentDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        loan3.setPenaltyPerDay(10.0d);
        loan3.setRemainingAmount(10.0d);
        String content = (new ObjectMapper()).writeValueAsString(loan3);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/loans/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(loanController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"loanId\":\"42\",\"customerId\":\"42\",\"lenderId\":\"42\",\"amount\":10.0,\"remainingAmount\":10.0,\"paymentDate"
                                        + "\":0,\"interestPerDay\":10.0,\"dueDate\":0,\"penaltyPerDay\":10.0,\"canceled\":true}"));
    }

    @Test
    void testUpdateLoan2() throws Exception {
        Loan loan = new Loan();
        loan.setAmount(10.0d);
        loan.setCanceled(true);
        loan.setCustomerId("42");
        loan.setDueDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        loan.setId(1L);
        loan.setInterestPerDay(10.0d);
        loan.setLenderId("42");
        loan.setLoanId("42");
        loan.setPaymentDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        loan.setPenaltyPerDay(10.0d);
        loan.setRemainingAmount(10.0d);
        when(loanRepository.save(Mockito.<Loan>any())).thenReturn(loan);
        Optional<Loan> emptyResult = Optional.empty();
        when(loanRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        Loan loan2 = new Loan();
        loan2.setAmount(10.0d);
        loan2.setCanceled(true);
        loan2.setCustomerId("42");
        loan2.setDueDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        loan2.setId(1L);
        loan2.setInterestPerDay(10.0d);
        loan2.setLenderId("42");
        loan2.setLoanId("42");
        loan2.setPaymentDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        loan2.setPenaltyPerDay(10.0d);
        loan2.setRemainingAmount(10.0d);
        String content = (new ObjectMapper()).writeValueAsString(loan2);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/loans/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(loanController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testUpdateLoan3() throws Exception {
        Loan loan = new Loan();
        loan.setAmount(10.0d);
        loan.setCanceled(true);
        loan.setCustomerId("42");
        loan.setDueDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        loan.setId(1L);
        loan.setInterestPerDay(10.0d);
        loan.setLenderId("42");
        loan.setLoanId("42");
        loan.setPaymentDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        loan.setPenaltyPerDay(10.0d);
        loan.setRemainingAmount(10.0d);
        Optional<Loan> ofResult = Optional.of(loan);

        Loan loan2 = new Loan();
        loan2.setAmount(10.0d);
        loan2.setCanceled(true);
        loan2.setCustomerId("42");
        loan2.setDueDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        loan2.setId(1L);
        loan2.setInterestPerDay(10.0d);
        loan2.setLenderId("42");
        loan2.setLoanId("42");
        loan2.setPaymentDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        loan2.setPenaltyPerDay(10.0d);
        loan2.setRemainingAmount(10.0d);
        when(loanRepository.save(Mockito.<Loan>any())).thenReturn(loan2);
        when(loanRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Loan loan3 = new Loan();
        loan3.setAmount(10.0d);
        loan3.setCanceled(true);
        loan3.setCustomerId("42");
        loan3.setDueDate(Date.from(LocalDate.ofYearDay(3, 3).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        loan3.setId(1L);
        loan3.setInterestPerDay(10.0d);
        loan3.setLenderId("42");
        loan3.setLoanId("42");
        loan3.setPaymentDate(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        loan3.setPenaltyPerDay(10.0d);
        loan3.setRemainingAmount(10.0d);
        String content = (new ObjectMapper()).writeValueAsString(loan3);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/loans/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(loanController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }
}

