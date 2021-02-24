package com.paymentchain.transactions.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paymentchain.transactions.entities.Transaction;

public interface TransactionsRepository extends JpaRepository<Transaction, Long>{

}
