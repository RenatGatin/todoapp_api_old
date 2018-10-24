-- IMPORTANT: Requires MySQL version minimum: 5.6.5


-- IF you doesn't want to delete foregn_key dependent tables in proper order - you can disable FOREIGN_KEY_CHECKS and enable it again after all done
--SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS `user_authority`;
DROP TABLE IF EXISTS `todo_item`;
DROP TABLE IF EXISTS `todo_list_share`;
DROP TABLE IF EXISTS `todo_list`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `authority`;
DROP TABLE IF EXISTS `oauth_access_token`;
DROP TABLE IF EXISTS `oauth_refresh_token`;
DROP TABLE IF EXISTS `pseudo_user`;
--SET FOREIGN_KEY_CHECKS=1; 
-- end of disabling FOREIGN_KEY_CHECKS

CREATE TABLE user (
  id int(11) NOT NULL AUTO_INCREMENT  PRIMARY KEY,
  username VARCHAR(50) NOT NULL,
  email VARCHAR(50) NOT NULL,
  password VARCHAR(500) NOT NULL,
  activated BOOLEAN DEFAULT FALSE,
  activationkey VARCHAR(50) DEFAULT NULL,
  resetpasswordkey VARCHAR(100) DEFAULT NULL,
  date_created_resetpasswordkey DATETIME DEFAULT NULL,
  enabled BOOLEAN DEFAULT FALSE,
  date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
  date_last_modified DATETIME ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
);

CREATE TABLE authority (
  id int(11) NOT NULL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
  date_last_modified DATETIME ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE user_authority (
  user_id int(11) NOT NULL,
  authority_id int(11) NOT NULL,
  date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
  date_last_modified DATETIME ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES user (id),
  FOREIGN KEY (authority_id) REFERENCES authority (id),
  UNIQUE INDEX user_authority_idx_1 (user_id, authority_id)
);

CREATE TABLE oauth_access_token (
  token_id VARCHAR(256) DEFAULT NULL,
  token BLOB,
  authentication_id VARCHAR(256) DEFAULT NULL,
  user_name VARCHAR(256) DEFAULT NULL,
  client_id VARCHAR(256) DEFAULT NULL,
  authentication BLOB,
  refresh_token VARCHAR(256) DEFAULT NULL,
  date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
  date_last_modified DATETIME ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE oauth_refresh_token (
  token_id VARCHAR(256) DEFAULT NULL,
  token BLOB,
  authentication BLOB,
  date_created DATETIME DEFAULT CURRENT_TIMESTAMP,
  date_last_modified DATETIME ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `pseudo_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `firstname` varchar(50) NOT NULL,
  `lastname` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(500) NOT NULL,
  `activated` tinyint(1) NOT NULL,
  `activationkey` varchar(500) NOT NULL,
  `date_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `activationkey` (`activationkey`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
);

CREATE TABLE `todo_list` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `creator_id` int(11) NOT NULL,
  `name` varchar(200) NOT NULL,
  `date_created` datetime NOT NULL,
  `date_last_modified` datetime NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Unique_id_creator` (`id`,`creator_id`) USING BTREE,
  KEY `FK_creator_user` (`creator_id`),
  CONSTRAINT `FK_creator_user` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `todo_list_share` (
  `list_id` int(11) NOT NULL,
  `share_user_id` int(11) NOT NULL,
  UNIQUE KEY `Unique_list_share` (`list_id`,`share_user_id`) USING BTREE,
  KEY `FK_share_user` (`share_user_id`),
  CONSTRAINT `FK_list_id` FOREIGN KEY (`list_id`) REFERENCES `todo_list` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_share_user` FOREIGN KEY (`share_user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `todo_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `list_id` int(11) NOT NULL,
  `title` varchar(500) NOT NULL,
  `notes` varchar(5000) DEFAULT NULL,
  `completed` tinyint(1) NOT NULL DEFAULT '0',
  `hidden` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Can be hidden only if completed',
  `date_created` datetime NOT NULL,
  `date_last_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Unique_item_list` (`id`,`list_id`) USING BTREE,
  KEY `list_id` (`list_id`),
  CONSTRAINT `FK_item_list` FOREIGN KEY (`list_id`) REFERENCES `todo_list` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);
