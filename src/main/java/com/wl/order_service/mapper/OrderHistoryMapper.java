package com.wl.order_service.mapper;

import com.wl.order_service.model.OrderHistory;
import com.wl.order_service.model.response.OrderHistoryResponse;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderHistoryMapper {

	public OrderHistoryResponse toResponse(OrderHistory history) {
		OrderHistoryResponse response = new OrderHistoryResponse();
		response.setOperationId(history.getOperationId());
		response.setId(history.getId());
		response.setOrderDate(history.getOrderDate());
		response.setStatus(history.getStatus());
		response.setCreatedAt(history.getCreatedAt());
		response.setModifiedAt(history.getModifiedAt());
		response.setOperation(history.getOperation());
		response.setOperationDate(history.getOperationDate());
		return response;
	}

	public List<OrderHistoryResponse> toResponseList(List<OrderHistory> historyList) {
		return historyList.stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}
}
