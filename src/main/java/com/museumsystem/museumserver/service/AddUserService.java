package com.museumsystem.museumserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.museumsystem.museumserver.dao.EmployeeDao;
import com.museumsystem.museumserver.dao.CustomerDao;
import com.museumsystem.museumserver.model.Employee;
import com.museumsystem.museumserver.model.Customer;

@Service
public class AddUserService {

    @Autowired
    private CustomerDao userDao;
    @Autowired
    private EmployeeDao empDao;
    
    /**
     * 
     * @param email email of the user to be checked
     * @return true, if user exists, otherwise, false
     */
    public boolean checkIfUserExistsByMail(String email) {
    	Customer user = null;
    	user = userDao.findByEmail(email);
    	Employee emp = null;
    	emp = empDao.findByEmail(email);
    	
    	return (user != null || emp !=null);
    }
    
    /**
     * 
     * @param user user to be added to the database
     * @return true on success, false if user already exists
     */
    public Boolean addUser(Customer user){
    	if(checkIfUserExistsByMail(user.getEmail())) {
    		return false;
    	}else {
    		userDao.save(user);
    		return true;
    	}
    }
}
