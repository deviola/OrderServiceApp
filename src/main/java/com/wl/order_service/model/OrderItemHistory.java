package com.wl.order_service.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "order_items_history")
@Data
@FieldDefaults(level = PRIVATE)
public class OrderItemHistory {

	@Id
	@Column(name = "operation_id")
	String operationId;

	@Column(name = "id")
	Long id;

	@Column(name = "order_id")
	Long orderId;

	@Column(name = "product_id")
	Long productId;

	@Column(name = "product_price")
	Double productPrice;

	@Column(name = "quantity")
	Integer quantity;

	@Column(name = "operation")
	String operation;

	@Column(name = "operation_date")
	LocalDateTime operationDate;
}

