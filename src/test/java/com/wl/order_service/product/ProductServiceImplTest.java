package com.wl.order_service.product;

import com.wl.order_service.exception.AppException;
import com.wl.order_service.exception.AppExceptionMessage;
import com.wl.order_service.mapper.ProductMapper;
import com.wl.order_service.model.Product;
import com.wl.order_service.model.request.ProductRequest;
import com.wl.order_service.model.response.ProductResponse;
import com.wl.order_service.repository.ProductRepository;
import com.wl.order_service.service.impl.ProductServiceImpl;
import com.wl.order_service.validator.ProductValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private ProductMapper productMapper;

	@Mock
	private ProductValidator productValidator;

	@InjectMocks
	private ProductServiceImpl productService;

	@Test
	@DisplayName("Positive case: Successfully create a new product")
	public void testCreateProduct_Success() {
		ProductRequest request = createRequest("Product Test_A", 100.00, 50);
		Product product = createTestProduct(1L, request.getName(), request.getPrice(), request.getStock());
		ProductResponse response = createResponse(product);

		setupMocksForCreateOrUpdate(request, product, response);

		ProductResponse result = productService.createProduct(request);

		assertProductResponseEquals(response, result);
		verifyInteractionsForCreateOrUpdate(request, product);
	}

	@Test
	@DisplayName("Positive case: Successfully update a product")
	public void testUpdateProduct_Success() {
		Long productId = 1L;
		ProductRequest request = createRequest("Product Test_B", 300.00, 50);
		Product existingProduct = createTestProduct(productId, "Product Test_A", 100.0, 98);
		ProductResponse response = createResponse(existingProduct);

		setupMocksForUpdate(productId, request, existingProduct, response);

		ProductResponse result = productService.updateProduct(productId, request);

		assertProductResponseEquals(response, result);
		verifyInteractionsForUpdate(productId, request, existingProduct);
	}

	@Test
	@DisplayName("Expected error: Create product with empty name")
	public void testCreateProduct_EmptyName_ThrowsException() {
		ProductRequest request = createRequest("", 30.00, 50);

		setupValidationException(request, new AppException(AppExceptionMessage.EMPTY_REQUEST_PARAMS));

		AppException exception = assertThrows(AppException.class, () -> productService.createProduct(request));
		assertAppExceptionMessage(exception, "Request parameters cannot be empty");

		verify(productValidator, times(1)).validateProductRequest(request);
		verifyNoInteractions(productMapper, productRepository);
	}

	@Test
	@DisplayName("Expected error: Create product with negative price")
	public void testCreateProduct_NegativePrice_ThrowsException() {
		ProductRequest request = createRequest("Product Test_C", -55.00, 34);

		setupValidationException(request, new AppException(AppExceptionMessage.PRICE_LESS_THAN_ZERO));

		AppException exception = assertThrows(AppException.class, () -> productService.createProduct(request));
		assertAppExceptionMessage(exception, "Product price cannot be less than 0");

		verify(productValidator, times(1)).validateProductRequest(request);
		verifyNoInteractions(productMapper, productRepository);
	}

	@Test
	@DisplayName("Positive case: Get newly created product by ID")
	public void testGetProductById_Success() {
		Long productId = 1L;
		Product product = createTestProduct(productId, "Product Test_D", 17.99, 40);
		ProductResponse response = createResponse(product);

		when(productRepository.findById(productId)).thenReturn(Optional.of(product));
		when(productMapper.toResponse(product)).thenReturn(response);

		ProductResponse result = productService.getProductById(productId);

		assertProductResponseEquals(response, result);
		verify(productRepository, times(1)).findById(productId);
		verify(productMapper, times(1)).toResponse(product);
	}

	@Test
	@DisplayName("Expected error: Get product by non-existent ID")
	public void testGetProductById_NotFound_ThrowsException() {
		Long productId = 999L;

		when(productRepository.findById(productId)).thenReturn(Optional.empty());

		AppException exception = assertThrows(AppException.class, () -> productService.getProductById(productId));
		assertAppExceptionMessage(exception, "Product with ID '999' not found");

		verify(productRepository, times(1)).findById(productId);
		verifyNoInteractions(productMapper);
	}

	private void setupMocksForCreateOrUpdate(ProductRequest request, Product product, ProductResponse response) {
		doNothing().when(productValidator).validateProductRequest(request);
		when(productMapper.toEntity(request)).thenReturn(product);
		when(productRepository.save(product)).thenReturn(product);
		when(productMapper.toResponse(product)).thenReturn(response);
	}

	private void verifyInteractionsForCreateOrUpdate(ProductRequest request, Product product) {
		verify(productValidator, times(1)).validateProductRequest(request);
		verify(productMapper, times(1)).toEntity(request);
		verify(productRepository, times(1)).save(product);
		verify(productMapper, times(1)).toResponse(product);
	}

	private void setupMocksForUpdate(Long productId, ProductRequest request, Product existingProduct, ProductResponse response) {
		doNothing().when(productValidator).validateProductRequest(request);
		when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
		doNothing().when(productMapper).updateEntity(request, existingProduct);
		when(productRepository.save(existingProduct)).thenReturn(existingProduct);
		when(productMapper.toResponse(existingProduct)).thenReturn(response);
	}

	private void verifyInteractionsForUpdate(Long productId, ProductRequest request, Product existingProduct) {
		verify(productValidator, times(1)).validateProductRequest(request);
		verify(productRepository, times(1)).findById(productId);
		verify(productMapper, times(1)).updateEntity(request, existingProduct);
		verify(productRepository, times(1)).save(existingProduct);
		verify(productMapper, times(1)).toResponse(existingProduct);
	}

	private void setupValidationException(ProductRequest request, AppException exception) {
		doThrow(exception).when(productValidator).validateProductRequest(request);
	}

	private void assertProductResponseEquals(ProductResponse expected, ProductResponse actual) {
		assertNotNull(actual);
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getPrice(), actual.getPrice());
		assertEquals(expected.getStock(), actual.getStock());
	}

	private void assertAppExceptionMessage(AppException exception, String expectedMessage) {
		assertNotNull(exception);
		assertEquals(expectedMessage, exception.getMessage());
	}

	private Product createTestProduct(Long id, String name, Double price, Integer stock) {
		Product product = new Product();
		product.setId(id);
		product.setName(name);
		product.setPrice(price);
		product.setStock(stock);
		return product;
	}

	private ProductRequest createRequest(String name, Double price, Integer stock) {
		ProductRequest request = new ProductRequest();
		request.setName(name);
		request.setPrice(price);
		request.setStock(stock);
		return request;
	}

	private ProductResponse createResponse(Product product) {
		ProductResponse response = new ProductResponse();
		response.setId(product.getId());
		response.setName(product.getName());
		response.setPrice(product.getPrice());
		response.setStock(product.getStock());
		return response;
	}

}