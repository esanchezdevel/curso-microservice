package com.paymentchain.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.paymentchain.customer.entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{

	@Query(value = "SELECT c FROM Customer c WHERE c.code = ?1")
	public Customer findByCode(String code);
}
