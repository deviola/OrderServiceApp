package com.wl.order_service.model.response;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class OrderItemResponse {
	Long productId;
	String productName;
	Integer quantity;
	Double productPrice;
}
