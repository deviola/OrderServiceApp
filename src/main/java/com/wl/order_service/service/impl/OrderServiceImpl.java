package com.wl.order_service.service.impl;

import com.wl.order_service.exception.AppException;
import com.wl.order_service.exception.AppExceptionMessage;
import com.wl.order_service.mapper.OrderMapper;
import com.wl.order_service.model.Order;
import com.wl.order_service.model.request.OrderRequest;
import com.wl.order_service.model.response.OrderResponse;
import com.wl.order_service.model.OrderStatus;
import com.wl.order_service.mapper.OrderHistoryMapper;
import com.wl.order_service.validator.OrderValidator;
import com.wl.order_service.repository.OrderHistoryRepository;
import com.wl.order_service.mapper.OrderItemHistoryMapper;
import com.wl.order_service.repository.OrderItemHistoryRepository;
import com.wl.order_service.model.response.OrderDetailResponse;
import com.wl.order_service.model.OrderHistory;
import com.wl.order_service.model.OrderItemHistory;
import com.wl.order_service.model.OrderItem;
import com.wl.order_service.model.request.OrderItemRequest;
import com.wl.order_service.repository.OrderRepository;
import com.wl.order_service.repository.ProductRepository;
import com.wl.order_service.model.Product;
import com.wl.order_service.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final OrderHistoryRepository orderHistoryRepository;
	private final ProductRepository productRepository;
	private final OrderValidator validator;
	private final OrderMapper orderMapper;
	private final OrderHistoryMapper orderHistoryMapper;
	private final OrderItemHistoryMapper orderItemHistoryMapper;
	private final OrderItemHistoryRepository orderItemHistoryRepository;

	@Override
	public OrderResponse createOrder(OrderRequest request) {
		if (request.getItems() == null || request.getItems().isEmpty()) {
			throw new AppException(AppExceptionMessage.EMPTY_REQUEST_PARAMS);
		}

		for (OrderItemRequest itemRequest : request.getItems()) {
			Product product = productRepository.findById(itemRequest.getProductId())
					.orElseThrow(() -> new AppException(AppExceptionMessage.PRODUCT_NOT_FOUND, itemRequest.getProductId()));

			validator.validateProductOrderItemRequest(itemRequest, product);
		}

		Order order = orderMapper.toEntity(request);
		Order savedOrder = orderRepository.save(order);
		updateProductInStock(savedOrder);

		return orderMapper.toResponse(savedOrder);
	}

	@Override
	public OrderResponse getOrderById(Long id) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new AppException(AppExceptionMessage.ORDER_NOT_FOUND, id));
		return orderMapper.toResponse(order);
	}

	@Override
	public List<OrderResponse> getAllOrders() {
		return orderMapper.toResponseList(orderRepository.findAll());
	}

	@Override
	public OrderResponse updateOrderStatus(Long id, OrderStatus newStatus) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new AppException(AppExceptionMessage.ORDER_NOT_FOUND, id));
		validator.validateStatusBeforeChange(order.getStatus(), newStatus);
		order.setStatus(newStatus);
		return orderMapper.toResponse(orderRepository.save(order));
	}

	@Override
	public OrderDetailResponse getOrderDetailsById(Long id) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new AppException(AppExceptionMessage.ORDER_NOT_FOUND, id));

		List<OrderHistory> historyList = orderHistoryRepository.findByIdOrderByOperationDateDesc(id);
		List<OrderItemHistory> itemHistoryList = orderItemHistoryRepository.findByOrderIdOrderByOperationDateDesc(id);

		OrderDetailResponse detailResponse = new OrderDetailResponse();
		detailResponse.setOrder(orderMapper.toResponse(order));
		detailResponse.setHistory(orderHistoryMapper.toResponseList(historyList));
		detailResponse.setItemHistory(orderItemHistoryMapper.toResponseList(itemHistoryList));

		return detailResponse;
	}

	private void updateProductInStock(Order savedOrder) {
		for (OrderItem item : savedOrder.getItems()) {
			Product product = item.getProduct();
			int newStock = product.getStock() - item.getQuantity();
			if (newStock < 0) {
				throw new AppException(AppExceptionMessage.INSUFFICIENT_STOCK);
			}
			product.setStock(newStock);
			productRepository.save(product);
		}
	}
}
