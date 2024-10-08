package com.wl.order_service.model.response;

import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;
import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class OrderItemHistoryResponse {
	String operationId;
	Long id;
	Long orderId;
	Long productId;
	Double productPrice;
	Integer quantity;
	String operation;
	LocalDateTime operationDate;
}
