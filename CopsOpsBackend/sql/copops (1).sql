-- Adminer 4.6.2 MySQL dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

DROP TABLE IF EXISTS `cop_approval_comments`;
CREATE TABLE `cop_approval_comments` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `ref_user_id` int(10) NOT NULL,
  `comment` text NOT NULL,
  `created_by` int(10) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ref_user_id` (`ref_user_id`),
  KEY `created_by` (`created_by`),
  CONSTRAINT `cop_approval_comments_ibfk_4` FOREIGN KEY (`created_by`) REFERENCES `ref_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `cop_approval_comments_ibfk_3` FOREIGN KEY (`ref_user_id`) REFERENCES `ref_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `cop_approval_comments` (`id`, `ref_user_id`, `comment`, `created_by`, `created_at`, `updated_at`) VALUES
(1,	18,	'Compte parasite',	2,	'2019-01-08 11:30:24',	'2019-01-08 11:30:24');

DROP TABLE IF EXISTS `cop_crew`;
CREATE TABLE `cop_crew` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `crew_name` varchar(191) NOT NULL,
  `incident_handrail` tinyint(4) NOT NULL,
  `cop_incident_details_id` bigint(20) NOT NULL,
  `cop_handrail_id` bigint(20) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `updated_by` int(10) DEFAULT NULL,
  `is_deleted` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `cop_crew` (`id`, `crew_name`, `incident_handrail`, `cop_incident_details_id`, `cop_handrail_id`, `created_at`, `updated_at`, `updated_by`, `is_deleted`) VALUES
(1,	'Cops Team-A',	1,	1,	1,	'2019-01-08 09:28:13',	'2019-01-08 09:28:13',	2,	1),
(2,	'Furtif',	1,	1,	1,	'2019-01-08 10:42:59',	'2019-01-08 10:42:59',	2,	1),
(3,	'Équipage 1',	1,	1,	1,	'2019-01-08 10:43:44',	'2019-01-08 10:43:44',	2,	1),
(4,	'EQUIPE Mr CABOCHE',	1,	1,	1,	'2019-01-08 12:21:38',	'2019-01-08 12:21:38',	2,	1);

DROP TABLE IF EXISTS `cop_crew_user_mapping`;
CREATE TABLE `cop_crew_user_mapping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cop_crew_id` bigint(20) NOT NULL,
  `ref_user_id` int(10) NOT NULL,
  `updated_by` int(10) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `is_deleted` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `cop_crew_id` (`cop_crew_id`),
  KEY `ref_user_id` (`ref_user_id`),
  CONSTRAINT `cop_crew_user_mapping_ibfk_1` FOREIGN KEY (`cop_crew_id`) REFERENCES `cop_crew` (`id`),
  CONSTRAINT `cop_crew_user_mapping_ibfk_2` FOREIGN KEY (`ref_user_id`) REFERENCES `ref_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `cop_crew_user_mapping` (`id`, `cop_crew_id`, `ref_user_id`, `updated_by`, `created_at`, `updated_at`, `is_deleted`) VALUES
(1,	1,	12,	2,	'2019-01-08 09:28:13',	'2019-01-08 09:28:13',	1),
(2,	1,	14,	2,	'2019-01-08 09:28:13',	'2019-01-08 09:28:13',	1),
(3,	1,	15,	2,	'2019-01-08 09:28:13',	'2019-01-08 09:28:13',	1),
(4,	2,	14,	2,	'2019-01-08 10:42:59',	'2019-01-08 10:42:59',	1),
(5,	2,	12,	2,	'2019-01-08 10:42:59',	'2019-01-08 10:42:59',	1),
(6,	2,	15,	2,	'2019-01-08 10:42:59',	'2019-01-08 10:42:59',	1),
(7,	3,	12,	2,	'2019-01-08 10:43:44',	'2019-01-08 10:43:44',	1),
(8,	3,	14,	2,	'2019-01-08 10:43:44',	'2019-01-08 10:43:44',	1),
(9,	3,	15,	2,	'2019-01-08 10:43:44',	'2019-01-08 10:43:44',	1),
(10,	4,	16,	2,	'2019-01-08 12:21:38',	'2019-01-08 12:21:38',	1),
(11,	4,	12,	2,	'2019-01-08 12:21:38',	'2019-01-08 12:21:38',	1),
(12,	4,	14,	2,	'2019-01-08 12:21:38',	'2019-01-08 12:21:38',	1),
(13,	4,	15,	2,	'2019-01-08 12:21:38',	'2019-01-08 12:21:38',	1);

