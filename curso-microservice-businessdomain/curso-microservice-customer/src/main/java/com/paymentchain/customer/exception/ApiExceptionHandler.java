package com.paymentchain.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.paymentchain.customer.common.StandarizedApiExceptionResponse;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<StandarizedApiExceptionResponse> handleNoContentException2(Exception e) {
		StandarizedApiExceptionResponse response = new StandarizedApiExceptionResponse("Input Output Error", "error-1024", e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.PARTIAL_CONTENT);
	}
}
