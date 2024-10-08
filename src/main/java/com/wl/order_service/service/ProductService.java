package com.wl.order_service.service;

import com.wl.order_service.model.request.ProductRequest;
import com.wl.order_service.model.response.ProductResponse;

import java.util.List;

public interface ProductService {
	ProductResponse createProduct(ProductRequest request);
	ProductResponse getProductById(Long id);
	List<ProductResponse> getAllProducts();
	ProductResponse updateProduct(Long id, ProductRequest request);
}
