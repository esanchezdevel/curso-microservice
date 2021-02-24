package com.paymentchain.products.controller;

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

import com.paymentchain.products.entities.Products;
import com.paymentchain.products.repository.ProductsRepository;

@RestController
@RequestMapping("/products")
public class ProductsRestController {

    @Autowired
    ProductsRepository productsRepository;
          
     @GetMapping()
    public List<Products> list() {
        return productsRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public Products get(@PathVariable long id) {   
        Products customer = productsRepository.findById(id).get();         
        return customer;   
    }  
   
    
    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable String id, @RequestBody Products input) {
        return null;
    }
    
   @PostMapping
    public ResponseEntity<?> post(@RequestBody Products input) {       
        Products save = productsRepository.save(input);
        return ResponseEntity.ok(save);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return null;
    }
	
}
