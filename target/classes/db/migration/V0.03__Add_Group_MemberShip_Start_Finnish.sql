ALTER TABLE group_memberships
    ADD membership_start_date TIMESTAMP NOT NULL;

ALTER TABLE group_memberships
    ADD membership_end_date TIMESTAMP;