package com.museumsystem.museumserver.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "artwork")
public class Artwork {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
	
	@OneToMany(mappedBy = "artwork")
	private List<ArtworkInfo> titles;
	
	@ManyToOne
	@JoinColumn(name = "artist_id")
	private Artist artist;
	
	@Column(name = "cretion_year")
	private Integer creationYear;
	
	@Column(name = "photo", columnDefinition = "MEDIUMBLOB")
	private byte[] photo;
	
	@Column(name = "maintenance_date")
	private String maintenanceDate;
	
	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee assignedEmployee;
	
	@OneToMany(mappedBy = "artwork")
	private List<ArtworkOperation> operations;
	
	@OneToMany(mappedBy = "artwork")
	private List<Maintenance> maintenances;
	
	@Column(name = "is_active", columnDefinition = "TINYINT(1)")
	private boolean isActive;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<ArtworkInfo> getTitles() {
		return titles;
	}

	public void setTitles(List<ArtworkInfo> titles) {
		this.titles = titles;
	}

	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}

	public Integer getCreationYear() {
		return creationYear;
	}

	public void setCreationYear(Integer creationYear) {
		this.creationYear = creationYear;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getMaintenanceDate() {
		return maintenanceDate;
	}

	public void setMaintenanceDate(String maintenanceDate) {
		this.maintenanceDate = maintenanceDate;
	}

	public Employee getAssignedEmployee() {
		return assignedEmployee;
	}

	public void setAssignedEmployee(Employee assignedEmployee) {
		this.assignedEmployee = assignedEmployee;
	}

	public List<ArtworkOperation> getOperations() {
		return operations;
	}

	public void setOperations(List<ArtworkOperation> operations) {
		this.operations = operations;
	}

	public List<Maintenance> getMaintenances() {
		return maintenances;
	}

	public void setMaintenances(List<Maintenance> maintenances) {
		this.maintenances = maintenances;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
