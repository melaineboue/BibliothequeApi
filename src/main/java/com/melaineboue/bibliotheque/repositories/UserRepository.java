package com.melaineboue.bibliotheque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.melaineboue.bibliotheque.empruntlivre.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{

}
