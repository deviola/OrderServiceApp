package com.wl.order_service.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
class ErrorResponse {

	private String message;

	ErrorResponse(String message) {
		this.message = message;
	}
}

