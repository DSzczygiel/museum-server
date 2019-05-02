package com.museumsystem.museumserver.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.museumsystem.museumserver.model.Artwork;
import com.museumsystem.museumserver.model.Employee;

@Transactional
@Repository
public interface ArtworkDao extends CrudRepository<Artwork, Long>{
	public Optional<Artwork> findById(Long id);
	public Iterable<Artwork> findAll();
	public Iterable<Artwork> findByAssignedEmployee(Employee employee);
}
