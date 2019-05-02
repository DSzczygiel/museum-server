package com.museumsystem.museumserver.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.museumsystem.museumserver.model.Customer;

@Transactional
@Repository
public interface CustomerDao extends CrudRepository<Customer, Long> {
	public Customer findByEmail(String email);
}
