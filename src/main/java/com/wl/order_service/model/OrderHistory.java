package com.wl.order_service.model;

import com.wl.order_service.model.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "orders_history")
@Data
@FieldDefaults(level = PRIVATE)
public class OrderHistory {

	@Id
	@Column(name = "operation_id")
	String operationId;

	@Column(name = "id")
	Long id;

	@Column(name = "product_id")
	Long productId;

	@Column(name = "product_price")
	Double productPrice;

	@Column(name = "quantity")
	Integer quantity;

	@Column(name = "order_date")
	LocalDateTime orderDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	OrderStatus status;

	@Column(name = "created_at")
	LocalDateTime createdAt;

	@Column(name = "modified_at")
	LocalDateTime modifiedAt;

	@Column(name = "operation")
	String operation;

	@Column(name = "operation_date")
	LocalDateTime operationDate;
}

