package com.userPoints.fetchRewards.TransactionException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NO_CONTENT)
public class TransactionException extends RuntimeException{
	
	public TransactionException(String message) {
		super(message);
	}
}
