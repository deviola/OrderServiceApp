package com.wl.order_service.repository;

import com.wl.order_service.model.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, String> {
	List<OrderHistory> findByIdOrderByOperationDateDesc(Long orderId);
}
