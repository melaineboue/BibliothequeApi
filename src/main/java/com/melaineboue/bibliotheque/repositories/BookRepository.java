package com.melaineboue.bibliotheque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.melaineboue.bibliotheque.empruntlivre.entities.Book;

public interface BookRepository extends JpaRepository<Book, Integer>{

}
