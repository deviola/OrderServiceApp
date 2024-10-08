CREATE TABLE orders_history (
                                id BIGINT NOT NULL,
                                order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                status VARCHAR(20),
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                operation_id VARCHAR(36) NOT NULL PRIMARY KEY,
                                operation VARCHAR(10) NOT NULL,
                                operation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);