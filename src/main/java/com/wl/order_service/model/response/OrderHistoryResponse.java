package com.wl.order_service.model.response;

import com.wl.order_service.model.OrderStatus;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class OrderHistoryResponse {
	String operationId;
	Long id;
	LocalDateTime orderDate;
	OrderStatus status;
	LocalDateTime createdAt;
	LocalDateTime modifiedAt;
	String operation;
	LocalDateTime operationDate;
}

