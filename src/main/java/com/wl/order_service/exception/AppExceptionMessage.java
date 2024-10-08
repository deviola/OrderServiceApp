package com.wl.order_service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AppExceptionMessage {

	PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "Product with ID '%s' not found"),
	ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "Order with ID '%s' not found"),
	INVALID_PRODUCT_PRICE(HttpStatus.BAD_REQUEST, "Invalid price '%s' for product. Price must be positive."),
	OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "Product with ID '%s' is out of stock."),
	PRICE_LESS_THAN_ZERO(HttpStatus.BAD_REQUEST, "Product price cannot be less than 0"),
	STOCK_QUANTITY_LESS_THAN_ZERO(HttpStatus.BAD_REQUEST, "Stock quantity cannot be less than 0"),
	EMPTY_REQUEST_PARAMS(HttpStatus.BAD_REQUEST, "Request parameters cannot be empty"),
	PRODUCT_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Product with name '%s' and price '%s' already exists"),
	ORDER_STATUS_SAME(HttpStatus.BAD_REQUEST, "Order is already in status '%s'"),
	PRODUCT_ORDER_QUANTITY(HttpStatus.BAD_REQUEST, "Product quantity in order cannot be less than 1"),
	INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "Not enough products in stock to make an order"),
	ORDER_COMPLETED(HttpStatus.BAD_REQUEST, "Order has been already completed, cannot change status"),
	INVALID_ORDER_STATUS_TRANSITION(HttpStatus.BAD_REQUEST, "Cannot change order status from '%s' to '%s'");

	private final HttpStatus httpStatus;
	private final String messageFormat;
	}

