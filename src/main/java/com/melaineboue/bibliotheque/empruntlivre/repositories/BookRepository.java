package com.melaineboue.bibliotheque.empruntlivre.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.melaineboue.bibliotheque.empruntlivre.entities.Book;
import com.melaineboue.bibliotheque.empruntlivre.entities.Loan;

public interface BookRepository extends JpaRepository<Book, Integer>{

	void deleteById(String bookId);
	List<Book> findByUserId(int user_id);
	List<Book> findByUserIdAndDeletedFalse(int user_id);
	//List<Book> findByUserIdNotAndDeletedFalse(int user_id);
	List<Book> findByUserIdNotAndStatusAndDeletedFalse(int user_id, String string);

}
