-- Drop constraints first
ALTER TABLE IF EXISTS email_verification_token 
    DROP CONSTRAINT IF EXISTS FKhnve4ecwwyisbmu836ch9b944;

ALTER TABLE IF EXISTS study_group 
    DROP CONSTRAINT IF EXISTS FK4gohbqwqq8gvi0ksw0hjq0ies;

ALTER TABLE IF EXISTS study_group_user 
    DROP CONSTRAINT IF EXISTS FK_STUDY_GROUP_STUDY_GROUP_USER;

ALTER TABLE IF EXISTS study_group_user 
    DROP CONSTRAINT IF EXISTS FK_APPLICATION_USER_STUDY_GROUP_USER;

ALTER TABLE IF EXISTS study_group_weekday 
    DROP CONSTRAINT IF EXISTS FK_STUDY_GROUP_STUDY_GROUP_WEEKDAY;

ALTER TABLE IF EXISTS study_group_weekday 
    DROP CONSTRAINT IF EXISTS FK_WEEKDAY_STUDY_GROUP_WEEKDAY;

-- Drop tables
DROP TABLE IF EXISTS study_group_weekday CASCADE;
DROP TABLE IF EXISTS study_group_user CASCADE;
DROP TABLE IF EXISTS study_group CASCADE;
DROP TABLE IF EXISTS email_verification_token CASCADE;
DROP TABLE IF EXISTS application_user CASCADE;
DROP TABLE IF EXISTS subject CASCADE;
DROP TABLE IF EXISTS weekday CASCADE;

-- Create tables
CREATE TABLE application_user (
    apus_is_enabled boolean NOT NULL,
    apus_id_discord BIGINT,
    apus_sq_user bigserial NOT NULL,
    apus_ds_email VARCHAR(255) NOT NULL UNIQUE,
    apus_ds_name VARCHAR(255) NOT NULL,
    apus_ds_password VARCHAR(255) NOT NULL,
    PRIMARY KEY (apus_sq_user)
);

CREATE TABLE email_verification_token (
    apus_sq_user BIGINT NOT NULL UNIQUE,
    emvt_dt_validity TIMESTAMP(6) NOT NULL,
    emvt_sq_email_verification_token bigserial NOT NULL,
    emvt_ds_token VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (emvt_sq_email_verification_token)
);

CREATE TABLE study_group (
    stgr_dt_meeting_time TIME(6) NOT NULL,
    stgr_is_private boolean,
    stgr_qt_max_students INT NOT NULL,
    stgr_id_discord BIGINT,
    stgr_sq_study_group bigserial NOT NULL,
    stgr_user_sq_user_owner BIGINT NOT NULL,
    subj_sq_subject BIGINT NOT NULL,
    stgr_ds_description VARCHAR(255),
    stgr_ds_modality VARCHAR(255) NOT NULL CHECK (
        stgr_ds_modality IN ('PRESENTIAL', 'REMOTE', 'HYBRID')
    ),
    stgr_ds_title VARCHAR(255) NOT NULL,
    stgr_invite_discord varchar(255),
    PRIMARY KEY (stgr_sq_study_group)
);

CREATE TABLE study_group_user (
    apus_sq_user BIGINT NOT NULL,
    stgr_sq_study_group BIGINT NOT NULL,
    stgu_sq_study_group_user bigserial NOT NULL,
    PRIMARY KEY (stgu_sq_study_group_user),
    CONSTRAINT UK_STUDY_GROUP_USER UNIQUE (apus_sq_user, stgr_sq_study_group)
);

CREATE TABLE study_group_weekday (
    stgr_sq_study_group BIGINT NOT NULL,
    stwd_sq_study_group_weekday bigserial NOT NULL,
    week_sq_weekday BIGINT NOT NULL,
    PRIMARY KEY (stwd_sq_study_group_weekday),
    CONSTRAINT UK_STUDY_GROUP_WEEKDAY UNIQUE (stgr_sq_study_group, week_sq_weekday)
);

CREATE TABLE subject (
    subj_sq_subject bigserial NOT NULL,
    subj_cd_code VARCHAR(255) NOT NULL UNIQUE,
    subj_ds_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (subj_sq_subject)
);

CREATE TABLE weekday (
    week_sq_weekday bigserial NOT NULL,
    week_ds_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (week_sq_weekday)
);

-- Add foreign key constraints
ALTER TABLE email_verification_token 
    ADD CONSTRAINT FKhnve4ecwwyisbmu836ch9b944 
    FOREIGN KEY (apus_sq_user) REFERENCES application_user(apus_sq_user);

ALTER TABLE study_group 
    ADD CONSTRAINT FK4gohbqwqq8gvi0ksw0hjq0ies 
    FOREIGN KEY (subj_sq_subject) REFERENCES subject(subj_sq_subject);

ALTER TABLE study_group_user 
    ADD CONSTRAINT FK_STUDY_GROUP_STUDY_GROUP_USER 
    FOREIGN KEY (stgr_sq_study_group) REFERENCES study_group(stgr_sq_study_group);

ALTER TABLE study_group_user 
    ADD CONSTRAINT FK_APPLICATION_USER_STUDY_GROUP_USER 
    FOREIGN KEY (apus_sq_user) REFERENCES application_user(apus_sq_user);

ALTER TABLE study_group_weekday 
    ADD CONSTRAINT FK_STUDY_GROUP_STUDY_GROUP_WEEKDAY 
    FOREIGN KEY (stgr_sq_study_group) REFERENCES study_group(stgr_sq_study_group);

ALTER TABLE study_group_weekday 
    ADD CONSTRAINT FK_WEEKDAY_STUDY_GROUP_WEEKDAY 
    FOREIGN KEY (week_sq_weekday) REFERENCES weekday(week_sq_weekday);