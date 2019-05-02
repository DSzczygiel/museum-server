package com.museumsystem.museumserver.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.museumsystem.museumserver.dao.ArtistDao;
import com.museumsystem.museumserver.dao.ArtistInfoDao;
import com.museumsystem.museumserver.dto.ArtistDto;
import com.museumsystem.museumserver.model.Artist;
import com.museumsystem.museumserver.model.ArtistInfo;

@Service
public class ArtistService {
	@Autowired
	ArtistDao artistDao;
	@Autowired
	ArtistInfoDao artistInfoDao;

	public List<ArtistDto> getArtists(String lang) {
		Iterable<Artist> artistIterable = artistDao.findAll();
		List<ArtistDto> artists = new ArrayList<>();
		for (Artist a : artistIterable) {
			if (a.isActive()) {
				ArtistInfo artistInfo = artistInfoDao.getArtistInfo(a, lang);
				ArtistDto artist = new ArtistDto();
				artist.setId(a.getId());
				artist.setName(artistInfo.getName());
				artist.setDescription(artistInfo.getDescription());
				artist.setBirthDate(a.getBirthDate());
				artist.setDeathDate(a.getDeathDate());

				artists.add(artist);
			}
		}

		return artists;
	}

	public boolean updateArtist(String lang, Long id, ArtistDto artist) {
		Optional<Artist> artistOptional = artistDao.findById(id);
		if (artistOptional.isPresent()) {
			Artist a = artistOptional.get();
			ArtistInfo aInfo = artistInfoDao.getArtistInfo(a, lang);

			a.setBirthDate(artist.getBirthDate());
			a.setDeathDate(artist.getDeathDate());
			aInfo.setName(artist.getName());
			aInfo.setDescription(artist.getDescription());

			artistDao.save(a);
			artistInfoDao.save(aInfo);
			return true;
		}
		return false;
	}

	public boolean addArtist(String lang, ArtistDto artist) {

		List<String> langs = new ArrayList<>();
		langs.add("pl");
		langs.add("en");

		Artist a = new Artist();
		a.setActive(true);
		a.setBirthDate(artist.getBirthDate());
		a.setDeathDate(artist.getDeathDate());
		artistDao.save(a);

		for (String l : langs) {
			ArtistInfo aInfo = new ArtistInfo();
			aInfo.setArtist(a);
			aInfo.setLangCode(l);
			aInfo.setName(artist.getName());

			if (l.equals(lang)) {
				aInfo.setDescription(artist.getDescription());
			} else {
				aInfo.setDescription(" ");
			}
			artistInfoDao.save(aInfo);
		}

		return true;
	}

	public boolean deleteArtist(Long id) {
		Optional<Artist> artistOptional = artistDao.findById(id);
		if (artistOptional.isPresent()) {
			Artist a = artistOptional.get();
			a.setActive(false);
			artistDao.save(a);
			return true;
		}
		return false;
	}
}
