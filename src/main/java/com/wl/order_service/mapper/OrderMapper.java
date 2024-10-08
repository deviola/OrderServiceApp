package com.wl.order_service.mapper;

import com.wl.order_service.exception.AppException;
import com.wl.order_service.exception.AppExceptionMessage;
import com.wl.order_service.model.Order;
import com.wl.order_service.model.request.OrderRequest;
import com.wl.order_service.model.response.OrderResponse;
import com.wl.order_service.model.OrderStatus;
import com.wl.order_service.model.OrderItem;
import com.wl.order_service.model.request.OrderItemRequest;
import com.wl.order_service.model.response.OrderItemResponse;
import com.wl.order_service.repository.ProductRepository;
import com.wl.order_service.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {

	private final ProductRepository productRepository;

	public OrderResponse toResponse(Order order) {
		OrderResponse response = new OrderResponse();
		response.setId(order.getId());
		response.setOrderDate(order.getOrderDate());
		response.setStatus(order.getStatus().name());

		List<OrderItemResponse> itemResponses = order.getItems().stream()
				.map(this::toItemResponse)
				.toList();

		response.setItems(itemResponses);

		return response;
	}

	public List<OrderResponse> toResponseList(List<Order> orders) {
		return orders.stream()
				.map(this::toResponse)
				.toList();
	}

	private OrderItemResponse toItemResponse(OrderItem item) {
		OrderItemResponse response = new OrderItemResponse();
		response.setProductId(item.getProduct().getId());
		response.setProductName(item.getProduct().getName());
		response.setQuantity(item.getQuantity());
		response.setProductPrice(item.getProductPrice());
		return response;
	}

	public Order toEntity(OrderRequest request) {
		Order order = new Order();
		order.setOrderDate(LocalDateTime.now());
		order.setStatus(OrderStatus.CREATED);

		List<OrderItem> items = request.getItems().stream()
				.map(this::toItemEntity)
				.toList();

		for (OrderItem item : items) {
			item.setOrder(order);
		}
		order.setItems(items);

		return order;
	}

	private OrderItem toItemEntity(OrderItemRequest itemRequest) {
		Product product = productRepository.findById(itemRequest.getProductId())
				.orElseThrow(() -> new AppException(AppExceptionMessage.PRODUCT_NOT_FOUND, itemRequest.getProductId()));

		OrderItem item = new OrderItem();
		item.setProduct(product);
		item.setQuantity(itemRequest.getQuantity());
		item.setProductPrice(product.getPrice());

		return item;
	}
}

