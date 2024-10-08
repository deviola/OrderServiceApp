CREATE TABLE order_items_history (
                                     id BIGINT NOT NULL,
                                     order_id BIGINT NOT NULL,
                                     product_id BIGINT NOT NULL,
                                     product_price DECIMAL(10,2) NOT NULL,
                                     quantity INT NOT NULL,
                                     operation_id VARCHAR(36) NOT NULL PRIMARY KEY,
                                     operation VARCHAR(10) NOT NULL,
                                     operation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     FOREIGN KEY (order_id) REFERENCES orders(id),
                                     FOREIGN KEY (product_id) REFERENCES products(id)
);