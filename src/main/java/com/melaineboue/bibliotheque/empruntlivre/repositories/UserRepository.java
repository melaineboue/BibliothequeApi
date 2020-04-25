package com.melaineboue.bibliotheque.empruntlivre.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.melaineboue.bibliotheque.empruntlivre.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	User findByEmail(String email);
	//List<User> findByEmail(String email);
	void deleteById(String userId);

}
