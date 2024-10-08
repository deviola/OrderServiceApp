package com.wl.order_service.controller;

import com.wl.order_service.model.request.ProductRequest;
import com.wl.order_service.model.response.ProductResponse;
import com.wl.order_service.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@GetMapping
	@Operation(summary = "Get all products")
	public ResponseEntity<List<ProductResponse>> getAllProducts() {
		return ResponseEntity.ok(productService.getAllProducts());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get product by ID")
	public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
		return ResponseEntity.ok(productService.getProductById(id));
	}

	@PostMapping
	@Operation(summary = "Create product")
	public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update product")
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
		return ResponseEntity.ok(productService.updateProduct(id, request));
	}

}