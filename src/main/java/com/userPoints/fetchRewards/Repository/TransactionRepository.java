package com.userPoints.fetchRewards.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.userPoints.fetchRewards.Entity.Transaction;

public interface TransactionRepository {
	
	List<String> getBalance();
	
	int addTransaction(Transaction transaction);

	void removeTransaction(String Id);
	
	List<Transaction> getSortedTransactions();
}
