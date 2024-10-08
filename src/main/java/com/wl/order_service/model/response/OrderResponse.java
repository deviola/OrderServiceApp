package com.wl.order_service.model.response;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class OrderResponse {
	Long id;
	LocalDateTime orderDate;
	String status;
	List<OrderItemResponse> items;
}

