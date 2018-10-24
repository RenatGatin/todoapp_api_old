INSERT INTO user (username, email, password, activated, enabled) VALUES ('admin', 'admin@mail.me', 'b8f57d6d6ec0a60dfe2e20182d4615b12e321cad9e2979e0b9f81e0d6eda78ad9b6dcfe53e4e22d1', true, true);
INSERT INTO user (username, email, password, activated, enabled) VALUES ('user01', 'user01@mail.me', 'd6dfa9ff45e03b161e7f680f35d90d5ef51d243c2a8285aa7e11247bc2c92acde0c2bb626b1fac74', true, true);
INSERT INTO user (username, email, password, activated, enabled) VALUES ('user02', 'user02@mail.me', 'd6dfa9ff45e03b161e7f680f35d90d5ef51d243c2a8285aa7e11247bc2c92acde0c2bb626b1fac74', true, true);
INSERT INTO user (username, email, password, activated, enabled) VALUES ('immortal', 'renat@gatin.ca', 'b8f57d6d6ec0a60dfe2e20182d4615b12e321cad9e2979e0b9f81e0d6eda78ad9b6dcfe53e4e22d1', true, true);
INSERT INTO user (username, email, password, activated, enabled) VALUES ('renat.gatin@gmail.com', 'renat.gatin@gmail.com', 'ce59a1140bea1d7e53e0185a581bf7957390076d1308b3b07b54b19805fe90e186cd108c053870ed', true, true);

INSERT INTO authority (id, name) VALUES (1, 'ROLE_USER');
INSERT INTO authority (id, name) VALUES (2, 'ROLE_ADMIN');
INSERT INTO authority (id, name) VALUES (3, 'ROLE_SUPERADMIN');

INSERT INTO user_authority (user_id, authority_id) VALUES (1, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (2, 1);
INSERT INTO user_authority (user_id, authority_id) VALUES (3, 1);
INSERT INTO user_authority (user_id, authority_id) VALUES (4, 3);
INSERT INTO user_authority (user_id, authority_id) VALUES (5, 1);

INSERT INTO `todo_list` VALUES ('1', '2', 'Test list', '2018-10-17 15:58:58', null);
INSERT INTO `todo_list` VALUES ('2', '2', 'List 2(sdfsd kjls)', '2018-10-15 16:02:05', null);
INSERT INTO `todo_list` VALUES ('3', '2', 'Third list shared', '2018-10-15 16:25:12', null);

INSERT INTO `todo_list_share` VALUES ('3', '3');
INSERT INTO `todo_list_share` VALUES ('3', '4');
INSERT INTO `todo_list_share` VALUES ('3', '5');

INSERT INTO `todo_item` VALUES ('1', '1', 'Buy Milk', null, '0', '0', '2018-10-23 18:16:23', null);
INSERT INTO `todo_item` VALUES ('2', '1', 'Go to School', null, '0', '0', '2018-10-18 18:21:25', null);
INSERT INTO `todo_item` VALUES ('3', '1', 'Home Work', null, '0', '0', '2018-10-12 18:21:57', null);

-- Separate USER, ADMIN and SUPERADMIN scopes
-- INSERT INTO user_authority (user_id, authority_id) VALUES (1, 1);
-- INSERT INTO user_authority (user_id, authority_id) VALUES (4, 1);
-- INSERT INTO user_authority (user_id, authority_id) VALUES (4, 2);