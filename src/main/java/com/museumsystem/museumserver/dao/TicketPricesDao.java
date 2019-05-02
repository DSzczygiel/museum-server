package com.museumsystem.museumserver.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.museumsystem.museumserver.model.TicketPrices;

@Transactional
@Repository
public interface TicketPricesDao extends CrudRepository<TicketPrices, Long>{
	public Optional<TicketPrices> findById(Long id);
}
