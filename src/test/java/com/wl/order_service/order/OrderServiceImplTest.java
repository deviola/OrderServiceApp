package com.wl.order_service.order;

import com.wl.order_service.exception.AppException;
import com.wl.order_service.exception.AppExceptionMessage;
import com.wl.order_service.mapper.OrderMapper;
import com.wl.order_service.model.Order;
import com.wl.order_service.model.request.OrderRequest;
import com.wl.order_service.model.response.OrderResponse;
import com.wl.order_service.model.OrderStatus;
import com.wl.order_service.model.OrderItem;
import com.wl.order_service.model.request.OrderItemRequest;
import com.wl.order_service.model.response.OrderItemResponse;
import com.wl.order_service.repository.OrderRepository;
import com.wl.order_service.repository.ProductRepository;
import com.wl.order_service.model.Product;
import com.wl.order_service.service.impl.OrderServiceImpl;
import com.wl.order_service.validator.OrderValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private OrderValidator orderValidator;

	@Mock
	private OrderMapper orderMapper;

	@InjectMocks
	private OrderServiceImpl orderService;

	@Test
	@DisplayName("Positive case: Successfully create a new order with multiple items")
	public void testCreateOrder_Success() {
		OrderItemRequest itemRequest1 = createOrderItemRequest(1L, 2);
		OrderItemRequest itemRequest2 = createOrderItemRequest(2L, 3);
		OrderRequest request = createOrderRequest(Arrays.asList(itemRequest1, itemRequest2));

		Product product1 = createProduct(1L, "Product A", 5, 100.0);
		Product product2 = createProduct(2L, "Product B", 10, 50.0);

		OrderItem item1 = createOrderItem(product1, itemRequest1.getQuantity(), product1.getPrice());
		OrderItem item2 = createOrderItem(product2, itemRequest2.getQuantity(), product2.getPrice());
		Order order = createOrder(1L, Arrays.asList(item1, item2), OrderStatus.CREATED);
		OrderResponse response = createOrderResponse(order);

		when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
		when(productRepository.findById(2L)).thenReturn(Optional.of(product2));
		doNothing().when(orderValidator).validateProductOrderItemRequest(any(OrderItemRequest.class), any(Product.class));
		when(orderMapper.toEntity(request)).thenReturn(order);
		when(orderRepository.save(order)).thenReturn(order);
		when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
		when(orderMapper.toResponse(order)).thenReturn(response);

		OrderResponse result = orderService.createOrder(request);

		assertOrderResponseEquals(response, result);

		verify(productRepository, times(1)).findById(1L);
		verify(productRepository, times(1)).findById(2L);
		verify(orderValidator, times(2)).validateProductOrderItemRequest(any(OrderItemRequest.class), any(Product.class));
		verify(orderMapper, times(1)).toEntity(request);
		verify(orderRepository, times(1)).save(order);
		verify(productRepository, times(2)).save(any(Product.class));
		verify(orderMapper, times(1)).toResponse(order);
	}


	@Test
	@DisplayName("Positive case: Successfully confirm an order")
	public void testUpdateOrderStatusToConfirmed_Success() {
		Long orderId = 1L;
		OrderStatus newStatus = OrderStatus.CONFIRMED;

		Order existingOrder = createOrder(orderId, Collections.emptyList(), OrderStatus.CREATED);
		OrderResponse response = createOrderResponse(existingOrder);

		when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
		doNothing().when(orderValidator).validateStatusBeforeChange(existingOrder.getStatus(), newStatus);
		when(orderRepository.save(existingOrder)).thenReturn(existingOrder);
		when(orderMapper.toResponse(existingOrder)).thenReturn(response);

		OrderResponse result = orderService.updateOrderStatus(orderId, newStatus);

		assertEquals(newStatus, existingOrder.getStatus());
		response.setStatus(newStatus.name());

		assertOrderResponseEquals(response, result);

		verify(orderRepository, times(1)).findById(orderId);
		verify(orderValidator, times(1)).validateStatusBeforeChange(OrderStatus.CREATED, newStatus);
		verify(orderRepository, times(1)).save(existingOrder);
		verify(orderMapper, times(1)).toResponse(existingOrder);
	}

	@Test
	@DisplayName("Positive case: Successfully cancel an order")
	public void testUpdateOrderStatusToCanceled_Success() {
		Long orderId = 1L;
		OrderStatus newStatus = OrderStatus.CANCELLED;

		Order existingOrder = createOrder(orderId, Collections.emptyList(), OrderStatus.CREATED);
		OrderResponse response = createOrderResponse(existingOrder);

		when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
		doNothing().when(orderValidator).validateStatusBeforeChange(existingOrder.getStatus(), newStatus);
		when(orderRepository.save(existingOrder)).thenReturn(existingOrder);
		when(orderMapper.toResponse(existingOrder)).thenReturn(response);

		OrderResponse result = orderService.updateOrderStatus(orderId, newStatus);

		assertEquals(newStatus, existingOrder.getStatus());
		response.setStatus(newStatus.name());

		assertOrderResponseEquals(response, result);

		verify(orderRepository, times(1)).findById(orderId);
		verify(orderValidator, times(1)).validateStatusBeforeChange(OrderStatus.CREATED, newStatus);
		verify(orderRepository, times(1)).save(existingOrder);
		verify(orderMapper, times(1)).toResponse(existingOrder);
	}

	@Test
	@DisplayName("Expected error: Confirming a canceled order")
	public void testUpdateOrderStatus_ConfirmCanceledOrder_ThrowsException() {
		Long orderId = 1L;
		OrderStatus currentStatus = OrderStatus.CANCELLED;
		OrderStatus newStatus = OrderStatus.CONFIRMED;

		Order existingOrder = createOrder(orderId, Collections.emptyList(), currentStatus);

		when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
		doThrow(new AppException(AppExceptionMessage.INVALID_ORDER_STATUS_TRANSITION, currentStatus, newStatus))
				.when(orderValidator).validateStatusBeforeChange(currentStatus, newStatus);

		AppException exception = assertThrows(AppException.class, () -> orderService.updateOrderStatus(orderId, newStatus));

		assertNotNull(exception);
		assertEquals(String.format("Cannot change order status from '%s' to '%s'", currentStatus, newStatus), exception.getMessage());

		verify(orderRepository, times(1)).findById(orderId);
		verify(orderValidator, times(1)).validateStatusBeforeChange(currentStatus, newStatus);
		verify(orderRepository, never()).save(any(Order.class));
		verifyNoInteractions(orderMapper);
	}

	@Test
	@DisplayName("Expected error: Creating an order with insufficient product stock")
	public void testCreateOrder_InsufficientStock_ThrowsException() {
		OrderItemRequest itemRequest = createOrderItemRequest(1L, 15);
		OrderRequest request = createOrderRequest(Collections.singletonList(itemRequest));
		Product product = createProduct(1L, "Product A", 10, 100.0);

		when(productRepository.findById(itemRequest.getProductId())).thenReturn(Optional.of(product));
		doThrow(new AppException(AppExceptionMessage.INSUFFICIENT_STOCK))
				.when(orderValidator).validateProductOrderItemRequest(itemRequest, product);

		AppException exception = assertThrows(AppException.class, () -> orderService.createOrder(request));

		assertNotNull(exception);
		assertEquals("Not enough products in stock to make an order", exception.getMessage());

		verify(productRepository, times(1)).findById(itemRequest.getProductId());
		verify(orderValidator, times(1)).validateProductOrderItemRequest(itemRequest, product);
		verifyNoInteractions(orderMapper);
		verifyNoMoreInteractions(orderRepository);
	}

	@Test
	@DisplayName("Expected error: Changing order status to the same status")
	public void testUpdateOrderStatus_SameStatus_ThrowsException() {
		Long orderId = 1L;
		OrderStatus currentStatus = OrderStatus.CREATED;
		OrderStatus newStatus = OrderStatus.CREATED;

		Order existingOrder = createOrder(orderId, Collections.emptyList(), currentStatus);

		when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
		doThrow(new AppException(AppExceptionMessage.ORDER_STATUS_SAME, currentStatus))
				.when(orderValidator).validateStatusBeforeChange(currentStatus, newStatus);

		AppException exception = assertThrows(AppException.class, () -> orderService.updateOrderStatus(orderId, newStatus));

		assertNotNull(exception);
		assertEquals(String.format("Order is already in status '%s'", currentStatus), exception.getMessage());

		verify(orderRepository, times(1)).findById(orderId);
		verify(orderValidator, times(1)).validateStatusBeforeChange(currentStatus, newStatus);
		verifyNoMoreInteractions(orderRepository);
		verifyNoInteractions(orderMapper);
	}

	@Test
	@DisplayName("Expected error: Creating an order with zero quantity")
	public void testCreateOrder_ZeroQuantity_ThrowsException() {
		OrderItemRequest itemRequest = createOrderItemRequest(1L, 0);
		OrderRequest request = createOrderRequest(Collections.singletonList(itemRequest));
		Product product = createProduct(1L, "Product A", 10, 100.0);

		when(productRepository.findById(itemRequest.getProductId())).thenReturn(Optional.of(product));
		doThrow(new AppException(AppExceptionMessage.STOCK_QUANTITY_LESS_THAN_ZERO))
				.when(orderValidator).validateProductOrderItemRequest(itemRequest, product);

		AppException exception = assertThrows(AppException.class, () -> orderService.createOrder(request));

		assertNotNull(exception);
		assertEquals("Stock quantity cannot be less than 0", exception.getMessage());

		verify(productRepository, times(1)).findById(itemRequest.getProductId());
		verify(orderValidator, times(1)).validateProductOrderItemRequest(itemRequest, product);
		verifyNoInteractions(orderMapper);
		verifyNoMoreInteractions(orderRepository);
	}

	private OrderItemRequest createOrderItemRequest(Long productId, Integer quantity) {
		OrderItemRequest itemRequest = new OrderItemRequest();
		itemRequest.setProductId(productId);
		itemRequest.setQuantity(quantity);
		return itemRequest;
	}

	private OrderRequest createOrderRequest(List<OrderItemRequest> itemRequests) {
		OrderRequest request = new OrderRequest();
		request.setItems(itemRequests);
		return request;
	}

	private Product createProduct(Long id, String name, Integer stock, Double price) {
		Product product = new Product();
		product.setId(id);
		product.setName(name);
		product.setStock(stock);
		product.setPrice(price);
		return product;
	}

	private OrderItem createOrderItem(Product product, Integer quantity, Double price) {
		OrderItem item = new OrderItem();
		item.setProduct(product);
		item.setQuantity(quantity);
		item.setProductPrice(price);
		return item;
	}

	private Order createOrder(Long id, List<OrderItem> items, OrderStatus status) {
		Order order = new Order();
		order.setId(id);
		order.setItems(items);
		order.setStatus(status);
		order.setOrderDate(LocalDateTime.now());
		return order;
	}

	private OrderResponse createOrderResponse(Order order) {
		OrderResponse response = new OrderResponse();
		response.setId(order.getId());
		response.setOrderDate(order.getOrderDate());
		response.setStatus(order.getStatus().name());

		List<OrderItemResponse> itemResponses = order.getItems().stream()
				.map(item -> {
					OrderItemResponse itemResponse = new OrderItemResponse();
					itemResponse.setProductId(item.getProduct().getId());
					itemResponse.setProductName(item.getProduct().getName());
					itemResponse.setQuantity(item.getQuantity());
					itemResponse.setProductPrice(item.getProductPrice());
					return itemResponse;
				})
				.toList();

		response.setItems(itemResponses);
		return response;
	}

	private void assertOrderResponseEquals(OrderResponse expected, OrderResponse actual) {
		assertNotNull(actual);
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getStatus(), actual.getStatus());
		assertEquals(expected.getItems().size(), actual.getItems().size());

		for (int i = 0; i < expected.getItems().size(); i++) {
			OrderItemResponse expectedItem = expected.getItems().get(i);
			OrderItemResponse actualItem = actual.getItems().get(i);

			assertEquals(expectedItem.getProductId(), actualItem.getProductId());
			assertEquals(expectedItem.getProductName(), actualItem.getProductName());
			assertEquals(expectedItem.getQuantity(), actualItem.getQuantity());
			assertEquals(expectedItem.getProductPrice(), actualItem.getProductPrice());
		}
	}
}
