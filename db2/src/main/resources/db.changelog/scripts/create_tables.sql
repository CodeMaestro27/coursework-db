CREATE TABLE balance (
                         id SERIAL PRIMARY KEY,
                         create_date DATE,
                         debit NUMERIC,
                         credit NUMERIC,
                         amount NUMERIC
);

CREATE TABLE articles(
                         id SERIAL PRIMARY KEY,
                         name VARCHAR
);

CREATE TABLE operations(
                           id SERIAL PRIMARY KEY,
                           debit NUMERIC,
                           credit NUMERIC,
                           create_date DATE,
                           balance_id INT,
                           article_id INT
);

ALTER TABLE operations
    ADD CONSTRAINT fk_operations_balance
        FOREIGN KEY (balance_id)
            REFERENCES balance(id);

ALTER TABLE operations
    ADD CONSTRAINT fk_operations_articles
        FOREIGN KEY (article_id)
            REFERENCES articles(id);

INSERT INTO articles (name) VALUES ('books');
INSERT INTO articles (name) VALUES ('food');
INSERT INTO articles (name) VALUES ('something');
INSERT INTO articles (name) VALUES ('education');

INSERT INTO balance (create_date, debit, credit, amount) VALUES ('2023-09-20', 200.00, 0.00, 200.00);
INSERT INTO balance (create_date, debit, credit, amount) VALUES ('2023-09-21', 50.00, 0.00, 250.00);
INSERT INTO balance (create_date, debit, credit, amount) VALUES ('2023-10-21', 200.00, 90.00, 570.00);
INSERT INTO balance (create_date, debit, credit, amount) VALUES ('2023-10-07', 10.00, 20.00, 370.00);

INSERT INTO operations (article_id, debit, credit, create_date, balance_id) VALUES (1, 500.00, 600.00, '2023-10-20', 1);
INSERT INTO operations (article_id, debit, credit, create_date, balance_id) VALUES (2, 50.00, 0.00, '2023-09-22', 2);
INSERT INTO operations (article_id, debit, credit, create_date, balance_id) VALUES (3, 30.00, 1.00, '2023-10-07', 3);
INSERT INTO operations (article_id, debit, credit, create_date, balance_id) VALUES (4, 10.00, 200.00, '2023-10-22', 4);

INSERT INTO operations (article_id, debit, credit, create_date, balance_id) VALUES (1, 200.00, 10.00, '2023-10-13', 1);
INSERT INTO operations (article_id, debit, credit, create_date, balance_id) VALUES (2, 0.00, 50.00, '2023-09-21', 2);
INSERT INTO operations (article_id, debit, credit, create_date, balance_id) VALUES (3, 30.00, 0.00, '2023-10-12', 3);
INSERT INTO operations (article_id, debit, credit, create_date, balance_id) VALUES (4, 500.00, 600.00, '2023-10-20', 4);