DROP TABLE IF EXISTS `cop_handrail`;
CREATE TABLE `cop_handrail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `object` varchar(255) DEFAULT NULL,
  `description` text,
  `signature` varchar(100) DEFAULT NULL,
  `reference` int(11) DEFAULT NULL,
  `qr_code` varchar(100) DEFAULT NULL,
  `latitude` varchar(100) DEFAULT NULL,
  `longitude` varchar(100) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `updated_on` datetime DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '1',
  `status` tinyint(4) NOT NULL DEFAULT '0',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `cop_handrail` (`id`, `object`, `description`, `signature`, `reference`, `qr_code`, `latitude`, `longitude`, `address`, `city`, `updated_on`, `created_by`, `is_deleted`, `status`, `created_at`, `updated_at`) VALUES
(1,	'objet main courante',	'test main courante',	'5c348776080ad.jpg',	100000,	'100000.png',	NULL,	NULL,	NULL,	NULL,	'2019-01-08 11:20:22',	17,	1,	0,	'2019-01-08 11:20:22',	'2019-01-08 11:20:22'),
(2,	'test MC',	'description MC',	'5c349865dd15a.jpg',	100002,	'100002.png',	NULL,	NULL,	NULL,	NULL,	'2019-01-08 12:32:37',	17,	1,	0,	'2019-01-08 12:32:37',	'2019-01-08 12:32:37');

DROP TABLE IF EXISTS `cop_handrail_attachment`;
CREATE TABLE `cop_handrail_attachment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cop_handrail_id` bigint(20) DEFAULT NULL,
  `photo` varchar(100) DEFAULT NULL,
  `video` varchar(100) DEFAULT NULL,
  `updated_on` datetime DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '1',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `cop_handrail_attachment` (`id`, `cop_handrail_id`, `photo`, `video`, `updated_on`, `is_deleted`, `created_at`, `updated_at`) VALUES
(1,	1,	'5c3487760b0bd.jpg',	NULL,	NULL,	1,	'2019-01-08 11:20:22',	'2019-01-08 11:20:22'),
(2,	2,	'5c349865dff4a.jpg',	NULL,	NULL,	1,	'2019-01-08 12:32:37',	'2019-01-08 12:32:37');

DROP TABLE IF EXISTS `cop_incident_attachment`;
CREATE TABLE `cop_incident_attachment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cop_incident_details_id` bigint(20) DEFAULT NULL,
  `photo` varchar(100) DEFAULT NULL,
  `video` varchar(100) DEFAULT NULL,
  `updated_on` datetime DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '1',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `cop_incident_attachment` (`id`, `cop_incident_details_id`, `photo`, `video`, `updated_on`, `is_deleted`, `created_at`, `updated_at`) VALUES
(1,	1,	'5c335e63ef8ec.jpg',	NULL,	NULL,	1,	'2019-01-07 14:12:51',	'2019-01-07 14:12:51'),
(2,	2,	'5c335f250ef11.jpg',	NULL,	NULL,	1,	'2019-01-07 14:16:05',	'2019-01-07 14:16:05'),
(3,	3,	'5c3483b758353.jpg',	NULL,	NULL,	1,	'2019-01-08 11:04:23',	'2019-01-08 11:04:23'),
(4,	4,	'5c3488340b1ce.jpg',	NULL,	NULL,	1,	'2019-01-08 11:23:32',	'2019-01-08 11:23:32');

DROP TABLE IF EXISTS `cop_incident_details`;
CREATE TABLE `cop_incident_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ref_incident_category_id` tinyint(4) DEFAULT NULL,
  `ref_incident_subcategory_id` tinyint(4) unsigned DEFAULT NULL,
  `incident_description` text,
  `other_description` text,
  `reference` int(10) DEFAULT NULL,
  `qr_code` varchar(100) DEFAULT NULL,
  `latitude` varchar(100) DEFAULT NULL,
  `longitude` varchar(100) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `updated_on` datetime DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '1',
  `status` tinyint(4) NOT NULL DEFAULT '1',
  `updated_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ref_incident_category_id` (`ref_incident_category_id`),
  KEY `ref_incident_subcategory_id` (`ref_incident_subcategory_id`),
  CONSTRAINT `cop_incident_details_ibfk_1` FOREIGN KEY (`ref_incident_category_id`) REFERENCES `ref_incident_category` (`id`),
  CONSTRAINT `cop_incident_details_ibfk_2` FOREIGN KEY (`ref_incident_subcategory_id`) REFERENCES `ref_incident_subcategory` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `cop_incident_details` (`id`, `ref_incident_category_id`, `ref_incident_subcategory_id`, `incident_description`, `other_description`, `reference`, `qr_code`, `latitude`, `longitude`, `address`, `city`, `updated_on`, `created_by`, `is_deleted`, `status`, `updated_at`, `created_at`) VALUES
(1,	1,	1,	'testattackpolice',	'test',	10000,	'10000.png',	'46.1703186',	'-1.1929422',	'36 Rue Robert Desnos, 17000 La Rochelle, France',	'La Rochelle',	'2019-01-07 14:12:51',	12,	1,	3,	'2019-01-08 12:15:53',	'2019-01-07 14:12:51'),
(3,	1,	4,	'description 1',	'description 2',	10002,	'10002.png',	'48.9733859',	'2.510553',	'18 Place des Nymphéas, 93420 Villepinte, France',	'Villepinte',	'2019-01-08 11:04:23',	16,	1,	3,	'2019-01-08 11:16:39',	'2019-01-08 11:04:23'),
(4,	2,	10,	'desc1',	'desc2',	10003,	'10003.png',	'48.9734029',	'2.5105439',	'18 Place des Nymphéas, 93420 Villepinte, France',	'Villepinte',	'2019-01-08 11:23:32',	17,	1,	3,	'2019-01-08 11:59:52',	'2019-01-08 11:23:32'),
(5,	3,	14,	'1',	'2',	10004,	'10004.png',	'48.9734785',	'2.5106653',	'18 Place des Nymphéas, 93420 Villepinte, France',	'Villepinte',	'2019-01-08 12:34:17',	17,	1,	3,	'2019-01-08 12:37:02',	'2019-01-08 12:34:17'),
(6,	2,	13,	'azer',	'azerty',	10005,	'10005.png',	'48.9734773',	'2.5106799',	'18 Place des Nymphéas, 93420 Villepinte, France',	'Villepinte',	'2019-01-08 12:47:57',	20,	1,	1,	'2019-01-08 12:47:57',	'2019-01-08 12:47:57');

SET NAMES utf8mb4;

DROP TABLE IF EXISTS `cop_lat_long`;
CREATE TABLE `cop_lat_long` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ref_user_id` int(11) NOT NULL,
  `is_deleted` tinyint(4) NOT NULL DEFAULT '1',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `cop_user_device_mapping`;
