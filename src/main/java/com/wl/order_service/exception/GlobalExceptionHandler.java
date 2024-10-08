package com.wl.order_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AppException.class)
	ResponseEntity<ErrorResponse> handleAppException(AppException ex) {
		return toErrorResponse(ex.getHttpStatus(), ex.getMessage());
	}

	private ResponseEntity<ErrorResponse> toErrorResponse(HttpStatus httpStatus, String message) {
		return ResponseEntity.status(httpStatus)
				.body(new ErrorResponse(message));
	}
}

