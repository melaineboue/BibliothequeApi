package com.melaineboue.bibliotheque.empruntlivre.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

@Entity
@TableGenerator(name = "idGenerator", initialValue = 2)
public class User {
	@Id
	@GeneratedValue(generator = "idGenerator")
	int id;
	String firstname; 
	String lastname;
	String email;
	String password;
	
	public int getId() {
		return id;
	}
	public void setId(int id) { 
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
