CREATE DATABASE `dbtest1`;

USE `dbtest1`;



DROP TABLE IF EXISTS `tb1`;

CREATE TABLE `tb1` (
  `id` int(11) NOT NULL,
  `gmt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE DATABASE `dbtest2`;

USE `dbtest2`;



DROP TABLE IF EXISTS `tb2`;

CREATE TABLE `tb2` (
  `id` int(11) NOT NULL,
  `val` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE DATABASE `dbtest3`;

USE `dbtest3`;

DROP TABLE IF EXISTS `tb2`;

CREATE TABLE `tb2` (
  `id` int(11) NOT NULL,
  `val` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;