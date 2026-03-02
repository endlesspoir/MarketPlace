
SET search_path TO user_service;


ALTER TABLE user_profiles
ALTER COLUMN user_id TYPE BIGINT USING user_id::bigint;


ALTER TABLE user_roles
ALTER COLUMN user_id TYPE BIGINT USING user_id::bigint;

ALTER TABLE user_roles
ALTER COLUMN role_id TYPE BIGINT USING role_id::bigint;