package com.museumsystem.museumserver.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonValue;

@Entity
@Table(name="customer")
public class Customer extends User implements UserDetails{

	private static final long serialVersionUID = -1423389057492902552L;

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

    @JsonValue
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email is not valid")
    @Column(name = "email")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Column(name = "password")
    private String password;

	@NotNull(message = "Status cannot be null")
    @Column(name = "is_active")
    private boolean isActive;
    
	@NotNull(message = "Date cannot be null")
	@Column(name = "register_date")
	private String registerDate;

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
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
    	System.out.println("USER PASS");
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
	    
    @Override
    public String toString(){
        return "User: "+ this.id + " " + this.firstName + " " + this.lastName +
                 " " + this.email + " " + this.password + " " + this.isActive;
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authority = new ArrayList<>();
		authority.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
		
		return authority;
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
		return this.isActive();
	}

	@Override
	public String getUsername() {
    	System.out.println("usergetusername");

		return this.getEmail();
	}
}
