package com.wl.order_service.mapper;

import com.wl.order_service.model.OrderItemHistory;
import com.wl.order_service.model.response.OrderItemHistoryResponse;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderItemHistoryMapper {

	public OrderItemHistoryResponse toResponse(OrderItemHistory history) {
		OrderItemHistoryResponse response = new OrderItemHistoryResponse();
		response.setOperationId(history.getOperationId());
		response.setId(history.getId());
		response.setOrderId(history.getOrderId());
		response.setProductId(history.getProductId());
		response.setProductPrice(history.getProductPrice());
		response.setQuantity(history.getQuantity());
		response.setOperation(history.getOperation());
		response.setOperationDate(history.getOperationDate());
		return response;
	}

	public List<OrderItemHistoryResponse> toResponseList(List<OrderItemHistory> historyList) {
		return historyList.stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}
}
