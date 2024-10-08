package com.wl.order_service.service;

import com.wl.order_service.model.request.OrderRequest;
import com.wl.order_service.model.response.OrderResponse;
import com.wl.order_service.model.OrderStatus;
import com.wl.order_service.model.response.OrderDetailResponse;

import java.util.List;

public interface OrderService {
	OrderResponse createOrder(OrderRequest request);
	OrderResponse getOrderById(Long id);
	List<OrderResponse> getAllOrders();
	OrderResponse updateOrderStatus(Long id, OrderStatus status);
	OrderDetailResponse getOrderDetailsById(Long id);
}
