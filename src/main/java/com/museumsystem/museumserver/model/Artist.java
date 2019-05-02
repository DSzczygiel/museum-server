package com.museumsystem.museumserver.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "artist")
public class Artist {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
	
	@OneToMany(mappedBy = "artist")
	private List<ArtistInfo> names = new ArrayList<>();
	
	@OneToMany(mappedBy = "artist")
	private List<Artwork> artworks = new ArrayList<>();
	
	@Column(name = "birth_date")
	private String birthDate;
	
	@Column(name = "death_date")
	private String deathDate;

	@Column(name = "is_active", columnDefinition = "TINYINT(1)")
	private boolean isActive;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<ArtistInfo> getNames() {
		return names;
	}

	public void setNames(List<ArtistInfo> names) {
		this.names = names;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getDeathDate() {
		return deathDate;
	}

	public void setDeathDate(String deathDate) {
		this.deathDate = deathDate;
	}

	public List<Artwork> getArtworks() {
		return artworks;
	}

	public void setArtworks(List<Artwork> artworks) {
		this.artworks = artworks;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
