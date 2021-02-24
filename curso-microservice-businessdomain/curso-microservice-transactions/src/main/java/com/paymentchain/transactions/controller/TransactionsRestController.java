package com.paymentchain.transactions.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paymentchain.transactions.entities.Transaction;
import com.paymentchain.transactions.repository.TransactionsRepository;

@RestController
@RequestMapping("/transactions")
public class TransactionsRestController {

	@Autowired
	TransactionsRepository transactionsRepository;
	
	@GetMapping("/")
	public List<Transaction> list(){
		return transactionsRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public Transaction get(@PathVariable String id) {
		return null;
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> put(@PathVariable String id, @RequestBody Transaction input) {
		return null;
	}
	
	@PostMapping("/")
	public ResponseEntity<?> post(@RequestBody Transaction input) {
		Transaction transactions = transactionsRepository.save(input);
		return ResponseEntity.ok(transactions);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) {
		return null;
	}
	
}
