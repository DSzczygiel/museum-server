package com.museumsystem.museumserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.museumsystem.museumserver.dao.CustomerDao;
import com.museumsystem.museumserver.model.Customer;

@Service
public class ActivateUserAccountService {

	@Autowired
	CustomerDao userDao;
	/**
	 * 
	 * @param email email of the user to be activated
	 * @return -1 if user doesn't exist, 
	 * -2 if user is already activated,
	 * 1 on success
	 */
	public int activateUser(String email) {
		Customer user = userDao.findByEmail(email);
		
		if(user == null) {
			return -1;
		}else if(user.isActive() == true) {
			return -2;
		}else {
			user.setActive(true);
			userDao.save(user);
			
			return 1;
		}
	}
}
