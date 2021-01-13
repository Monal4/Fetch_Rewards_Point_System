package com.userPoints.fetchRewards.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.userPoints.fetchRewards.Entity.Transaction;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository{

	@PersistenceContext
	EntityManager entityManager;
	
	
	/*
	 * query for grouping points based on vendors
	 * */
	@Override
	public List<String> getBalance() {
		
		Query query = entityManager.createQuery("SELECT t.payer, sum(t.points) as balance FROM Transaction t GROUP BY t.payer");
		
		List<Object[]> result =  query.getResultList();
		
		List<String> values = new ArrayList<>();
		
		result.forEach((r) -> {
			String s = r[0] + ", " + r[1];
			values.add(s);
		});
		
		return values;
	}

	
	/*
	 * 
	 * 
	 *checks if the vendor with id exists then we will simply update the row
	 *or we will insert new vendor with points and date of entry
	 * 
	 * 
	 * */
	@Override
	public int addTransaction(Transaction transaction) {
		
		Transaction t = entityManager.find(Transaction.class, transaction.getId());
		if( t!=null ) {
			entityManager.merge(t);
			return transaction.getPoints();
		}
		
		entityManager.persist(transaction);
		return transaction.getPoints();
	}

	/*
	 * used in testing custom transaction
	 * */
	@Override
	public void removeTransaction(String Id) {
		Transaction t = entityManager.find(Transaction.class, Id);
		if( t!=null ) {
			entityManager.remove(t);
		}
	}

	
	/*
	 * get sorted transactions for deduction
	 * */
	@Override
	public List<Transaction> getSortedTransactions() {
		Query query = entityManager.createQuery("SELECT t FROM Transaction t ORDER BY t.date ASC", Transaction.class);
		List<Transaction> result = query.getResultList();

		return result;
	}

}
