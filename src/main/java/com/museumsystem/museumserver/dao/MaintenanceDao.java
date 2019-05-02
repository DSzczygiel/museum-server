package com.museumsystem.museumserver.dao;

import org.springframework.data.repository.CrudRepository;

import com.museumsystem.museumserver.model.Maintenance;

public interface MaintenanceDao extends CrudRepository<Maintenance, Long>{
	
}
