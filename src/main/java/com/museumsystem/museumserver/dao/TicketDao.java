package com.museumsystem.museumserver.dao;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.museumsystem.museumserver.model.Customer;
import com.museumsystem.museumserver.model.Ticket;

@Transactional
@Repository
public interface TicketDao extends CrudRepository<Ticket, Long>{
	public List<Ticket> findByCustomer(Customer customer);
	public Long countByCustomer(Customer customer);
}
