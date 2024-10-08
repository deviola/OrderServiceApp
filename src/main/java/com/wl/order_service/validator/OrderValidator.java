package com.wl.order_service.validator;

import com.wl.order_service.exception.AppException;
import com.wl.order_service.exception.AppExceptionMessage;
import com.wl.order_service.model.OrderStatus;
import com.wl.order_service.model.request.OrderItemRequest;
import com.wl.order_service.model.Product;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

	public void validateProductOrderItemRequest(OrderItemRequest request, Product product) {
		validateProductInStock(product);
		validateProductQuantity(request, product);
	}

	public void validateStatusBeforeChange(OrderStatus currentStatus, OrderStatus newStatus) {
		if (currentStatus == newStatus) {
			throw new AppException(AppExceptionMessage.ORDER_STATUS_SAME, currentStatus);
		}

		if (currentStatus == OrderStatus.COMPLETED) {
			throw new AppException(AppExceptionMessage.ORDER_COMPLETED);
		}

		switch (newStatus) {
			case CONFIRMED:
				if (currentStatus != OrderStatus.CREATED) {
					throw new AppException(
							AppExceptionMessage.INVALID_ORDER_STATUS_TRANSITION, currentStatus, newStatus
					);
				}
				break;

			case COMPLETED:
				if (currentStatus != OrderStatus.CONFIRMED) {
					throw new AppException(
							AppExceptionMessage.INVALID_ORDER_STATUS_TRANSITION, currentStatus, newStatus
					);
				}
				break;
		}
	}

	private void validateProductInStock(Product product) {
		if (product.getStock() <= 0) {
			throw new AppException(AppExceptionMessage.OUT_OF_STOCK, product.getId());
		}
	}

	private void validateProductQuantity(OrderItemRequest request, Product product) {
		if (request.getQuantity() == null || request.getQuantity() < 1) {
			throw new AppException(AppExceptionMessage.STOCK_QUANTITY_LESS_THAN_ZERO);
		}

		if (product.getStock() < request.getQuantity()) {
			throw new AppException(AppExceptionMessage.INSUFFICIENT_STOCK);
		}
	}
}
