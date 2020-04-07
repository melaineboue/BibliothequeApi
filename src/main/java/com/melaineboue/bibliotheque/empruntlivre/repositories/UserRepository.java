package com.melaineboue.bibliotheque.empruntlivre.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.melaineboue.bibliotheque.empruntlivre.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	List<User> findByEmail(String email);

}
