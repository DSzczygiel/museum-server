package com.museumsystem.museumserver.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.museumsystem.museumserver.dao.EmployeeDao;
import com.museumsystem.museumserver.dao.CustomerDao;
import com.museumsystem.museumserver.model.Employee;
import com.museumsystem.museumserver.model.Customer;

@Service
public class LoginUserService implements UserDetailsService {

	@Autowired
	private CustomerDao userDao;
	@Autowired
	private EmployeeDao empDao;
	@Autowired 
	TicketService ticketService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		System.out.println("login service getbyusername");
		
		Customer user = userDao.findByEmail(email);

		if (user != null) {
			ticketService.updateCustomerTickets(email);
			return user;
		} else {
			Employee emp = empDao.findByEmail(email);

			if (emp != null)
				return emp;
			else
				throw new UsernameNotFoundException("Invalid email");
		}
	}
}
