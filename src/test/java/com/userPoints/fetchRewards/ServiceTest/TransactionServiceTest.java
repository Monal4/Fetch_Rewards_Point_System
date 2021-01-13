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
	
	List<String> transactionsgetBalance = new ArrayList<>();
	List<Transaction> t = new ArrayList<>();
	Transaction transaction = new Transaction();
	
	@Before
	public void setup() {
		
		Transaction t1 = getTestTransaction();
		t.add(t1);
		transactionsgetBalance.add(t1.getPayer() + ", " + t1.getPoints());
		
		Mockito.when(repository.getBalance()).thenReturn(transactionsgetBalance);
		
		repository.addTransaction(t1);
		
		Mockito.when(repository.addTransaction(transaction)).thenReturn(500);
		
		Mockito.when(repository.getSortedTransactions()).thenReturn(t); //should return sorted list of transactions
	}
	
	@After
    public void cleanup() {
    }
	
	public Transaction getTestTransaction() {
		transaction.setDate(TransactionController.convertDateFormat("12/5 5PM"));
		transaction.setId("3TEST");
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
		
		int points = service.addPoints(getTestTransaction());
		
		Assert.assertEquals(500, points);
	}
	
	@Test
	public void deductPointsTest() {
		List<String> x = service.deductPoints(300);
		
		List<String> expectedDeduction = new ArrayList<>();
		expectedDeduction.add("TestPayer, -300, now");
		
		Assert.assertEquals(expectedDeduction, x);
	}
}