CREATE TABLE `cop_user_device_mapping` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `ref_user_id` int(10) NOT NULL,
  `device_id` varchar(50) DEFAULT NULL,
  `created_on` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `ref_user_id` (`ref_user_id`),
  CONSTRAINT `cop_user_device_mapping_ibfk_1` FOREIGN KEY (`ref_user_id`) REFERENCES `ref_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `cop_user_device_mapping` (`id`, `ref_user_id`, `device_id`, `created_on`, `created_at`, `updated_at`, `is_deleted`) VALUES
(1,	11,	'378a0531a629e382',	NULL,	'2019-01-07 14:09:37',	'2019-01-07 14:09:37',	1),
(2,	12,	'378a0531a629e382',	NULL,	'2019-01-07 14:11:26',	'2019-01-07 14:11:26',	1),
(3,	13,	'581674d329445dfa',	NULL,	'2019-01-07 14:13:12',	'2019-01-07 14:13:12',	1),
(4,	14,	'581674d329445dfa',	NULL,	'2019-01-07 14:20:22',	'2019-01-07 14:20:22',	1),
(5,	15,	'ead9e8173c657b80',	NULL,	'2019-01-08 06:27:22',	'2019-01-08 06:27:22',	1),
(6,	16,	'b852762b1ed9c6cf',	NULL,	'2019-01-08 10:55:00',	'2019-01-08 10:55:00',	1),
(7,	17,	'b852762b1ed9c6cf',	NULL,	'2019-01-08 11:19:18',	'2019-01-08 11:19:18',	1),
(8,	18,	'b852762b1ed9c6cf',	NULL,	'2019-01-08 11:29:40',	'2019-01-08 11:29:40',	1),
(9,	19,	'b852762b1ed9c6cf',	NULL,	'2019-01-08 12:26:52',	'2019-01-08 12:26:52',	1),
(10,	20,	'b852762b1ed9c6cf',	NULL,	'2019-01-08 12:39:41',	'2019-01-08 12:39:41',	1);

DROP TABLE IF EXISTS `cop_user_handrails_closed`;
CREATE TABLE `cop_user_handrails_closed` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cop_handrail_id` bigint(20) NOT NULL,
  `comment` text NOT NULL,
  `created_by` int(11) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `signature` varchar(100) NOT NULL,
  `reference` varchar(100) NOT NULL,
  `ref_incident_status_id` tinyint(4) NOT NULL,
  `qr_code` varchar(100) NOT NULL,
  `is_deleted` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `cop_user_handrail_mapping`;
CREATE TABLE `cop_user_handrail_mapping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ref_user_id` int(10) NOT NULL,
  `cop_handrail_id` bigint(20) NOT NULL,
  `created_by` int(10) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `is_deleted` tinyint(4) NOT NULL DEFAULT '1',
  `status` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `ref_user_id` (`ref_user_id`),
  KEY `cop_handrail_id` (`cop_handrail_id`),
  KEY `created_by` (`created_by`),
  CONSTRAINT `cop_user_handrail_mapping_ibfk_1` FOREIGN KEY (`ref_user_id`) REFERENCES `ref_user` (`id`),
  CONSTRAINT `cop_user_handrail_mapping_ibfk_2` FOREIGN KEY (`cop_handrail_id`) REFERENCES `cop_handrail` (`id`),
  CONSTRAINT `cop_user_handrail_mapping_ibfk_3` FOREIGN KEY (`created_by`) REFERENCES `ref_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `cop_user_incidents_closed`;
CREATE TABLE `cop_user_incidents_closed` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cop_incident_details_id` bigint(20) NOT NULL,
  `comment` text NOT NULL,
  `created_by` int(11) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `ref_incident_status_id` tinyint(4) NOT NULL,
  `signature` varchar(100) NOT NULL,
  `reference` varchar(100) NOT NULL,
  `qr_code` varchar(100) NOT NULL,
  `is_deleted` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `cop_incident_details_id` (`cop_incident_details_id`),
  KEY `ref_incident_status_id` (`ref_incident_status_id`),
  CONSTRAINT `cop_user_incidents_closed_ibfk_1` FOREIGN KEY (`cop_incident_details_id`) REFERENCES `cop_incident_details` (`id`),
  CONSTRAINT `cop_user_incidents_closed_ibfk_2` FOREIGN KEY (`ref_incident_status_id`) REFERENCES `ref_incident_status` (`id`) ON DELETE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `cop_user_incidents_closed` (`id`, `cop_incident_details_id`, `comment`, `created_by`, `created_at`, `updated_at`, `ref_incident_status_id`, `signature`, `reference`, `qr_code`, `is_deleted`) VALUES
