package com.museumsystem.museumserver.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.museumsystem.museumserver.model.Artist;

@Transactional
@Repository
public interface ArtistDao extends CrudRepository<Artist, Long>{
	public Optional<Artist> findById(Long id);
	public Iterable<Artist> findAll();
}
