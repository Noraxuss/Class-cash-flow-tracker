ALTER TABLE logs
    ADD logs_messages VARCHAR(255) NOT NULL;

ALTER TABLE logs
    ADD member_id VARCHAR(255);