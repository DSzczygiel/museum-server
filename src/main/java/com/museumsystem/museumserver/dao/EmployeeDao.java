package com.museumsystem.museumserver.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.museumsystem.museumserver.model.Employee;

@Transactional
@Repository
public interface EmployeeDao extends CrudRepository<Employee, Long>{
	public Employee findByEmail(String email);
}
