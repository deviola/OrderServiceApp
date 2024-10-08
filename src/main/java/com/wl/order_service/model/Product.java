package com.wl.order_service.model;

import com.wl.order_service.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Data
@Table(name = "products")
@FieldDefaults(level = PRIVATE)
public class Product extends BaseEntity {

	@Column(name = "name", nullable = false)
	String name;

	@Column(name = "price", nullable = false)
	Double price;

	@Column(name = "stock", nullable = false)
	Integer stock;
}