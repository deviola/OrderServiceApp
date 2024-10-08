CREATE TRIGGER orders_after_update
    AFTER UPDATE ON orders
    FOR EACH ROW
BEGIN
    IF NOT (OLD.id <=> NEW.id AND
            OLD.order_date <=> NEW.order_date AND
            OLD.status <=> NEW.status AND
            OLD.created_at <=> NEW.created_at AND
            OLD.modified_at <=> NEW.modified_at) THEN

        INSERT INTO orders_history (
            id,
            order_date,
            status,
            created_at,
            modified_at,
            operation_id,
            operation,
            operation_date
        ) VALUES (
            OLD.id,
            OLD.order_date,
            OLD.status,
            OLD.created_at,
            OLD.modified_at,
            UUID(),
            'UPDATE',
            NOW()
        );
END IF;
END;
