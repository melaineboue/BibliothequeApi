package com.melaineboue.bibliotheque.empruntlivre.controller;

import java.util.Date;
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
	
	//les livres d'un utilisateur
	@ResponseStatus(code = HttpStatus.CREATED)
	@GetMapping(value = "/users/{userId}/books")
	public List<Book> getBooksForUser(@PathVariable("userId") String userId)  throws NumberFormatException, Exception
	{
		int user_id = Integer.parseInt(userId);
		Optional<User> userOptional = userRepository.findById(user_id);
		if(!userOptional.isPresent())
			throw new Exception("User avec userId "+userId+" est introuvable");
		return bookRepository.findByUserIdAndDeletedFalse(user_id);
	}
	
	//Liste des livres disponibles à être prêté (livres disponibles n'appartenant pas à l'utilisateur connecté
	@GetMapping(value = "/users/free/{userId}")
	public List<Book> getLoanableBooks(@PathVariable("userId") String userId) throws NumberFormatException{
		int user_id = Integer.parseInt(userId);
		return bookRepository.findByUserIdNotAndStatusAndDeletedFalse(user_id, "Available");
	}

	//Liste des emprunts en cours de l'utilisateur connecté
	@GetMapping(value = "/users/currentLoan/{userId}")
	public List<Loan> getCurrentLoans(@PathVariable("userId") String userId) throws NumberFormatException{
		int user_id = Integer.parseInt(userId);
		return loanRepository.findByBorrowerIdAndCloseDateNull(user_id);
	}
	
	//Liste des anciens emprunts de l'utilisateur connecté
	@GetMapping(value = "/users/oldLoan/{userId}")
	public List<Loan> getOldLoans(@PathVariable("userId") String userId) throws NumberFormatException{
		int user_id = Integer.parseInt(userId);
		return loanRepository.findByBorrowerIdAndCloseDateNotNull(user_id);
	}
	
	//le 1er livre d'un utilisateur
	@ResponseStatus(code = HttpStatus.CREATED)
	@GetMapping(value = "/users/{userId}/books/{numero}")
	public Book getFirstBookForUser(@PathVariable("userId") String userId, @PathVariable("numero") String numero)  throws NumberFormatException, Exception
	{
		int user_id = Integer.parseInt(userId);
		int numero_ = Integer.parseInt(numero);
		
		Optional<User> userOptional = userRepository.findById(user_id);
		if(!userOptional.isPresent())
			throw new Exception("User avec userId "+userId+" est introuvable");
		List<Book> books = bookRepository.findByUserIdAndDeletedFalse(user_id);
		return (books.size() >=numero_ ) ? books.get(numero_ -1) : null;
	}
	
	
	//suppression d'un livre
	@DeleteMapping(value = "/books/{bookId}")
	public ResponseEntity deleteBook(@PathVariable("bookId") String bookId) throws Exception 
	{
		System.out.println("Delete Book");
		int book_id = Integer.parseInt(bookId);
		List<Loan> loans = loanRepository.findByBookId(book_id);
		for(Loan loan : loans)
		{
			//On ne peut pas supprimer un livre dont l'emprunt est en cours
			if(loan.getCloseDate()==null)
				return new ResponseEntity(HttpStatus.CONFLICT);
		}
		Optional<Book> bookOptional = bookRepository.findById(book_id);
		if(!bookOptional.isPresent())
			throw new Exception("Le livre que vous voulez supprimer n'existe pas");
		Book book = bookOptional.get();
		book.setDeleted(Boolean.TRUE); 
		bookRepository.save(book);
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
		User userRecovered = userRepository.findByEmail(user.getEmail());
		if(userRecovered != null)
			return new ResponseEntity(HttpStatus.CONFLICT);
		userRepository.save(user);
		return  new ResponseEntity(HttpStatus.CREATED);
	}

	//suppression user
	@DeleteMapping(value = "/users/{userId}")
	public ResponseEntity deleteUser(@PathVariable("userId") String userId) {
		
		int user_id = Integer.parseInt(userId);
		List<Loan> loans = loanRepository.findByBorrowerIdOrLenderId(user_id, user_id);
		for(Loan loan : loans)
		{
			//On ne peut pas supprimer un livre dont l'emprunt est en cours
			if(loan.getCloseDate()==null)
				return new ResponseEntity(HttpStatus.CONFLICT);
		}
		userRepository.deleteById(user_id);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	//rechercher user par email
	@GetMapping(value = "/users/{email}")
	@ResponseStatus(code = HttpStatus.OK)
	public User getUser(@PathVariable("email") String email) {
		return userRepository.findByEmail(email);
	}	
	
	//les emprunts
	//add loan
	@GetMapping(value = "/users/{userId}/books/{bookId}/loan")
	public ResponseEntity createLoan(@PathVariable("userId") String userId, @PathVariable("bookId") String bookId) throws Exception
	{
		Loan loan = new Loan();
		int user_id = Integer.parseInt(userId);
		int book_id = Integer.parseInt(bookId); 

		List<Loan> loans = loanRepository.findByBookIdAndBorrowerIdAndCloseDateNull(book_id, user_id);
		if(loans != null && loans.size() > 0)
			return new ResponseEntity(HttpStatus.CONFLICT);
		
		Optional<User> userOptional = userRepository.findById(user_id);
		Optional<Book> bookOptional = bookRepository.findById(book_id);
		if(!userOptional.isPresent() || !bookOptional.isPresent())
			throw new Exception("Le livre ou l'utilisateur n'existe pas");
		
		Book book = bookOptional.get();
		User user = userOptional.get();
		
		book.setStatus(EnumsBookStatus.USED.getCode());
		bookRepository.save(book);
		loan.setAskDate(new Date());
		loan.setBorrower(user);
		loan.setLender(book.getUser());
		loan.setBook(book); 
		loanRepository.save(loan);
		return new ResponseEntity(HttpStatus.CREATED);
	}
	
	@DeleteMapping("/loans/{loanId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteLoan(@PathVariable("loanId") String loanId) throws Exception 
	{
		int loan_id = Integer.parseInt(loanId);
		Optional<Loan> loanOptional = loanRepository.findById(loan_id);
		if(!loanOptional.isPresent())
			throw new Exception("L'emprunt ayant pour id "+ loanId +"n'existe pas ");
		Loan loan = loanOptional.get();
		loan.setCloseDate(new Date());
		Book book = loan.getBook();
		book.setStatus(EnumsBookStatus.AVAILABLE.getCode());
		loanRepository.save(loan);
		bookRepository.save(book);
	}
	
	//tous les emprunts
	@GetMapping(value = "/loans")
	public List<Loan> getLoans() {
		return loanRepository.findAll(); 
	}


	
	
	
	

}
