package com.melaineboue.bibliotheque.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.melaineboue.bibliotheque.empruntlivre.entities.Loan;

public interface LoanRepository extends JpaRepository<Loan, Integer>{

}
