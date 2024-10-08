package com.wl.order_service.validator;

import com.wl.order_service.exception.AppException;
import com.wl.order_service.exception.AppExceptionMessage;
import com.wl.order_service.model.request.ProductRequest;
import com.wl.order_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductValidator {

	private final ProductRepository repository;

	public void validateProductRequest(ProductRequest request) {
		validateNotEmptyFields(request);
		validatePrice(request.getPrice());
		validateStock(request.getStock());
		validateUniqueProduct(request);
	}

	private void validateNotEmptyFields(ProductRequest request) {
		if (request.getName() == null || request.getName().isEmpty()
				|| request.getStock() == null || request.getPrice() == null) {
			throw new AppException(AppExceptionMessage.EMPTY_REQUEST_PARAMS);
		}
	}

	private void validatePrice(Double price) {
		if (price < 0) {
			throw new AppException(AppExceptionMessage.PRICE_LESS_THAN_ZERO);
		}
	}

	private void validateStock(Integer stock) {
		if (stock < 0) {
			throw new AppException(AppExceptionMessage.STOCK_QUANTITY_LESS_THAN_ZERO);
		}
	}

	private void validateUniqueProduct(ProductRequest request) {
		boolean exists = repository.existsByNameIgnoreCaseAndPrice(request.getName(), request.getPrice());
		if (exists) {
			throw new AppException(AppExceptionMessage.PRODUCT_ALREADY_EXISTS, request.getName(), request.getPrice());
		}
	}
}
