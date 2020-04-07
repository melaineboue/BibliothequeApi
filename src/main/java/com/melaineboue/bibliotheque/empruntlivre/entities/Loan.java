package com.melaineboue.bibliotheque.empruntlivre.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class Loan {
	@Id
	@GeneratedValue
	int id;
	@ManyToOne
	User lender;
	@ManyToOne
	Book book;
	@ManyToOne
	User borrower;
	Date AskDate;
	Date closeDate;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getLender() {
		return lender;
	}
	public void setLender(User lender) {
		this.lender = lender;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public User getBorrower() {
		return borrower;
	}
	public void setBorrower(User borrower) {
		this.borrower = borrower;
	}
	public Date getAskDate() {
		return AskDate;
	}
	public void setAskDate(Date askDate) {
		AskDate = askDate;
	}
	public Date getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}
	
}
