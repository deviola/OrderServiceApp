package com.wl.order_service.model.request;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class OrderItemRequest {
	Long productId;
	Integer quantity;
}
