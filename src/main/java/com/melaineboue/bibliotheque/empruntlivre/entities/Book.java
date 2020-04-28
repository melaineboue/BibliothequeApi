package com.melaineboue.bibliotheque.empruntlivre.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Data
@TableGenerator(name = "idBookGenerator", initialValue = 4)
public class Book{
	
	@Id
	@GeneratedValue(generator = "idBookGenerator")
	int id;
	@Size(min = 1, max = 75, message = "La taille doit être au moins 1 et au plus 75")
	String name;
	String status;
	String category;
	Boolean deleted;
	@ManyToOne
	User user;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
