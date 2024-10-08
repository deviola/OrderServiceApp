package com.wl.order_service.repository;

import com.wl.order_service.model.OrderItemHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemHistoryRepository extends JpaRepository<OrderItemHistory, String> {
	List<OrderItemHistory> findByOrderIdOrderByOperationDateDesc(Long orderId);
}

