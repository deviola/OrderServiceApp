package com.wl.order_service.repository;

import com.wl.order_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	boolean existsByNameIgnoreCaseAndPrice(String name, Double price);
}
