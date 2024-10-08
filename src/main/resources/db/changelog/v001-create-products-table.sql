CREATE TABLE products (
                          id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          price DECIMAL(10,2),
                          stock INT,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);