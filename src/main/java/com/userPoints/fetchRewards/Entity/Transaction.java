package com.userPoints.fetchRewards.Entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.OrderBy;

@Entity
public class Transaction {
	
	@Id
	private String Id;
	
	@Column(name="Payer")
	private String payer;
	
	@Column(name="Points")
	private Integer points;
	
	@Column(name="Date")
	private Date date;

	public Transaction() {
		this.Id = UUID.randomUUID()
                .toString();
	}
	
	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getPayer() {
		return payer;
	}

	public void setPayer(String payer) {
		this.payer = payer;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
