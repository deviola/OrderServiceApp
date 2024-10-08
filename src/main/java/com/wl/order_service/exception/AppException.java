package com.wl.order_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException {
	private final HttpStatus httpStatus;

	public AppException(AppExceptionMessage exceptionMessage, Object... messageArgs) {
		super(String.format(exceptionMessage.getMessageFormat(), messageArgs));
		this.httpStatus = exceptionMessage.getHttpStatus();
	}

	public AppException(HttpStatus httpStatus, String message) {
		super(message);
		this.httpStatus = httpStatus;
	}
}

