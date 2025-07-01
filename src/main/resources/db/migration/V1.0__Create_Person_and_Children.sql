-- 1. Create group_members (no FK yet)
CREATE TABLE group_members
(
    id               VARCHAR(255) NOT NULL,
    first_name       VARCHAR(255) NOT NULL,
    last_name        VARCHAR(255) NOT NULL,
    email            VARCHAR(255) NOT NULL,
    date_of_creation TIMESTAMP    NOT NULL,
    person_type      VARCHAR(255) NOT NULL,
    guardian_id      VARCHAR(255),
    overseer_id      VARCHAR(255),
    CONSTRAINT pk_group_members PRIMARY KEY (id)
);

ALTER TABLE group_members
    ADD CONSTRAINT uc_group_members_email UNIQUE (email);

-- 2. Create overseers
CREATE TABLE overseers
(
    id               VARCHAR(255) NOT NULL,
    first_name       VARCHAR(255) NOT NULL,
    last_name        VARCHAR(255) NOT NULL,
    email            VARCHAR(255) NOT NULL,
    date_of_creation TIMESTAMP    NOT NULL,
    person_type      VARCHAR(255) NOT NULL,
    CONSTRAINT pk_overseers PRIMARY KEY (id)
);

ALTER TABLE overseers
    ADD CONSTRAINT uc_overseers_email UNIQUE (email);

-- 3. Create guardians
CREATE TABLE guardians
(
    id               VARCHAR(255) NOT NULL,
    first_name       VARCHAR(255) NOT NULL,
    last_name        VARCHAR(255) NOT NULL,
    email            VARCHAR(255) NOT NULL,
    date_of_creation TIMESTAMP    NOT NULL,
    group_member_id  VARCHAR(255) NOT NULL,
    person_type      VARCHAR(255) NOT NULL,
    CONSTRAINT pk_guardians PRIMARY KEY (id)
);

ALTER TABLE guardians
    ADD CONSTRAINT uc_guardians_email UNIQUE (email);

-- 4. Add foreign key constraints
ALTER TABLE group_members
    ADD CONSTRAINT FK_GROUP_MEMBERS_ON_OVERSEER FOREIGN KEY (overseer_id) REFERENCES overseers (id);

ALTER TABLE guardians
    ADD CONSTRAINT FK_GUARDIANS_ON_GROUP_MEMBER FOREIGN KEY (group_member_id) REFERENCES group_members (id);