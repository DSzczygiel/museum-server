package com.museumsystem.museumserver.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.museumsystem.museumserver.model.Payment;

@Transactional
@Repository
public interface PaymentDao extends CrudRepository<Payment, Long>{

}
