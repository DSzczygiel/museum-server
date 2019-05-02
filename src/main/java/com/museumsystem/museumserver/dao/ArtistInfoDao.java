package com.museumsystem.museumserver.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.museumsystem.museumserver.model.Artist;
import com.museumsystem.museumserver.model.ArtistInfo;

@Transactional
@Repository
public interface ArtistInfoDao extends CrudRepository<ArtistInfo, Long>{
	@Query("SELECT a FROM ArtistInfo a WHERE a.artist = :artist AND a.langCode = :lang")
	public ArtistInfo getArtistInfo(@Param("artist") Artist artist, @Param("lang") String lang);
	
	public List<ArtistInfo> findByArtist(Artist artist);
}
