--liquibase formatted sql
--changeset shakurov_dinar:1 failOnError: true
-- https://docs.liquibase.com/concepts/changelogs/sql-format.html

CREATE TABLE diploma.user
(
    user_id     TEXT PRIMARY KEY,
    create_date TIMESTAMP NOT NULL,
    modify_date TIMESTAMP NOT NULL
);

CREATE TABLE diploma.token
(
    token_id      BIGSERIAL PRIMARY KEY,
    user_id       TEXT      NOT NULL,
    access_token  TEXT      NOT NULL,
    expired_at    TIMESTAMP NOT NULL,
    refresh_Token TEXT      NOT NULL,
    disk_type     TEXT      NOT NULL,
    create_date   TIMESTAMP NOT NULL,
    modify_date   TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES diploma.user
);

--rollback DROP TABLE diploma.token;
--rollback DROP TABLE diploma.user;