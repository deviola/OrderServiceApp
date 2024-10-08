package com.wl.order_service.model;

import com.wl.order_service.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Data
@Table(name = "orders")
@FieldDefaults(level = PRIVATE)
public class Order extends BaseEntity {

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	List<OrderItem> items;

	@Column(name = "order_date", nullable = false)
	LocalDateTime orderDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	OrderStatus status;
}
