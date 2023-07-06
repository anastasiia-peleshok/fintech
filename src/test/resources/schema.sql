DROP TABLE IF EXISTS account;

CREATE TABLE account (
                         id BIGINT NOT NULL Auto_increment,
                         first_name VARCHAR(255) NOT NULL,
                         last_name VARCHAR(255) NOT NULL,
                         identifier BIGINT NOT NULL,
                         passport_number BIGINT NOT NULL,
                         annual_income DECIMAL(19, 2) NOT NULL,
                         available_balance DECIMAL(19, 2) DEFAULT 500.00,
                         credit_balance DECIMAL(19, 2) DEFAULT 0.00,
                         deposit_balance DECIMAL(19, 2) DEFAULT 0.00,
                         credit_limit DECIMAL(19, 2) DEFAULT 0.00,
                         PRIMARY KEY (id)
);
DROP TABLE IF EXISTS transaction;
CREATE TABLE transaction (
                             id BIGINT NOT NULL AUTO_INCREMENT,
                             account_sender_id BIGINT NOT NULL,
                             account_receiver_id BIGINT NOT NULL,
                             description VARCHAR(255) NOT NULL,
                             amount DECIMAL(19, 2) NOT NULL,
                             stamp TIMESTAMP NOT NULL,
                             commission DECIMAL(19, 2) NOT NULL,
                             PRIMARY KEY (id)
);