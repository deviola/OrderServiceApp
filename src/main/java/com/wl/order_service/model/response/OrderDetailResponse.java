package com.wl.order_service.model.response;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class OrderDetailResponse {
	OrderResponse order;
	List<OrderHistoryResponse> history;
	List<OrderItemHistoryResponse> itemHistory;
}
