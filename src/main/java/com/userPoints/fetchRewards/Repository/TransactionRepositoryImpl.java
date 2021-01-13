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

	@Override
	public void removeTransaction(String Id) {
		Transaction t = entityManager.find(Transaction.class, Id);
		if( t!=null ) {
			entityManager.remove(t);
		}
	}

	@Override
	public List<Transaction> getSortedTransactions() {
		Query query = entityManager.createQuery("SELECT t FROM Transaction t ORDER BY t.date ASC", Transaction.class);
		List<Transaction> result = query.getResultList();

		return result;
	}

}
