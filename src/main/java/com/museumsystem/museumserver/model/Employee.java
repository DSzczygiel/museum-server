package com.museumsystem.museumserver.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "employee")
public class Employee extends User implements UserDetails{

	private static final long serialVersionUID = -4174014664026138954L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotBlank(message = "First name cannot be blank")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Column(name = "last_name")
    private String lastName;
    
    @NotBlank(message = "Pesel cannot be blank")
    @Size(min = 11, max = 11, message = "Pesel must contain 11 digits")
    @Column(name = "pesel")
    private String pesel;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email is not valid")
    @Column(name = "email")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Column(name = "password")
    private String password;

	@OneToMany(mappedBy = "assignedEmployee")
	private List<Artwork> artworks;
	
	@OneToMany(mappedBy = "employee")
	private List<ArtworkOperation> operations;
	
	@OneToMany(mappedBy = "employee")
	private List<News> news;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPesel() {
		return pesel;
	}

	public void setPesel(String pesel) {
		this.pesel = pesel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
    public List<Artwork> getArtworks() {
		return artworks;
	}

	public void setArtworks(List<Artwork> artworks) {
		this.artworks = artworks;
	}

	@Override
    public String toString(){
        return "User: "+ this.id + " " + this.firstName + " " + this.lastName +
                 " " + this.email + " " + this.password;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authority = new ArrayList<>();
		authority.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
		
		return authority;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	} 
}
