package com.wl.order_service.controller;

import com.wl.order_service.model.request.OrderRequest;
import com.wl.order_service.model.response.OrderResponse;
import com.wl.order_service.model.OrderStatus;
import com.wl.order_service.service.OrderService;
import com.wl.order_service.model.response.OrderDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@GetMapping
	@Operation(summary = "Get all orders")
	public ResponseEntity<List<OrderResponse>> getAllOrders() {
		return ResponseEntity.ok(orderService.getAllOrders());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get order by ID with current information")
	public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
		return ResponseEntity.ok(orderService.getOrderById(id));
	}

	@GetMapping("/{id}/details")
	@Operation(summary = "Get order by ID with detailed information")
	public ResponseEntity<OrderDetailResponse> getOrderDetails(@PathVariable Long id) {
		return ResponseEntity.ok(orderService.getOrderDetailsById(id));
	}

	@PostMapping
	@Operation(summary = "Create order")
	public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
	}

	@PutMapping("/{id}/confirm")
	@Operation(summary = "Confirm order")
	public ResponseEntity<OrderResponse> confirmOrder(@PathVariable Long id) {
		return ResponseEntity.ok(orderService.updateOrderStatus(id, OrderStatus.CONFIRMED));
	}

	@PutMapping("/{id}/complete")
	@Operation(summary = "Complete order")
	public ResponseEntity<OrderResponse> completeOrder(@PathVariable Long id) {
		return ResponseEntity.ok(orderService.updateOrderStatus(id, OrderStatus.COMPLETED));
	}

	@PutMapping("/{id}/cancel")
	@Operation(summary = "Cancel order")
	public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long id) {
		return ResponseEntity.ok(orderService.updateOrderStatus(id, OrderStatus.CANCELLED));
	}

}
