package com.wl.order_service.service.impl;

import com.wl.order_service.exception.AppException;
import com.wl.order_service.exception.AppExceptionMessage;
import com.wl.order_service.mapper.ProductMapper;
import com.wl.order_service.model.Product;
import com.wl.order_service.model.request.ProductRequest;
import com.wl.order_service.model.response.ProductResponse;
import com.wl.order_service.validator.ProductValidator;
import com.wl.order_service.repository.ProductRepository;
import com.wl.order_service.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final ProductMapper productMapper;
	private final ProductValidator productValidator;

	@Override
	public ProductResponse createProduct(ProductRequest request) {
		productValidator.validateProductRequest(request);
		Product product = productMapper.toEntity(request);
		return productMapper.toResponse(productRepository.save(product));
	}

	@Override
	public ProductResponse getProductById(Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new AppException(AppExceptionMessage.PRODUCT_NOT_FOUND, id));
		return productMapper.toResponse(product);
	}

	@Override
	public List<ProductResponse> getAllProducts() {
		return productMapper.toResponseList(productRepository.findAll());
	}

	@Override
	public ProductResponse updateProduct(Long id, ProductRequest request) {
		productValidator.validateProductRequest(request);
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new AppException(AppExceptionMessage.PRODUCT_NOT_FOUND, id));
		productMapper.updateEntity(request, product);
		return productMapper.toResponse(productRepository.save(product));
	}

}