CREATE TRIGGER order_items_after_insert
    AFTER INSERT ON order_items
    FOR EACH ROW
BEGIN
    INSERT INTO order_items_history (
        id,
        order_id,
        product_id,
        product_price,
        quantity,
        operation_id,
        operation,
        operation_date
    ) VALUES (
                 NEW.id,
                 NEW.order_id,
                 NEW.product_id,
                 NEW.product_price,
                 NEW.quantity,
                 UUID(),
                 'INSERT',
                 NOW()
             );
END;