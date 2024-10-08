package com.wl.order_service.model;

import com.wl.order_service.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "order_items")
@Data
@FieldDefaults(level = PRIVATE)
public class OrderItem extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	Order order;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id", nullable = false)
	Product product;

	@Column(nullable = false)
	Integer quantity;

	@Column(nullable = false)
	Double productPrice;
}

