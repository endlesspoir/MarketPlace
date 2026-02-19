SET search_path TO user_service;

INSERT INTO "user_service".roles (name, description)
VALUES ('ADMIN', 'Администратор системы'),
       ('SELLER', 'Продавец на маркетплейсе'),
       ('BUYER', 'Покупатель на маркетплейсе');
