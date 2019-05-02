package com.museumsystem.museumserver.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.museumsystem.museumserver.model.News;

public interface NewsDao extends CrudRepository<News, Long>{
	List<News> findByLang(String lang);
}
