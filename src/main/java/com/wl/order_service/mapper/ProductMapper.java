package com.wl.order_service.mapper;

import com.wl.order_service.model.Product;
import com.wl.order_service.model.request.ProductRequest;
import com.wl.order_service.model.response.ProductResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper {

	public ProductResponse toResponse(Product product) {
		ProductResponse response = new ProductResponse();
		response.setId(product.getId());
		response.setName(product.getName());
		response.setPrice(product.getPrice());
		response.setStock(product.getStock());
		return response;
	}

	public List<ProductResponse> toResponseList(List<Product> products) {
		return products.stream()
				.map(this::toResponse)
				.toList();
	}

	public Product toEntity(ProductRequest request) {
		Product product = new Product();
		product.setName(request.getName());
		product.setPrice(request.getPrice());
		product.setStock(request.getStock());
		return product;
	}

	public void updateEntity(ProductRequest request, Product product) {
		if (request.getName() != null) {
			product.setName(request.getName());
		}

		if (request.getPrice() != null) {
			product.setPrice(request.getPrice());
		}

		if (request.getStock() != null) {
			product.setStock(request.getStock());
		}
	}

}

