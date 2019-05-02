package com.museumsystem.museumserver.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.museumsystem.museumserver.dao.ArtistDao;
import com.museumsystem.museumserver.dao.ArtistInfoDao;
import com.museumsystem.museumserver.dao.ArtworkDao;
import com.museumsystem.museumserver.dao.ArtworkInfoDao;
import com.museumsystem.museumserver.dao.ArtworkOperationDao;
import com.museumsystem.museumserver.dao.EmployeeDao;
import com.museumsystem.museumserver.dao.MaintenanceDao;
import com.museumsystem.museumserver.dto.ArtistDto;
import com.museumsystem.museumserver.dto.ArtworkDto;
import com.museumsystem.museumserver.model.Artist;
import com.museumsystem.museumserver.model.ArtistInfo;
import com.museumsystem.museumserver.model.Artwork;
import com.museumsystem.museumserver.model.ArtworkInfo;
import com.museumsystem.museumserver.model.ArtworkOperation;
import com.museumsystem.museumserver.model.Employee;
import com.museumsystem.museumserver.model.Maintenance;

@Service
public class ArtworkService {

	@Autowired
	ArtworkDao artworkDao;
	@Autowired
	ArtworkInfoDao artworkInfoDao;
	@Autowired
	ArtistDao artistDao;
	@Autowired
	ArtistInfoDao artistInfoDao;
	@Autowired
	EmployeeDao employeeDao;
	@Autowired
	ArtworkOperationDao artworkOperationDao;
	@Autowired
	MaintenanceDao maintenanceDao;

	public ArtistDto getArtist(Artist artist, String lang) {
		ArtistInfo artistInfo = artistInfoDao.getArtistInfo(artist, lang);
		if (artistInfo != null) {
			ArtistDto artistDto = new ArtistDto();
			artistDto.setId(artist.getId());
			artistDto.setBirthDate(artist.getBirthDate());
			artistDto.setDeathDate(artist.getDeathDate());
			artistDto.setDescription(artistInfo.getDescription());
			artistDto.setName(artistInfo.getName());

			return artistDto;
		}
		return null;
	}

	public ArtworkDto getArtwork(Long id, String lang) {
		Optional<Artwork> artworkOptional = artworkDao.findById(id);
		Artwork artwork = null;

		if (artworkOptional.isPresent()) {
			artwork = artworkOptional.get();
			ArtworkInfo artworkInfo = artworkInfoDao.getArtworkInfo(artwork, lang);
			ArtistDto artist = getArtist(artwork.getArtist(), lang);
			if (artist != null) {
				ArtworkDto artworkDto = new ArtworkDto();
				artworkDto.setId(artwork.getId());
				artworkDto.setArtist(artist);
				artworkDto.setCreationYear(artwork.getCreationYear());
				artworkDto.setDescription(artworkInfo.getDescription());
				artworkDto.setPhoto(artwork.getPhoto());
				artworkDto.setTitle(artworkInfo.getTitle());

				return artworkDto;
			}
		}
		return null;
	}

	public List<ArtworkDto> getArtworks(String lang) {
		Iterable<Artwork> artworkIterable = artworkDao.findAll();

		List<ArtworkDto> artworks = new ArrayList<>();
		for (Artwork artwork : artworkIterable) {
			if (artwork.isActive()) {
				ArtworkInfo artworkInfo = artworkInfoDao.getArtworkInfo(artwork, lang);
				ArtistDto artist = getArtist(artwork.getArtist(), lang);
				if (artist != null) {
					ArtworkDto artworkDto = new ArtworkDto();
					artworkDto.setId(artwork.getId());
					artworkDto.setArtist(artist);
					artworkDto.setCreationYear(artwork.getCreationYear());
					artworkDto.setDescription(artworkInfo.getDescription());
					artworkDto.setPhoto(artwork.getPhoto());
					artworkDto.setTitle(artworkInfo.getTitle());

					artworks.add(artworkDto);
				}
			}
		}

		return artworks;
	}

	public boolean updateArtwork(String lang, Long id, ArtworkDto artwork) {
		Optional<Artwork> artworkOptional = artworkDao.findById(id);
		if (artworkOptional.isPresent()) {
			Artwork a = artworkOptional.get();
			ArtworkInfo aInfo = artworkInfoDao.getArtworkInfo(a, lang);
			Optional<Artist> artistOptional = artistDao.findById(artwork.getArtist().getId());

			if (artistOptional.isPresent()) {
				Artist artist = artistOptional.get();

				a.setArtist(artist);
				a.setCreationYear(artwork.getCreationYear());
				a.setPhoto(artwork.getPhoto());

				aInfo.setDescription(artwork.getDescription());
				aInfo.setTitle(artwork.getTitle());

				artworkDao.save(a);
				artworkInfoDao.save(aInfo);

				return true;
			}
		}
		return false;
	}

