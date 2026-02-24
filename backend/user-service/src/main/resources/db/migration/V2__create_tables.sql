SET search_path TO user_service;

CREATE TABLE user_service.roles (
                                    id BIGSERIAL PRIMARY KEY,
                                    name VARCHAR(20) NOT NULL UNIQUE,
                                    description VARCHAR(255),
                                    permissions TEXT
);


CREATE TABLE user_service.users (
                                    id BIGSERIAL PRIMARY KEY,
                                    email VARCHAR(100) NOT NULL UNIQUE,
                                    password VARCHAR(255) NOT NULL,
                                    login VARCHAR(50) NOT NULL UNIQUE,
                                    first_name VARCHAR(50) NOT NULL,
                                    last_name VARCHAR(50),
                                    phone_number VARCHAR(20) UNIQUE,
                                    is_verified_email BOOLEAN NOT NULL DEFAULT FALSE,
                                    is_verified_phone BOOLEAN NOT NULL DEFAULT FALSE,
                                    last_login TIMESTAMP,
                                    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                                    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
                                    deleted_at TIMESTAMP
);


CREATE TABLE user_service.user_profiles (
                                            id BIGSERIAL PRIMARY KEY,
                                            user_id INT NOT NULL UNIQUE REFERENCES user_service.users(id) ON DELETE CASCADE,
                                            avatar_url VARCHAR(500),
                                            city VARCHAR(100),
                                            country VARCHAR(100),
                                            bio TEXT,
                                            language VARCHAR(10) NOT NULL DEFAULT 'ru',
                                            timezone VARCHAR(50),
                                            updated_at TIMESTAMP DEFAULT NOW()
);


CREATE TABLE user_service.user_roles (
                                         user_id INT NOT NULL REFERENCES user_service.users(id) ON DELETE CASCADE,
                                         role_id INT NOT NULL REFERENCES user_service.roles(id) ON DELETE CASCADE,
                                         PRIMARY KEY (user_id, role_id)
);

