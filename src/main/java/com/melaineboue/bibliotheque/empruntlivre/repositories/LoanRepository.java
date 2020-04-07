package com.melaineboue.bibliotheque.empruntlivre.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.melaineboue.bibliotheque.empruntlivre.entities.Loan;

public interface LoanRepository extends JpaRepository<Loan, Integer>{

	public List<Loan> findByBookId(int bookId);

}
