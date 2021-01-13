package com.userPoints.fetchRewards.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.TransactionalException;

import com.userPoints.fetchRewards.Entity.Transaction;
import com.userPoints.fetchRewards.Repository.TransactionRepository;
import com.userPoints.fetchRewards.TransactionException.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService{
	
	@Autowired
	private TransactionRepository repository;
	
	@Transactional
	public List<String> getPoints(){
		
		List<String> result = repository.getBalance();
		
		if(result == null || result.size() == 0 ) throw new TransactionalException(null, null);
		
		return result;
	}
	
	@Transactional
	public int addPoints(Transaction t) {
		
		if(t.getPoints() < 0) {
			return deductPointsMethod( -1 * t.getPoints() );
		}
		
		else return repository.addTransaction(t);		
	}
	
	public int deductPointsMethod(Integer val) {
		List<Transaction> sortedTransactions = repository.getSortedTransactions();
		
		if(sortedTransactions==null || sortedTransactions.size()==0) throw new TransactionalException(null, null);
		
		for(int i=0; i<sortedTransactions.size(); i++) {
			Transaction transaction = sortedTransactions.get(i);
			
			if(transaction.getPoints() >= val) {
				transaction.setPoints(transaction.getPoints()-val);
				return repository.addTransaction(transaction);
			}else {
				val -= transaction.getPoints();
				transaction.setPoints(0);
				repository.addTransaction(transaction);
			}
		}
		
		return -1;
	}
	
	@Transactional
	public List<String> deductPoints(Integer points) {
		
		List<Transaction> sortedTransactions = repository.getSortedTransactions();
		
		if(sortedTransactions==null || sortedTransactions.size()==0) throw new TransactionalException(null, null);
		
		List<String> result = new ArrayList<>();
		
		for(int i=0; i<sortedTransactions.size(); i++) {
			Transaction transaction = sortedTransactions.get(i);
			
			Transaction temp = new Transaction();
			
			StringBuilder sb = new StringBuilder();
			
			if(transaction.getPoints() >= points) {
				int actualPoints = transaction.getPoints();
				int difference = actualPoints-points;
				
				if(difference!=0) {
					transaction.setPoints(difference);
				}else {
					transaction.setPoints(difference);
				}
				repository.addTransaction(transaction);
				
				sb.append(transaction.getPayer());
				sb.append(", " +  (  -1 * ( actualPoints-difference )  ) );
				sb.append(", now");
				
				result.add(sb.toString());
				
				return result;
			}else if(transaction.getPoints() < points && transaction.getPoints()>0){
				
				int actualPoints = transaction.getPoints();
				
				points -= actualPoints;
				transaction.setPoints(0);
				repository.addTransaction(transaction);
				
				sb.append(transaction.getPayer());
				sb.append(", " +  (  -1*actualPoints  ) );
				sb.append(", now");
				
				result.add(sb.toString());
			}
		}
		
		if(points != 0) throw new TransactionalException(null, null);
		
		return result;
	}
}
