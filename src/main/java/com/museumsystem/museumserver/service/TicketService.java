package com.museumsystem.museumserver.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import com.museumsystem.museumserver.dao.CustomerDao;
import com.museumsystem.museumserver.dao.TicketDao;
import com.museumsystem.museumserver.dao.TicketPricesDao;
import com.museumsystem.museumserver.model.Customer;
import com.museumsystem.museumserver.model.Payment;
import com.museumsystem.museumserver.model.Ticket;
import com.museumsystem.museumserver.model.TicketPrices;
import com.museumsystem.museumserver.utlis.JWTTokenManager;
import com.museumsystem.museumserver.utlis.PdfCreator;

@Service
public class TicketService {
	@Autowired 
	CustomerDao customerDao;
	@Autowired
	TicketPricesDao ticketPricesDao;
	@Autowired
	TicketDao ticketDao;
	@Autowired
	EmailManagerService emailManagerService;
	
	public Ticket addTicket(Ticket ticket, String email) {
		Optional<TicketPrices> ticketPricesOptional = ticketPricesDao.findById(1L);
		TicketPrices ticketPrices = ticketPricesOptional.get();
		Customer customer = customerDao.findByEmail(email);
		if(customer == null)
			return null;
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyy");
		Calendar cal = Calendar.getInstance();
		String currentDate = dateFormat.format(cal.getTime());
		
		int price = ticket.getAdultsNr() * ticketPrices.getAdultPrice() + ticket.getChildrenNr() * ticketPrices.getChildrenPrice();
		
		ticket.setCustomer(customer);
		ticket.setStatus("unpaid");
		ticket.setPrice(price);
		ticket.setOrderDate(currentDate);
		ticket = ticketDao.save(ticket);
		
		return ticket;
	}
	
	public Ticket getTicketById(Long id) {
		Optional<Ticket> ticketOptional = ticketDao.findById(id);
		
		if(ticketOptional.isPresent()) {
			return ticketOptional.get();
		}else {
			return null;
		}
	}
	
	public List<Ticket> getCustomerTickets(String email){
		Customer customer = customerDao.findByEmail(email);
		
		if(customer == null)
			return null;
		
		List<Ticket> list = ticketDao.findByCustomer(customer);
		
		return list;	
	}
	
	//TODO Update tickets after login
	public void updateCustomerTickets(String email) {
		List<Ticket> tickets = getCustomerTickets(email);
		
		for(Ticket t : tickets) {
			if(t.getStatus().equals("valid") || t.getStatus().equals("unpaid")) {
				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyy");
				Calendar cal = Calendar.getInstance();
				try {
					Date ticketDate = dateFormat.parse(t.getDate());
					cal.set(Calendar.MILLISECOND, 0);
					cal.set(Calendar.HOUR, 0);
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.MINUTE , 0);
					cal.set(Calendar.SECOND , 0);

					if(ticketDate.before(cal.getTime())) {
						t.setStatus("expired");
						ticketDao.save(t);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		}
	}
	
	public boolean processPayment(Payment payment) {
		Optional<Ticket> ticketOptional = ticketDao.findById(Long.valueOf(payment.getDescription()));
		
		if(ticketOptional.isPresent()) {
			Ticket ticket = ticketOptional.get();
			if(ticket.getStatus().equals("unpaid") && (ticket.getPrice() == payment.getAmount())) {
				ticket.setStatus("valid");
				ticketDao.save(ticket);
				
				InputStreamResource iss = PdfCreator.getTicketPdf(ticket);
				emailManagerService.sendEmailWithTicket(ticket.getCustomer().getEmail(), "ticket_" + ticket.getId(), iss);
				return true;
			}
		}
		return false;
	}
	
	private boolean ticketBelongsToCustomer(Ticket ticket, Customer customer) {
		return ticket.getCustomer().getId() == customer.getId();
	}
	
	public String generateTicketCode(Long id, String email) {
		Optional<Ticket> ticketOptional = ticketDao.findById(id);
		if(ticketOptional.isPresent()) {
			Customer customer = customerDao.findByEmail(email);
			Ticket ticket = ticketOptional.get();
			if(customer != null) {
				if(ticketBelongsToCustomer(ticket, customer)) {
					String code = JWTTokenManager.generateTicketToken(ticket, "qwerty");
					return code;
				}else {
					return "err_invalid_data";
				}
			}else {
				return "err_no_customer";
			}
		}else {
			return "err_no_ticket";
		}		
	}
	
	public boolean validateTicket(Long id) {
		Optional<Ticket> ticketOptional = ticketDao.findById(id);
		
		if(ticketOptional.isPresent()) {
			Ticket t = ticketOptional.get();
			
			if(t.getStatus().equals("valid")) {
				t.setStatus("expired");
				ticketDao.save(t);
			
				return true;
			}
		}
		return false;
	}
}
