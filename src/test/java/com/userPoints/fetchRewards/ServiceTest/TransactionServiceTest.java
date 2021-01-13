package com.userPoints.fetchRewards.ServiceTest;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.userPoints.fetchRewards.Controller.TransactionController;
import com.userPoints.fetchRewards.Entity.Transaction;
import com.userPoints.fetchRewards.Repository.TransactionRepository;
import com.userPoints.fetchRewards.Service.TransactionService;
import com.userPoints.fetchRewards.Service.TransactionServiceImpl;
import com.userPoints.fetchRewards.TransactionException.TransactionException;

@RunWith(SpringRunner.class)
@ContextConfiguration
public class TransactionServiceTest {
	
	@TestConfiguration
	public static class config {
		@Bean
		public TransactionService getTransactionService() {
			return new TransactionServiceImpl();
		}
	}
	
	@Autowired
	private TransactionService service;
	
	@MockBean
    private TransactionRepository repository;
	
	@Before
	public void setup() {
		List<Transaction> t = new ArrayList<>();
		
		List<String> transactionsgetBalance = new ArrayList<>();
		transactionsgetBalance.add(getTestTransaction().toString());
		
		Mockito.when(repository.getBalance()).thenReturn(transactionsgetBalance);
		
		Mockito.when(repository.addTransaction(transaction)).thenReturn(500);
		
		Mockito.when(repository.getSortedTransactions()).thenReturn(t); //should return sorted list of transactions
	}
	
	@After
    public void cleanup() {
    }
	
	Transaction transaction = new Transaction();
	
	public Transaction getTestTransaction() {
		transaction.setDate(TransactionController.convertDateFormat("12/5 5PM"));
		transaction.setId("3");
		transaction.setPayer("TestPayer");
		transaction.setPoints(500);
		
		return transaction;
	}
	
	@Test
	public void getPointsTest() {
		Assert.assertNotEquals("You do not have any points", service.getPoints());
	}
	
	@Test
	public void addingPointTest() {
		
		Transaction transaction = new Transaction();
		transaction.setDate(TransactionController.convertDateFormat("12/5 5PM"));
		transaction.setId("3");
		transaction.setPayer("TestPayer");
		transaction.setPoints(500);
		
		int points = service.addPoints(getTestTransaction());
		
		Assert.assertEquals(500, points);
	}
	
	@Test
	public void deductPointsTest() {
		Transaction t = new Transaction();
		t.setPayer("Test");
		t.setPoints(-300);
		t.setDate(TransactionController.convertDateFormat("12/5 5PM"));
		
		Assert.assertEquals(200, service.addPoints(t));
	}
}
