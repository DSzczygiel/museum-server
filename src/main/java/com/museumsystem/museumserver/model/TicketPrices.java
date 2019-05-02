package com.museumsystem.museumserver.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ticket_prices")
public class TicketPrices {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	Long id;
	
	@Column(name = "adult_price")
	int adultPrice;
	
	@Column(name = "children_price")
	int childrenPrice;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getAdultPrice() {
		return adultPrice;
	}

	public void setAdultPrice(int adultPrice) {
		this.adultPrice = adultPrice;
	}

	public int getChildrenPrice() {
		return childrenPrice;
	}

	public void setChildrenPrice(int childrenPrice) {
		this.childrenPrice = childrenPrice;
	}
}
