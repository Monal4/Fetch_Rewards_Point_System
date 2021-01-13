package com.userPoints.fetchRewards.ControllerTest;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.userPoints.fetchRewards.Controller.TransactionController;
import com.userPoints.fetchRewards.Entity.Transaction;
import com.userPoints.fetchRewards.Repository.TransactionRepository;
import com.userPoints.fetchRewards.Service.TransactionService;

import junit.framework.Assert;


@WebMvcTest
@RunWith(SpringRunner.class)
public class TransactionControllerTest {

	@Autowired
	MockMvc mockmvc;
	
	@MockBean
	TransactionService service;
	
	@MockBean
	TransactionRepository repository;
	
	@Before
	public void setUp() {
		Transaction t = new Transaction();
		t.setPayer("payer");
		t.setDate(TransactionController.convertDateFormat("12/5 5PM"));
		t.setPoints(500);
		t.setId("2003TEST");
		
		repository.addTransaction(t);
	}
	
	@After
	public void cleanUp() {
		repository.removeTransaction("2003TEST");
	}
	
	@Test
	public void getBalance() throws Exception{
		mockmvc.perform(
				get("/balance").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
		).andExpect(
				status().isOk() 
		);
		
		verify(service).getPoints();
	}
	
	@Test
	public void addPoints() throws Exception {
		mockmvc.perform(
				post("/point")
				
		).andExpect(
				status().isOk()
		);
	}
	
	@Test
	public void deductPointTest() throws Exception{
		mockmvc.perform(
				get("/deduct/{points}", 400)
		).andExpect(
				status().isOk()
		);
	}
	
	@Test
	public void testconvertStringToDate() {
		
		Date date = TransactionController.convertDateFormat("12/5 5PM");
		
		String pattern = "MM/dd HH:mm";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		String date1 = simpleDateFormat.format(date);
		Assert.assertEquals("12/05 17:00", date1);
	}
	
	/*
	 * Automatic Day switching once we insert date to db after converting it and formating it back again.
	 * */
	
	@Test
	public void testconvertStringToDate2() {
		
		Date date = TransactionController.convertDateFormat("1/1 12AM");
		
		String pattern = "MM/dd HH:mm";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		String date1 = simpleDateFormat.format(date);
		
		Assert.assertEquals("01/02 00:00", date1);
	}
	
	@Test
	public void convertTimeto24HourFormatTestMidnightTest() {
		Assert.assertEquals("24:00", TransactionController.convertTimeto24HourFormat("12", "AM", new StringBuilder()).toString());
	}
	
	@Test
	public void convertTimeto24HourFormatTestMorningTest() {
		Assert.assertEquals("1:00", TransactionController.convertTimeto24HourFormat("1", "AM", new StringBuilder()).toString());
	}
	
	@Test
	public void convertTimeto24HourFormatTestNoonTest() {
		Assert.assertEquals("12:00", TransactionController.convertTimeto24HourFormat("12", "PM", new StringBuilder()).toString());
	}
	
	@Test
	public void convertTimeto24HourFormatTestAfterNoonTest() {
		Assert.assertEquals("16:00", TransactionController.convertTimeto24HourFormat("4", "PM", new StringBuilder()).toString());
	}
	
	@Test
	public void checkMonthAndDayFirstMonth() {
		Assert.assertEquals("01/01", TransactionController.checkMonthAndDay(new String[] {"1","1"}, new StringBuilder()).toString());
	}
	
	@Test
	public void checkMonthAndDayFirstMonth10days() {
		Assert.assertEquals("01/11", TransactionController.checkMonthAndDay(new String[] {"1","11"}, new StringBuilder()).toString());
	}
	
	@Test
	public void checkMonthAndDayLastMonth10days() {
		Assert.assertEquals("12/11", TransactionController.checkMonthAndDay(new String[] {"12","11"}, new StringBuilder()).toString());
	}
}
