package com.museumsystem.museumserver.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.museumsystem.museumserver.model.Artwork;
import com.museumsystem.museumserver.model.ArtworkInfo;

@Transactional
@Repository
public interface ArtworkInfoDao extends CrudRepository<ArtworkInfo, Long>{
	@Query("SELECT a FROM ArtworkInfo a WHERE a.artwork = :artwork AND a.langCode = :lang")
	public ArtworkInfo getArtworkInfo(@Param("artwork") Artwork artwork, @Param("lang") String lang);
	public List<ArtworkInfo> findByArtwork(Artwork artwork);
}
