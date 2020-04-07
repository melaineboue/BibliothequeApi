package com.melaineboue.bibliotheque.empruntlivre.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.melaineboue.bibliotheque.empruntlivre.Enums.EnumsBookStatus;
import com.melaineboue.bibliotheque.empruntlivre.Exceptions.ExceptionFormatIncorrect;
import com.melaineboue.bibliotheque.empruntlivre.entities.Book;
import com.melaineboue.bibliotheque.empruntlivre.entities.Loan;
import com.melaineboue.bibliotheque.empruntlivre.entities.User;
import com.melaineboue.bibliotheque.empruntlivre.repositories.BookRepository;
import com.melaineboue.bibliotheque.empruntlivre.repositories.LoanRepository;
import com.melaineboue.bibliotheque.empruntlivre.repositories.UserRepository;

@RestController
public class ApiController {
	
	@Autowired
	BookRepository bookRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	LoanRepository loanRepository;
	
	@GetMapping(value = "/isAlive")
	public boolean isAlive() {
		return Boolean.TRUE;
	}
	
	//Book
	//liste des livres
	@GetMapping(value = "/books")
	public List<Book> getBooks() {
		return bookRepository.findAll();
	}

	//Creation d'un livre utilisateur
	@ResponseStatus(code = HttpStatus.CREATED)
	@PostMapping(value = "/users/{userId}/book")
	public void createBookForUser(@RequestBody Book book, @PathVariable("userId") String userId)  throws NumberFormatException, Exception
	{
		if(book ==null)
			throw new ExceptionFormatIncorrect("Book ne doit pas être vide");
		int user_id = Integer.parseInt(userId);
		Optional<User> userOptional = userRepository.findById(user_id);
		if(!userOptional.isPresent())
			throw new Exception("User avec userId "+userId+" est introuvable");
		User user = userOptional.get();
		book.setUser(user);
		book.setStatus(EnumsBookStatus.AVAILABLE.getCode());
		book.setDeleted(false);
		bookRepository.save(book);
	}
	
	//suppression d'un livre
	@DeleteMapping(value = "/books/{bookId}/delete")
	public ResponseEntity deleteBook(@PathVariable("bookId") String bookId) throws NumberFormatException
	{
		int book_id = Integer.parseInt(bookId);
		List<Loan> loans = loanRepository.findByBookId(book_id);
		for(Loan loan : loans)
		{
			//On ne peut pas supprimer un livre dont l'emprunt est en cours
			if(loan.getCloseDate()==null)
				return new ResponseEntity(HttpStatus.CONFLICT);
		}
		bookRepository.deleteById(bookId);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	//User
	//liste des users
	@GetMapping(value = "/users")
	@ResponseStatus(code = HttpStatus.OK)
	public List<User> getUsers() {
		return userRepository.findAll();
	}
	
	//ajout user
	@PostMapping(value = "/users")
	public ResponseEntity createUser(@RequestBody User user) {
		List<User> users = userRepository.findByEmail(user.getEmail());
		if(users != null || users.size() > 0)
			return new ResponseEntity(HttpStatus.CONFLICT);
		userRepository.save(user);
		return  new ResponseEntity(HttpStatus.CREATED);
	}

	//suppression user
	
	//rechercher user par email
	
	
	
	

}
