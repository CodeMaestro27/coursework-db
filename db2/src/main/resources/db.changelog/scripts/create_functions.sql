DROP FUNCTION IF EXISTS uncalculated_operations();
CREATE OR REPLACE FUNCTION uncalculated_operations()
    RETURNS TABLE(art_name INTEGER, total NUMERIC) AS
$$
BEGIN
    RETURN QUERY SELECT a.id, SUM(o.debit - o.credit)
    FROM articles a
        LEFT JOIN operations o ON a.id = o.article_id
    GROUP BY a.id;
END;
$$
LANGUAGE PLPGSQL;

DROP FUNCTION IF EXISTS balance_operations_count();
CREATE OR REPLACE FUNCTION balance_operations_count()
    RETURNS TABLE(b_id INTEGER, operations_count BIGINT) AS
$$
BEGIN
    RETURN QUERY SELECT b.id, COUNT(o.balance_id)
    FROM balance b
             LEFT JOIN operations o ON b.id = o.balance_id
    GROUP BY b.id, b.create_date;
END;
$$
LANGUAGE PLPGSQL;