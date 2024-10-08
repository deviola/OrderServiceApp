package com.wl.order_service.model.request;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class OrderRequest {
	List<OrderItemRequest> items;
}
