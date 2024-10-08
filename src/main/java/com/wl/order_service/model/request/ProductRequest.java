package com.wl.order_service.model.request;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class ProductRequest {
	String name;
	Double price;
	Integer stock;
}