	public boolean addArtwork(String lang, ArtworkDto artwork, String email) {
		List<String> langs = new ArrayList<>();
		langs.add("pl");
		langs.add("en");

		Employee emp = employeeDao.findByEmail(email);

		if (emp == null)
			return false;

		Artwork a = new Artwork();
		Optional<Artist> artistOptional = artistDao.findById(artwork.getArtist().getId());

		if (artistOptional.isPresent()) {
			Artist artist = artistOptional.get();

			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyy");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, +6);
			String maintenanceDate = dateFormat.format(cal.getTime());

			a.setActive(true);
			a.setArtist(artist);
			a.setAssignedEmployee(emp);
			a.setCreationYear(artwork.getCreationYear());
			a.setMaintenanceDate(maintenanceDate);
			a.setPhoto(artwork.getPhoto());

			artworkDao.save(a);
			for (String l : langs) {
				ArtworkInfo aInfo = new ArtworkInfo();
				aInfo.setArtwork(a);
				aInfo.setLangCode(l);
				aInfo.setTitle(artwork.getTitle());

				if (l.equals(lang)) {
					aInfo.setDescription(artwork.getDescription());
				} else {
					aInfo.setDescription(" ");
				}
				artworkInfoDao.save(aInfo);
			}
			return true;
		}
		return false;
	}

	public boolean deleteArtwork(Long id) {
		Optional<Artwork> artworkOptional = artworkDao.findById(id);
		if (artworkOptional.isPresent()) {
			Artwork a = artworkOptional.get();
			a.setActive(false);
			artworkDao.save(a);
			return true;
		}
		return false;
	}

	public boolean addOperation(Long id, String email, String date, String description) {
		Employee emp = employeeDao.findByEmail(email);
		if (emp == null)
			return false;

		Optional<Artwork> artworkOptional = artworkDao.findById(id);
		if (artworkOptional.isPresent()) {
			Artwork a = artworkOptional.get();
			ArtworkOperation operation = new ArtworkOperation();

			operation.setArtwork(a);
			operation.setDate(date);
			operation.setDescription(description);
			operation.setEmployee(emp);

			artworkOperationDao.save(operation);
			return true;
		}
		return false;
	}

	public List<HashMap<String, String>> getArtworkOperations(Long id) {
		Optional<Artwork> artworkOptional = artworkDao.findById(id);
		List<HashMap<String, String>> operations = new ArrayList<>();
		if (artworkOptional.isPresent()) {
			List<ArtworkOperation> artworkOperations = artworkOptional.get().getOperations();

			for (ArtworkOperation a : artworkOperations) {
				HashMap<String, String> operation = new HashMap<>();
				operation.put("date", a.getDate());
				operation.put("description", a.getDescription());
				operation.put("employee", a.getEmployee().getFirstName() + " " + a.getEmployee().getLastName());

				operations.add(operation);
			}
			return operations;
		}
		return null;
	}

	public List<HashMap<String, String>> getMaintenances(String email) {
		Employee emp = employeeDao.findByEmail(email);
		if (emp == null)
			return null;

		List<HashMap<String, String>> maintenances = new ArrayList<>();
		Iterable<Artwork> artworks = emp.getArtworks();

		for (Artwork a : artworks) {
			if (a.isActive()) {
				ArtworkInfo artworkInfo = artworkInfoDao.getArtworkInfo(a, "pl");
				ArtistInfo artistInfo = artistInfoDao.getArtistInfo(a.getArtist(), "pl");
				HashMap<String, String> m = new HashMap<>();
				m.put("name", artworkInfo.getTitle());
				m.put("author", artistInfo.getName());
				m.put("date", a.getMaintenanceDate());
				m.put("art_id", a.getId().toString());
				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyy");
				Calendar cal = Calendar.getInstance();
				try {
					Date ticketDate = dateFormat.parse(a.getMaintenanceDate());
					cal.set(Calendar.MILLISECOND, 0);
					cal.set(Calendar.HOUR, 0);
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);

					if (ticketDate.before(cal.getTime())) {
						m.put("need_main", "true");
					} else {
						m.put("need_main", "false");
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				maintenances.add(m);
			}
		}
		return maintenances;
	}

	public boolean addMaintenance(Long id) {
		Optional<Artwork> artOptional = artworkDao.findById(id);
		if (artOptional.isPresent()) {
			Artwork art = artOptional.get();
			Maintenance main = new Maintenance();
			DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyy");
			Calendar cal = Calendar.getInstance();
			String nowDate = dateFormat.format(cal.getTime());
			main.setArtwork(art);
			main.setDate(nowDate);
			maintenanceDao.save(main);

			cal.add(Calendar.MONTH, +6);
			String maintenanceDate = dateFormat.format(cal.getTime());
			art.setMaintenanceDate(maintenanceDate);

			artworkDao.save(art);

			return true;
		}
		return false;
	}
}