(1,	3,	'rapport',	16,	'2019-01-08 11:16:39',	'2019-01-08 11:16:39',	3,	'5c348697dfd74.jpg',	'700000',	'700000.png',	1),
(2,	4,	'rapport',	16,	'2019-01-08 11:59:52',	'2019-01-08 11:59:52',	3,	'5c3490b856b74.jpg',	'700002',	'700002.png',	1),
(3,	1,	'hduehdj',	16,	'2019-01-08 12:15:53',	'2019-01-08 12:15:53',	3,	'5c349478f3e03.jpg',	'700003',	'700003.png',	1),
(4,	5,	'test rapport',	16,	'2019-01-08 12:37:02',	'2019-01-08 12:37:02',	3,	'5c34996e1be3d.jpg',	'700004',	'700004.png',	1);

DROP TABLE IF EXISTS `cop_user_incident_mapping`;
CREATE TABLE `cop_user_incident_mapping` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `ref_user_id` int(10) NOT NULL,
  `cop_incident_details_id` bigint(20) NOT NULL,
  `created_by` int(10) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `is_deleted` tinyint(4) NOT NULL DEFAULT '1',
  `status` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `ref_user_id` (`ref_user_id`),
  KEY `cop_incident_details_id` (`cop_incident_details_id`),
  KEY `created_by` (`created_by`),
  CONSTRAINT `cop_user_incident_mapping_ibfk_1` FOREIGN KEY (`ref_user_id`) REFERENCES `ref_user` (`id`),
  CONSTRAINT `cop_user_incident_mapping_ibfk_2` FOREIGN KEY (`cop_incident_details_id`) REFERENCES `cop_incident_details` (`id`),
  CONSTRAINT `cop_user_incident_mapping_ibfk_3` FOREIGN KEY (`created_by`) REFERENCES `ref_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `cop_user_incident_mapping` (`id`, `ref_user_id`, `cop_incident_details_id`, `created_by`, `created_at`, `updated_at`, `is_deleted`, `status`) VALUES
(1,	16,	3,	16,	'2019-01-08 11:11:43',	'2019-01-08 11:16:39',	1,	3),
(2,	16,	4,	16,	'2019-01-08 11:59:02',	'2019-01-08 11:59:52',	1,	3),
(3,	16,	1,	16,	'2019-01-08 12:14:15',	'2019-01-08 12:15:53',	1,	3),
(4,	16,	5,	16,	'2019-01-08 12:36:16',	'2019-01-08 12:37:02',	1,	3);

DROP TABLE IF EXISTS `cop_user_incident_temp_mapping`;
CREATE TABLE `cop_user_incident_temp_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ref_user_id` int(10) NOT NULL,
  `cop_incident_details_id` bigint(20) NOT NULL,
  `ref_user_created_by` int(10) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `is_deleted` int(11) NOT NULL DEFAULT '1',
  `ref_incident_status_id` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `ref_user_id` (`ref_user_id`),
  KEY `cop_incident_details_id` (`cop_incident_details_id`),
  KEY `ref_user_created_by` (`ref_user_created_by`),
  KEY `ref_incident_status_id` (`ref_incident_status_id`),
  CONSTRAINT `cop_user_incident_temp_mapping_ibfk_1` FOREIGN KEY (`ref_user_id`) REFERENCES `ref_user` (`id`),
  CONSTRAINT `cop_user_incident_temp_mapping_ibfk_2` FOREIGN KEY (`cop_incident_details_id`) REFERENCES `cop_incident_details` (`id`),
  CONSTRAINT `cop_user_incident_temp_mapping_ibfk_3` FOREIGN KEY (`ref_user_created_by`) REFERENCES `ref_user` (`id`),
  CONSTRAINT `cop_user_incident_temp_mapping_ibfk_4` FOREIGN KEY (`ref_incident_status_id`) REFERENCES `ref_incident_status` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `cop_user_incident_temp_mapping` (`id`, `ref_user_id`, `cop_incident_details_id`, `ref_user_created_by`, `created_at`, `updated_at`, `is_deleted`, `ref_incident_status_id`) VALUES
(1,	16,	1,	2,	'2019-01-08 12:13:24',	'2019-01-08 12:13:24',	1,	1);

DROP TABLE IF EXISTS `migrations`;
CREATE TABLE `migrations` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `migration` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `batch` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `oauth_access_tokens`;
CREATE TABLE `oauth_access_tokens` (
  `id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `client_id` int(11) NOT NULL,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `scopes` text COLLATE utf8mb4_unicode_ci,
  `revoked` tinyint(1) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `expires_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `oauth_access_tokens_user_id_index` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `oauth_auth_codes`;
CREATE TABLE `oauth_auth_codes` (
  `id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` int(11) NOT NULL,
  `client_id` int(11) NOT NULL,
  `scopes` text COLLATE utf8mb4_unicode_ci,
  `revoked` tinyint(1) NOT NULL,
  `expires_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `oauth_clients`;
