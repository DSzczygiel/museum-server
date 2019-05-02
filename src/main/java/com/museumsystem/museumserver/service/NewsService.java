package com.museumsystem.museumserver.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.museumsystem.museumserver.dao.EmployeeDao;
import com.museumsystem.museumserver.dao.NewsDao;
import com.museumsystem.museumserver.model.Employee;
import com.museumsystem.museumserver.model.News;

@Service
public class NewsService {
	@Autowired
	NewsDao newsDao;
	@Autowired
	EmployeeDao employeeDao;
	
	public List<HashMap<String, String>> getNews(String lang) {
		
		
		List<HashMap<String, String>> newsList = new ArrayList<>();
		List<News> news = newsDao.findByLang(lang);
		
		for(News n : news) {
			if(n.isActive()) {
				HashMap<String, String> map = new HashMap<>();
				map.put("date", n.getDate());
				map.put("content", n.getContent());
				map.put("id", n.getId().toString());
				newsList.add(map);
			}
		}
		return newsList;
	}
	
	public boolean addNews(String email, HashMap<String, String> requestData) {
		Employee emp = employeeDao.findByEmail(email);
		if(emp == null)
			return false;
		
		News news = new News();
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyy");
		Calendar cal = Calendar.getInstance();
		String currentDate = dateFormat.format(cal.getTime());
		
		news.setActive(true);
		news.setContent(requestData.get("content"));
		news.setDate(currentDate);
		news.setEmployee(emp);
		news.setLang(requestData.get("lang"));
		
		newsDao.save(news);
		return true;
	}
	
	public boolean deleteNews(Long id) {
		Optional<News> newsOptional = newsDao.findById(id);
		
		if(newsOptional.isPresent()) {
			News news = newsOptional.get();
			news.setActive(false);
			newsDao.save(news);
			
			return true;
		}
		
		return false;
	}
	
	public boolean updateNews(Long id, String email, String content) {
		Employee emp = employeeDao.findByEmail(email);
		
		if(emp == null)
			return false;
		
		Optional<News> newsOptional = newsDao.findById(id);
	
		if(newsOptional.isPresent()) {
			News news = newsOptional.get();
			news.setContent(content);
			news.setEmployee(emp);
			newsDao.save(news);
			
			return true;
		}
		
		return false;
	}
}
