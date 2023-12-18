CREATE OR REPLACE FUNCTION prevent_invalid_balance()
    RETURNS TRIGGER AS
$$
BEGIN
    IF NEW.debit = 0 OR NEW.credit = 0 OR NEW.create_date IS NULL OR NEW.debit IS NULL OR NEW.credit IS NULL THEN
        RAISE EXCEPTION 'Bản cân đối không hợp lệ: Debit, credit và create_date phải được chỉ định.';
    END IF;
    RETURN NEW;
END;
$$
    LANGUAGE PLPGSQL;

CREATE TRIGGER prevent_invalid_balance_trigger1
    BEFORE UPDATE ON balance
    FOR EACH ROW EXECUTE FUNCTION prevent_invalid_balance();

CREATE TRIGGER prevent_invalid_balance_trigger2
    BEFORE INSERT ON balance
    FOR EACH ROW EXECUTE FUNCTION prevent_invalid_balance();


CREATE OR REPLACE FUNCTION prevent_modified_accounted_operation()
    RETURNS TRIGGER AS
$$
BEGIN
    IF (SELECT COUNT(*) FROM balance WHERE balance.id = NEW.balance_id) > 0 THEN
        RAISE EXCEPTION 'Không thể sửa giao dịch đã được tính toán trong bản cân đối.';
    END IF;
    RETURN NEW;
END;
$$
    LANGUAGE PLPGSQL;

CREATE TRIGGER prevent_modified_accounted_operation_trigger
    BEFORE UPDATE ON operations
    FOR EACH ROW EXECUTE FUNCTION prevent_modified_accounted_operation();


CREATE OR REPLACE FUNCTION prevent_deleted_accounted_operation()
    RETURNS TRIGGER AS
$$
BEGIN
    IF (SELECT COUNT(*) FROM balance WHERE balance.id = OLD.balance_id) > 0 THEN
        RAISE EXCEPTION 'Không thể xóa giao dịch đã được tính toán trong bản cân đối.';
    END IF;
    RETURN OLD;
END;
$$
    LANGUAGE PLPGSQL;

CREATE TRIGGER prevent_deleted_accounted_operation_trigger
    BEFORE DELETE ON operations
    FOR EACH ROW EXECUTE FUNCTION prevent_deleted_accounted_operation();