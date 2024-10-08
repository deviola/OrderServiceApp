package com.wl.order_service.model.response;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class ProductResponse {
	Long id;
	String name;
	Double price;
	Integer stock;
}

