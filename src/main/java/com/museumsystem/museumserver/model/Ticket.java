package com.museumsystem.museumserver.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "ticket")
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	Long id;
	
	@NotNull(message = "Customer cannot be null")
	@ManyToOne
	@JoinColumn(name = "customer_id")
	Customer customer;
	
	@NotBlank(message = "status cannot be blank")
	@Column(name = "status")
	String status;
	
	@NotNull(message = "Date cannot be null")
	@Column(name = "date")
	String date;
	
	@NotNull(message = "Order date cannot be null")
	@Column(name = "order_date")
	String orderDate;
	
	@Column(name = "adults_nr")
	int adultsNr;
	
	@Column(name = "children_nr")
	int childrenNr;
	
	@Column(name = "price")
	double price;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    @JsonProperty("customer_email")
	public Customer getCustomer() {
		return customer;
	}

    @JsonProperty("customer")
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	
	public int getAdultsNr() {
		return adultsNr;
	}

	public void setAdultsNr(int adultsNr) {
		this.adultsNr = adultsNr;
	}

	public int getChildrenNr() {
		return childrenNr;
	}

	public void setChildrenNr(int childrenNr) {
		this.childrenNr = childrenNr;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Ticket [id=" + id + ", customer=" + customer + ", status=" + status + ", date=" + date + ", adultsNr="
				+ adultsNr + ", childrenNr=" + childrenNr + ", price=" + price + "]";
	}
}
