package com.museumsystem.museumserver.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.museumsystem.museumserver.model.Artwork;
import com.museumsystem.museumserver.model.ArtworkOperation;

public interface ArtworkOperationDao extends CrudRepository<ArtworkOperation, Long>{
	public List<ArtworkOperation> findByArtwork(Artwork artwork);
}
