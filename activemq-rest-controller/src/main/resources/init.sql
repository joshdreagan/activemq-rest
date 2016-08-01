CREATE SCHEMA SA;

CREATE TABLE subscriptions (
    subscriber_id varchar(255) NOT NULL,
    destination varchar(255) NOT NULL,
    subscription_request clob NOT NULL,
    constraint subscriptions_pk PRIMARY KEY (subscriber_id, destination)
);

--INSERT INTO subscriptions VALUES ('B', 'TEST.FOO', '{"selector":null,"url":"http://localhost:8082"}');