CREATE TABLE `oauth_clients` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `secret` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `redirect` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `personal_access_client` tinyint(1) NOT NULL,
  `password_client` tinyint(1) NOT NULL,
  `revoked` tinyint(1) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `oauth_clients_user_id_index` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `oauth_personal_access_clients`;
CREATE TABLE `oauth_personal_access_clients` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `client_id` int(11) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `oauth_personal_access_clients_client_id_index` (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `oauth_refresh_tokens`;
CREATE TABLE `oauth_refresh_tokens` (
  `id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `access_token_id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `revoked` tinyint(1) NOT NULL,
  `expires_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `oauth_refresh_tokens_access_token_id_index` (`access_token_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `password_resets`;
CREATE TABLE `password_resets` (
  `email` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `token` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  KEY `password_resets_email_index` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


DROP TABLE IF EXISTS `ref_city`;
CREATE TABLE `ref_city` (
  `id` tinyint(4) NOT NULL AUTO_INCREMENT,
  `city_name` varchar(100) DEFAULT NULL,
  `updated_on` datetime DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `ref_incident_category`;
CREATE TABLE `ref_incident_category` (
  `id` tinyint(4) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(70) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `icon` varchar(100) DEFAULT NULL,
  `help_line_no` varchar(20) DEFAULT NULL,
  `updated_on` datetime DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `ref_incident_category` (`id`, `category_name`, `description`, `icon`, `help_line_no`, `updated_on`, `is_deleted`) VALUES
(1,	'Police',	'Police Incident',	'img_police.png',	'1234567890',	NULL,	1),
(2,	'Medical/Fireman',	'Medical and Fireman incident',	'img_medical.png',	'1234567890',	NULL,	1),
(3,	'City',	'City incident',	'img_city.png',	'1234567890',	NULL,	1);

DROP TABLE IF EXISTS `ref_incident_status`;
CREATE TABLE `ref_incident_status` (
  `id` tinyint(4) NOT NULL AUTO_INCREMENT,
  `status_name` varchar(100) NOT NULL,
  `is_deleted` int(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `ref_incident_status` (`id`, `status_name`, `is_deleted`, `created_at`, `updated_at`) VALUES
(1,	'Waiting',	1,	'2018-12-13 07:23:06',	'2018-12-13 07:23:06'),
(2,	'Pending',	1,	'2018-12-13 07:23:18',	'2018-12-13 07:23:18'),
(3,	'Closed',	1,	'2018-12-13 07:23:42',	'2018-12-13 07:23:42'),
(4,	'Reopen',	1,	'2018-12-13 07:23:59',	'2018-12-13 07:23:59');

DROP TABLE IF EXISTS `ref_incident_subcategory`;
CREATE TABLE `ref_incident_subcategory` (
  `id` tinyint(4) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `ref_incident_category_id` int(11) NOT NULL,
  `sub_category_name` varchar(70) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `icon` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_deleted` tinyint(4) NOT NULL DEFAULT '1',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ref_incident_subcategory_sub_category_name_unique` (`sub_category_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `ref_incident_subcategory` (`id`, `ref_incident_category_id`, `sub_category_name`, `description`, `icon`, `is_deleted`, `created_at`, `updated_at`) VALUES
(0001,	1,	'Attack/voilence',	'',	'img_violence.png',	1,	NULL,	NULL),
(0002,	1,	'Theft',	'',	'img_thief.png',	1,	NULL,	NULL),
(0003,	1,	'Burglary',	'',	'img_burglary.png',	1,	NULL,	NULL),
(0004,	1,	'Sexual Voilence',	'',	'img_sexual_violence.png',	1,	NULL,	NULL),
(0005,	1,	'Body accident',	'',	'img_accident.png',	1,	NULL,	NULL),
(0006,	1,	'Other',	'',	'img_other.png',	1,	NULL,	NULL),
(0007,	2,	'Heart attack and malaise',	'',	'img_heart_attack.png',	1,	NULL,	NULL),
(0008,	2,	'Fire and scorch',	'',	'img_fire.png',	1,	NULL,	NULL),
(0009,	2,	'Hemorrhage, wound',	'',	'img_hemorragies.png',	1,	NULL,	NULL),
(0010,	2,	'Accident',	'',	'img_accident_fireman.png',	1,	NULL,	NULL),
(0011,	2,	'Airway obstruction',	'',	'img_airway.png',	1,	NULL,	NULL),
(0013,	2,	'Other Fireman',	'',	'img_other_fireman.png',	1,	NULL,	NULL),
(0014,	3,	'Tags on a public building',	'',	'img_tag.png',	1,	NULL,	NULL),
(0015,	3,	'Dirty place ( Street, Parc)',	'',	'img_dirtyplace.png',	1,	NULL,	NULL),
(0016,	3,	'Hole in the pavement or debris',	'',	'img_debris.png',	1,	NULL,	NULL),
(0017,	3,	'Lighting out of order',	'',	'img_light.png',	1,	NULL,	NULL),
(0018,	3,	'Urban furniture ( Panel, bus shelter)',	'',	'img_urban_furniture.png',	1,	NULL,	NULL),
(0019,	3,	'Other City',	'',	'img_other_reporting_ville.png',	1,	NULL,	NULL);

DROP TABLE IF EXISTS `ref_user`;
CREATE TABLE `ref_user` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ref_user_type_id` int(11) NOT NULL COMMENT '1=Cop, 2=Citizen',
  `user_password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `first_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `date_of_birth` date NOT NULL,
  `place_of_birth` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `gender` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone_number` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email_id` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `id_card1` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `id_card2` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `business_card1` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `business_card2` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `latitude` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `longitude` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `otp` int(11) NOT NULL,
  `verified` int(11) NOT NULL DEFAULT '0',
  `approved` tinyint(4) NOT NULL COMMENT '0=>pending, 1=>Approved, 2=>Reject',
  `level` tinyint(4) DEFAULT NULL COMMENT '1',
  `cops_grade` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'Grade I' COMMENT 'default=> Grade I, update=> Grade II',
  `available` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0=>Not available, 1=>Available',
  `is_deleted` tinyint(4) NOT NULL DEFAULT '1',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `profile_image` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `profile_qrcode` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `remember_token` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` int(11) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ref_user_user_id_unique` (`user_id`),
  UNIQUE KEY `ref_user_email_id_unique` (`email_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `ref_user` (`id`, `user_id`, `ref_user_type_id`, `user_password`, `first_name`, `last_name`, `date_of_birth`, `place_of_birth`, `gender`, `phone_number`, `email_id`, `id_card1`, `id_card2`, `business_card1`, `business_card2`, `latitude`, `longitude`, `otp`, `verified`, `approved`, `level`, `cops_grade`, `available`, `is_deleted`, `created_at`, `updated_at`, `profile_image`, `profile_qrcode`, `remember_token`, `status`) VALUES
(1,	'COPOPS5c178ec38fb0f',	5,	'$2y$10$/6IEe26jqrz1X8M/MO4a5OebGuvo7/m5azdG0F.h6blQj36hSzmZS',	'Operator 1',	'Operator 1',	'2001-01-22',	'Port Green',	'male',	'231-554-9316',	'dayana83@yahoo.com',	NULL,	NULL,	NULL,	NULL,	NULL,	NULL,	3,	1,	1,	1,	'Grade I',	1,	1,	'2018-12-17 06:25:47',	'2018-12-17 06:25:47',	NULL,	NULL,	'',	1),
(2,	'COPOPS5c178ec3a688e',	5,	'$2y$10$38lSHmt/11b6lvs0t3L4W.tZFWM16B9WnZYFppuKhhSODea0Wlllu',	'Operator 2',	'Operator 2',	'2002-02-25',	'South Herbertchester',	'female',	'+1-998-588-2738',	'rosendo99@hotmail.com',	NULL,	NULL,	NULL,	NULL,	NULL,	NULL,	3,	1,	1,	1,	'Grade I',	1,	1,	'2018-12-17 06:25:47',	'2018-12-17 06:25:47',	NULL,	NULL,	'nLu2Esx84uMt8ytUsHZtk9EJSArPuYICLBXUG9kBKqrrrZKRrd46qs830f5m',	1),
(3,	'COPOPS5c178ec3bab2a',	5,	'$2y$10$smNLq8tjR98vQmNzehJIMewwE7YKil6eSMiJIZcxdGZnwxEWk88ci',	'Operator 3',	'Operator 3',	'1978-09-09',	'Collierland',	'female',	'893.357.3414 x6',	'tessie.christiansen@wolff.com',	NULL,	NULL,	NULL,	NULL,	NULL,	NULL,	9,	1,	1,	1,	'Grade I',	1,	1,	'2018-12-17 06:25:47',	'2018-12-17 06:25:47',	NULL,	NULL,	'',	1),
(4,	'COPOPS5c178ec3d5387',	5,	'$2y$10$3lX65OqfSO6nTbLnxYI41OK5U5rM28sj/VCrwzvbkk.K8i4crJtTq',	'Operator 4',	'Operator 4',	'1993-09-25',	'North Nayelifurt',	'female',	'305.618.2802 x6',	'yhermiston@erdman.com',	NULL,	NULL,	NULL,	NULL,	NULL,	NULL,	5,	1,	1,	1,	'Grade I',	1,	1,	'2018-12-17 06:25:47',	'2018-12-17 06:25:47',	NULL,	NULL,	'',	1),
(5,	'COPOPS5c178ec3ef952',	5,	'$2y$10$xu3AWFHpNn61wZaZ.DIjruSSKEv8Sm3UKGReO/VAVWlXMsrOEHqVO',	'Operator 5',	'Operator 5',	'1993-10-10',	'Lake Lupe',	'female',	'1-235-761-2520 ',	'hope.weber@keeling.com',	NULL,	NULL,	NULL,	NULL,	NULL,	NULL,	7,	1,	1,	1,	'Grade I',	1,	1,	'2018-12-17 06:25:48',	'2018-12-17 06:25:48',	NULL,	NULL,	'',	1),
(6,	'COPOPS5c178ec40fea3',	5,	'$2y$10$ybjJtTfvVcVCKA2WHXlZgu1eFmhM4iYEbv.yifd6hVKuM3sOAm5xC',	'Operator 6',	'Operator 6',	'2009-07-12',	'South Pattiestad',	'female',	'+1-945-227-6368',	'ekuhic@hotmail.com',	NULL,	NULL,	NULL,	NULL,	NULL,	NULL,	9,	1,	1,	1,	'Grade I',	1,	1,	'2018-12-17 06:25:48',	'2018-12-17 06:25:48',	NULL,	NULL,	'',	1),
(7,	'COPOPS5c178ec4283d7',	5,	'$2y$10$dQdrxgslZ4W6ovxXBgwMrOV214i8rvKhWZMdNSa61Ff9B69C873Sm',	'Operator 7',	'Operator 7',	'1990-03-03',	'Greenfelderhaven',	'male',	'663.251.4104 x8',	'bernhard.aidan@emmerich.com',	NULL,	NULL,	NULL,	NULL,	NULL,	NULL,	9,	1,	1,	1,	'Grade I',	1,	1,	'2018-12-17 06:25:48',	'2018-12-17 06:25:48',	NULL,	NULL,	'',	1),
(8,	'COPOPS5c178ec43ca29',	5,	'$2y$10$ICAJLXVPeC2Ybe1oGSN8v.2JTGh5Eii3XlZoT9sSWk7yZe2c8wS/S',	'Operator 8',	'Operator 8',	'1996-10-21',	'Iciechester',	'male',	'536-355-3194 x0',	'bailey.judge@casper.org',	NULL,	NULL,	NULL,	NULL,	NULL,	NULL,	7,	1,	1,	1,	'Grade I',	1,	1,	'2018-12-17 06:25:48',	'2018-12-17 06:25:48',	NULL,	NULL,	'',	1),
(9,	'COPOPS5c178ec450e95',	5,	'$2y$10$eJPgi/X3C3at9bJv1ZBr9uZnLRXsfg2swi.TcXgxORH3uQl8Ts0LS',	'Operator 9',	'Operator 9',	'1974-07-27',	'Gradychester',	'female',	'362-673-7197',	'elena42@gmail.com',	NULL,	NULL,	NULL,	NULL,	NULL,	NULL,	7,	1,	1,	1,	'Grade I',	1,	1,	'2018-12-17 06:25:48',	'2018-12-17 06:25:48',	NULL,	NULL,	'',	1),
(10,	'COPOPS5c178ec46551a',	5,	'$2y$10$mubZ2SDgEboi2DdW86U3g.RPWpP18RuurS6tLlxR3OZpwf9BMMvW.',	'Operator 10',	'Operator 10',	'1974-02-20',	'South Junior',	'female',	'+1-708-208-3537',	'ziemann.german@hotmail.com',	NULL,	NULL,	NULL,	NULL,	NULL,	NULL,	2,	1,	1,	1,	'Grade I',	1,	1,	'2018-12-17 06:25:48',	'2018-12-17 06:25:48',	NULL,	NULL,	'',	1),
(11,	'5c335da1d8e6f',	4,	'123',	'singh',	'simran',	'2014-01-07',	'',	'Male',	'97979797979',	'ssimranjit0711@gmail.com',	NULL,	NULL,	NULL,	NULL,	'46.1703153',	'-1.1929175',	648802,	1,	1,	NULL,	'Grade I',	1,	1,	'2019-01-07 14:09:37',	'2019-01-07 14:10:38',	NULL,	'5c335da1d8e6f.png',	NULL,	1),
(12,	'5c335e0ed1340',	3,	'123',	'singh',	'simranjit',	'2014-09-07',	'',	'Male',	'97979499797',	'simranjitsinghdeveloper@gmail.com',	NULL,	NULL,	'5c335e0ed149c.jpg',	NULL,	'46.1703157',	'-1.1929108',	769507,	1,	1,	NULL,	'Grade II',	1,	1,	'2019-01-07 14:11:26',	'2019-01-07 14:12:15',	NULL,	'5c335e0ed1340.png',	NULL,	1),
(13,	'5c335e783e08f',	4,	'123456',	'John',	'Smith',	'1984-01-01',	'',	'Male',	'9876543210',	'rajputdev9971@gmail.com',	NULL,	NULL,	NULL,	NULL,	'28.6027946',	'77.3812659',	246516,	1,	1,	NULL,	'Grade I',	1,	1,	'2019-01-07 14:13:12',	'2019-01-07 14:13:55',	NULL,	'5c335e783e08f.png',	NULL,	1),
(14,	'5c33602670c00',	3,	'123456',	'Jack',	'Reacher',	'1986-01-07',	'',	'Male',	'0123456789',	'kamalvelocis@gmail.com',	NULL,	NULL,	NULL,	NULL,	'28.6027919',	'77.3812672',	146739,	1,	1,	NULL,	'Grade II',	1,	1,	'2019-01-07 14:20:22',	'2019-01-07 14:21:47',	NULL,	'5c33602670c00.png',	NULL,	1),
(15,	'5c3442ca6ca0c',	3,	'123',	'kumar',	'ranjan',	'2012-01-08',	'',	'Male',	'8080808080',	'ranjan.gupta@velocis.co.in',	'5c3442ca6cbfd.jpg',	'5c3442ca6cd93.jpg',	'5c3442ca6cefe.jpg',	'5c3442ca6d065.jpg',	'28.6027948',	'77.3812643',	809250,	1,	1,	NULL,	'Grade I',	1,	1,	'2019-01-08 06:27:22',	'2019-01-08 06:27:57',	NULL,	'5c3442ca6ca0c.png',	NULL,	1),
(16,	'5c348184df371',	3,	'123',	'limani',	'massy',	'1983-01-08',	'',	'Male',	'0659202553',	'massy.limani@appinn.fr',	NULL,	NULL,	NULL,	NULL,	'48.9734203',	'2.5105847',	991430,	1,	1,	NULL,	'Grade II',	1,	1,	'2019-01-08 10:55:00',	'2019-01-08 12:37:02',	NULL,	'5c348184df371.png',	NULL,	1),
(17,	'5c348736628b6',	4,	'123',	'limani',	'massy',	'1974-01-08',	'',	'Male',	'12345678900',	'contact@appinn.fr',	NULL,	NULL,	NULL,	NULL,	'48.9734213',	'2.5105918',	780344,	1,	1,	NULL,	'Grade I',	1,	1,	'2019-01-08 11:19:18',	'2019-01-08 11:19:48',	NULL,	'5c348736628b6.png',	NULL,	1),
(18,	'5c3489a4ac627',	3,	'123',	'jack',	'francois',	'1976-01-08',	'',	'Male',	'060504080907',	'azerty@hotmail.fr',	'5c3489a4ac82a.jpg',	'5c3489a4ac9c2.jpg',	'5c3489a4acaae.jpg',	'5c3489a4acc0a.jpg',	'48.9734243',	'2.5105948',	504083,	1,	1,	NULL,	'Grade I',	1,	1,	'2019-01-08 11:29:40',	'2019-01-08 12:41:27',	NULL,	'5c3489a4ac627.png',	NULL,	1),
(19,	'5c34970c4de3f',	4,	'123',	'citoyen',	'michel',	'1981-01-08',	'',	'Male',	'060402030406',	'mmas@live.fr',	NULL,	NULL,	NULL,	NULL,	'48.9734727',	'2.5106773',	547379,	0,	1,	NULL,	'Grade I',	1,	1,	'2019-01-08 12:26:52',	'2019-01-08 12:26:52',	NULL,	'5c34970c4de3f.png',	NULL,	1),
(20,	'5c349a0de7034',	3,	'123',	'michel',	'operateur',	'2019-01-08',	'',	'Male',	'060102030405',	'123@cops.com',	'5c349a0de71af.jpg',	NULL,	NULL,	NULL,	'48.9734803',	'2.5106944',	124263,	1,	1,	NULL,	'Grade II',	1,	1,	'2019-01-08 12:39:41',	'2019-01-08 12:43:08',	'5c349a0de7346.jpg',	'5c349a0de7034.png',	NULL,	1);

DROP TABLE IF EXISTS `ref_user_type`;
CREATE TABLE `ref_user_type` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `is_deleted` tinyint(4) NOT NULL DEFAULT '1',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `ref_user_type` (`id`, `user_type`, `is_deleted`, `created_at`, `updated_at`) VALUES
(1,	'Super Administrator',	1,	'2018-12-17 06:07:38',	'2018-12-17 06:07:38'),
(2,	'Administrator',	1,	'2018-12-17 06:07:38',	'2018-12-17 06:07:38'),
(3,	'Operator',	1,	'2018-12-17 06:07:38',	'2018-12-17 06:07:38'),
(4,	'Citizen',	1,	'2018-12-17 06:07:38',	'2018-12-17 06:07:38'),
(5,	'Backoffice',	1,	'2018-12-17 06:07:38',	'2018-12-17 06:07:38');

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email_verified_at` timestamp NULL DEFAULT NULL,
  `password` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `remember_token` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `users_email_unique` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- 2019-01-08 13:46:15
