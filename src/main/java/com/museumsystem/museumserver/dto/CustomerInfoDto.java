package com.museumsystem.museumserver.dto;

public class CustomerInfoDto {

	private String firstName;
	private String lastName;
	private String email;
	private Integer daysFromRegister;
	private Integer orderedTickets;
	private String registerDate;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getDaysFromRegister() {
		return daysFromRegister;
	}
	public void setDaysFromRegister(Integer daysFromRegister) {
		this.daysFromRegister = daysFromRegister;
	}
	public Integer getOrderedTickets() {
		return orderedTickets;
	}
	public void setOrderedTickets(Integer orderedTickets) {
		this.orderedTickets = orderedTickets;
	}
	public String getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
	@Override
	public String toString() {
		return "CustomerInfoDto [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", daysFromRegister=" + daysFromRegister + ", orderedTickets=" + orderedTickets + ", registerDate="
				+ registerDate + "]";
	}	
}
