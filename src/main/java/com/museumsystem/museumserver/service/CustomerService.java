package com.museumsystem.museumserver.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.museumsystem.museumserver.dao.CustomerDao;
import com.museumsystem.museumserver.dao.EmployeeDao;
import com.museumsystem.museumserver.dao.TicketDao;
import com.museumsystem.museumserver.dto.CustomerInfoDto;
import com.museumsystem.museumserver.model.Customer;
import com.museumsystem.museumserver.model.Employee;

@Service
public class CustomerService {
	
	@Autowired
	CustomerDao customerDao;
	@Autowired
	TicketDao ticketDao;
	@Autowired
	EmployeeDao employeeDao;
	@Autowired
	PasswordEncoder userPasswordEncoder;
	
	public CustomerInfoDto getCustomerInfo(String email) {
		Customer customer = customerDao.findByEmail(email);
		if(customer != null) {
			CustomerInfoDto customerInfo = new CustomerInfoDto();
			customerInfo.setFirstName(customer.getFirstName());
			customerInfo.setLastName(customer.getLastName());
			customerInfo.setEmail(customer.getEmail());
			customerInfo.setRegisterDate(customer.getRegisterDate());
			customerInfo.setOrderedTickets(ticketDao.countByCustomer(customer).intValue());
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
			Calendar cal = Calendar.getInstance();
			
			try {
				Date registerDate = dateFormat.parse(customer.getRegisterDate());
				cal.set(Calendar.MILLISECOND, 0);
				cal.set(Calendar.HOUR, 0);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE , 0);
				cal.set(Calendar.SECOND , 0);
				
				long diff = cal.getTimeInMillis() - registerDate.getTime();
				Long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
				customerInfo.setDaysFromRegister(days.intValue());
				return customerInfo;

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}	
		}else {
			return null;
		}
	}
	
	public boolean deleteUser(String email) {
		return false;
		
	}
	
	public String changeData(String email, String newEmail, String firstName, String lastName) {
		Customer cust = customerDao.findByEmail(email);
		
		if(cust == null)
			return "err_no_user";
		
		if(newEmail == null || firstName == null || lastName == null)
			return "err_incomplete_data";
					
		if(!cust.getFirstName().equals(firstName))
			cust.setFirstName(firstName);
		
		if(!cust.getLastName().equals(lastName))
			cust.setLastName(lastName);
		
		if(!cust.getEmail().equals(newEmail)) {
			Customer c = customerDao.findByEmail(newEmail);
			Employee e = employeeDao.findByEmail(newEmail);
			
			if(c == null && e == null)
				cust.setEmail(newEmail);
			else
				return "err_email_used";
		}
		customerDao.save(cust);
		
		return "success";
		
	}
	
	public String changePassword(String email, String password, String newPassword) {
		Customer cust = customerDao.findByEmail(email);
		
		if(cust == null)
			return "err_no_user";
		
		if(!password.equals(newPassword)) {
			if(userPasswordEncoder.matches(password, cust.getPassword())) {
				cust.setPassword(userPasswordEncoder.encode(newPassword));
				customerDao.save(cust);
			}else{
				return "err_wrong_pass";
			}
		}
		
		return "success";
	}
	
	public String deleteCustomer(String email) {
		Customer cust = customerDao.findByEmail(email);
		
		if(cust == null)
			return "err_no_user";
		
		customerDao.delete(cust);
		
		return "success";
	}
}
