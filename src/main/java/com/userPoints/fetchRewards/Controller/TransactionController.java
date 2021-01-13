package com.userPoints.fetchRewards.Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.userPoints.fetchRewards.Entity.Transaction;
import com.userPoints.fetchRewards.Service.TransactionService;

@RestController
public class TransactionController {
	
	/*
	 * dependency injection of TransactionService interface
	 * */
	@Autowired
	private TransactionService service;
	
	/*
	 * endpoint for getting most updated balance per vendor
	 * */
	@GetMapping(value="/balance", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<String> getBalance() {
		
		return service.getPoints();
	}
	
	//Test URL
	//http://localhost:9004/point?Payer=Test&Points=3000&Date=1/12+2PM
	/*
	 * endpoint for adding points or default ones
	 * */
	@PostMapping(value="/point")
	public void addPoints( @RequestParam(value="Payer",required=false) String payer,
							@RequestParam(value="Points",required=false) Integer points,
							@RequestParam(value="Date",required=false) String date) {
		
		if(payer == null || points == null || date == null) {
			List<Transaction> transactions = fillDefault();
			
			transactions.forEach((t) -> service.addPoints(t) );
		} else {
			Transaction transaction = new Transaction();
			transaction.setPayer(payer);
			transaction.setPoints(points);
			transaction.setDate(convertDateFormat(date) );
			
			service.addPoints(transaction);
		}
	}
	
	/*
	 * endpoint for deducting points
	 * */
	@GetMapping(value="/deduct/{points}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<String> deductPoints(@PathVariable(value="points") Integer points) {
		
		return service.deductPoints(points);
	}
	
	/*
	 * custom method adding transactions to db if there is no value present on out points call
	 * this can be removed once we have an actual endpoint sending us the data
	 * */
	public static List<Transaction> fillDefault() {
		List<Transaction> transactions = new ArrayList<>();
		
		Transaction t1 = new Transaction();
		t1.setPayer("DANNON");
		t1.setPoints(300);
		t1.setDate(convertDateFormat("10/31 10AM"));
		transactions.add(t1);
		
		Transaction t2 = new Transaction();
		t2.setPayer("UNILEVER");
		t2.setPoints(200);
		t2.setDate(convertDateFormat("10/31 11AM"));
		transactions.add(t2);

		Transaction t3 = new Transaction();
		t3.setPayer("DANNON");
		t3.setPoints(-200);
		t3.setDate(convertDateFormat("10/31 3PM"));
		transactions.add(t3);
		
		Transaction t4 = new Transaction();
		t4.setPayer("MILLER COORS");
		t4.setPoints(10000);
		t4.setDate(convertDateFormat("11/1 2PM"));
		transactions.add(t4);
		
		Transaction t5 = new Transaction();
		t5.setPayer("DANNON");
		t5.setPoints(1000);
		t5.setDate(convertDateFormat("11/2 2PM"));
		transactions.add(t5);
		
		return transactions;
	}
	
	
	/*
	 * these three methods are used to convert incoming date data to 
	 * proper date format for fetching and ordering later
	 * */
	public static Date convertDateFormat(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm");
		
		StringBuilder sb = new StringBuilder();
		
		String[] arr = date.split(" ");
		
		String[] monthAndDay = arr[0].split("/");
		
		sb = checkMonthAndDay(monthAndDay,sb);
		sb.append(" ");
		
		String time = arr[1].substring(0, arr[1].length()-2);
		String mode = arr[1].substring( arr[1].length()-2, arr[1].length() );
		
		sb = convertTimeto24HourFormat(time, mode, sb);
		
		Date d = new Date();
		
		try {  
	        d = formatter.parse(sb.toString());
	    } catch (ParseException e) {
	    	e.printStackTrace();
	    }  
		
		return d;
	}
	
	public static StringBuilder convertTimeto24HourFormat(String time, String mode, StringBuilder sb) {
		
		String[] timearr = time.split(":");
		
		int hour = Integer.parseInt(timearr[0]);
		
		if("PM".equals(mode) && hour<12) {
			hour += 12;
		}
		
		if("AM".equals(mode) && hour == 12) {
			hour += 12;
		}
		
		sb.append(hour);
		
		if(timearr.length > 1) {
			sb.append(":" + timearr[1]);
		}else {
			sb.append(":00");
		}
		
		return sb;
	}
	
	public static StringBuilder checkMonthAndDay(String[] monthAndDay, StringBuilder sb) {
		
		if(monthAndDay[0].length() == 1) sb.append("0" + monthAndDay[0] + "/");
		else sb.append(monthAndDay[0] + "/");
		
		if(monthAndDay[1].length() == 1) {
			sb.append("0");
			sb.append(monthAndDay[1]);
		}
		else {
			sb.append(monthAndDay[1]);
		}
		
		return sb;
	}
	
}
