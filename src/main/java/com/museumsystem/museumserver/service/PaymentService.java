package com.museumsystem.museumserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.museumsystem.museumserver.dao.PaymentDao;
import com.museumsystem.museumserver.model.Payment;

@Service
public class PaymentService {
	
	@Autowired
	private PaymentDao paymentDao;
	@Autowired
	private TicketService ticketService;
	
	public int addPayment(Payment payment) {
		paymentDao.save(payment);
		ticketService.processPayment(payment);
		return 0;
		
	}
}
