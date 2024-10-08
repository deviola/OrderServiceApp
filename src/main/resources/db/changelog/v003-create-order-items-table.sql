CREATE TABLE order_items (
                             id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                             order_id BIGINT NOT NULL,
                             product_id BIGINT NOT NULL,
                             quantity INT NOT NULL,
                             product_price DECIMAL(10,2) NOT NULL,
                             CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES orders(id),
                             CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products(id)
);
