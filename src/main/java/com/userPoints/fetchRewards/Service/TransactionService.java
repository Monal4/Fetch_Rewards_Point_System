package com.userPoints.fetchRewards.Service;

import java.util.Date;
import java.util.List;

import com.userPoints.fetchRewards.Entity.Transaction;

/*
 * interface to achieve polymorphism and reusability
 * */
public interface TransactionService {
	
	List<String> getPoints();
	
	int addPoints(Transaction transaction);
	
	List<String> deductPoints(Integer points);
}
