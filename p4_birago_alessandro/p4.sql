-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Creato il: Dic 11, 2017 alle 18:18
-- Versione del server: 5.7.19
-- Versione PHP: 5.6.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `p4`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `apps`
--

DROP TABLE IF EXISTS `apps`;
CREATE TABLE IF NOT EXISTS `apps` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(11) NOT NULL,
  `google` tinyint(1) NOT NULL DEFAULT '0',
  `finsa` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `apps`
--

INSERT INTO `apps` (`id`, `nome`, `google`, `finsa`) VALUES
(4, 'app', 1, 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `datiappfinsa`
--

DROP TABLE IF EXISTS `datiappfinsa`;
CREATE TABLE IF NOT EXISTS `datiappfinsa` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data` date DEFAULT NULL,
  `TotDw` int(11) DEFAULT NULL,
  `DayInstalls` int(11) DEFAULT NULL,
  `TotRate` float DEFAULT NULL,
  `DayRate` float DEFAULT NULL,
  `DayCrash` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `datiappfinsa`
--

INSERT INTO `datiappfinsa` (`id`, `data`, `TotDw`, `DayInstalls`, `TotRate`, `DayRate`, `DayCrash`) VALUES
(1, '2017-05-05', 116, 119, 5, 5, NULL),
(2, '2017-05-10', 15581, 2626, 4.86, 4.97, NULL),
(3, '2017-05-15', 34002, 3825, 4.54, 4.24, 0),
(4, '2017-05-09', 13123, 3511, 4.64, 4.2, 2),
(5, '2017-05-14', 30332, 4286, 4.68, 4.54, 3),
(6, '2017-05-08', 9821, 3991, 4.76, 4.85, NULL),
(7, '2017-05-13', 26223, 3961, 4.61, 4.96, 1),
(8, '2017-05-17', NULL, 3649, NULL, NULL, 1),
(9, '2017-05-06', 3122, 3109, 4.73, 4.73, NULL),
(10, '2017-05-11', 18401, 2978, 4.77, 4.18, NULL),
(11, '2017-05-16', 36768, 2926, 4.48, 4.14, 1),
(12, '2017-05-07', 5981, 3001, 4.7, 4.65, 2),
(13, '2017-05-12', 22445, 4244, 4.41, 3.84, 2);

-- --------------------------------------------------------

--
-- Struttura della tabella `datiappgoogle`
--

DROP TABLE IF EXISTS `datiappgoogle`;
CREATE TABLE IF NOT EXISTS `datiappgoogle` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data` date DEFAULT NULL,
  `TotDw` int(11) DEFAULT NULL,
  `DayInstalls` int(11) DEFAULT NULL,
  `TotRate` float DEFAULT NULL,
  `DayRate` float DEFAULT NULL,
  `DayCrash` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `datiappgoogle`
--

INSERT INTO `datiappgoogle` (`id`, `data`, `TotDw`, `DayInstalls`, `TotRate`, `DayRate`, `DayCrash`) VALUES
(1, '2017-05-05', 116, 119, 5, 5, NULL),
(2, '2017-05-10', 15581, 2626, 4.85, 4.97, NULL),
(3, '2017-05-15', 34002, 3825, 4.69, 4.24, 0),
(4, '2017-05-09', 13123, 3511, 4.85, 4.85, 2),
(5, '2017-05-14', 30332, 4286, 4.72, 4.54, 3),
(6, '2017-05-08', 9821, 3991, 4.84, 4.76, NULL),
(7, '2017-05-13', 26223, 3961, 4.73, 3.96, 1),
(8, '2017-05-17', NULL, NULL, NULL, NULL, 1),
(9, '2017-05-06', 3122, 3109, 4.87, 4.86, NULL),
(10, '2017-05-11', 18401, 2978, 4.8, 4.18, NULL),
(11, '2017-05-16', 36768, 2926, 4.65, 4.14, 1),
(12, '2017-05-07', 5981, 3001, 4.86, 4.82, 2),
(13, '2017-05-12', 22445, 4244, 4.77, 4.49, 2);

-- --------------------------------------------------------

--
-- Struttura della tabella `datiappgooglefinsa`
--

DROP TABLE IF EXISTS `datiappgooglefinsa`;
CREATE TABLE IF NOT EXISTS `datiappgooglefinsa` (
  `data` date NOT NULL,
  `TotDw` int(11) DEFAULT NULL,
  `TotRate` float DEFAULT NULL,
  `DayRate` float DEFAULT NULL,
  `DayCrash` int(11) DEFAULT NULL,
  PRIMARY KEY (`data`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `datiappgooglefinsa`
--

INSERT INTO `datiappgooglefinsa` (`data`, `TotDw`, `TotRate`, `DayRate`, `DayCrash`) VALUES
('2017-05-05', 232, 5, 5, 0),
('2017-05-10', 31162, 4.855, 4.97, 0),
('2017-05-15', 68004, 4.615, 4.24, 0),
('2017-05-09', 26246, 4.745, 4.525, 4),
('2017-05-14', 60664, 4.7, 4.54, 6),
('2017-05-08', 19642, 4.8, 4.805, 0),
('2017-05-13', 52446, 4.67, 4.46, 2),
('2017-05-17', 0, 0, 0, 2),
('2017-05-06', 6244, 4.8, 4.795, 0),
('2017-05-11', 36802, 4.785, 4.18, 0),
('2017-05-16', 73536, 4.565, 4.14, 2),
('2017-05-07', 11962, 4.78, 4.735, 4),
('2017-05-12', 44890, 4.59, 4.165, 4);

-- --------------------------------------------------------

--
-- Struttura della tabella `datiappgoogle_old`
--

DROP TABLE IF EXISTS `datiappgoogle_old`;
CREATE TABLE IF NOT EXISTS `datiappgoogle_old` (
  `Date` date NOT NULL,
  `PackageName` varchar(100) DEFAULT NULL,
  `Visitors` int(11) DEFAULT NULL,
  `Installers` int(11) DEFAULT NULL,
  `Buyers` int(11) DEFAULT NULL,
  `DailyCrashes` int(11) DEFAULT NULL,
  `DailyANRs` int(11) DEFAULT NULL,
  `DailyDeviceInstalls` int(11) DEFAULT NULL,
  `DailyDeviceUninstalls` int(11) DEFAULT NULL,
  `DailyDeviceUpgrades` int(11) DEFAULT NULL,
  `TotalUserInstalls` int(11) DEFAULT NULL,
  `DailyUserInstalls` int(11) DEFAULT NULL,
  `DailyUserUninstalls` int(11) DEFAULT NULL,
  `ActiveDeviceInstalls` int(11) DEFAULT NULL,
  `DailyAverageRating` float DEFAULT NULL,
  `TotalAverageRating` float DEFAULT NULL,
  `InstallersRetainedFor7Days` int(11) DEFAULT NULL,
  `InstallersTo7DaysRetentionRate` float DEFAULT NULL,
  `InstallersRetainedFor15Days` int(11) DEFAULT NULL,
  `InstallersTo15DaysRetentionRate` float DEFAULT NULL,
  `InstallersRetainedFor30Days` int(11) DEFAULT NULL,
  `InstallersTo30DaysRetentionRate` float DEFAULT NULL,
  PRIMARY KEY (`Date`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `datiappgoogle_old`
--

INSERT INTO `datiappgoogle_old` (`Date`, `PackageName`, `Visitors`, `Installers`, `Buyers`, `DailyCrashes`, `DailyANRs`, `DailyDeviceInstalls`, `DailyDeviceUninstalls`, `DailyDeviceUpgrades`, `TotalUserInstalls`, `DailyUserInstalls`, `DailyUserUninstalls`, `ActiveDeviceInstalls`, `DailyAverageRating`, `TotalAverageRating`, `InstallersRetainedFor7Days`, `InstallersTo7DaysRetentionRate`, `InstallersRetainedFor15Days`, `InstallersTo15DaysRetentionRate`, `InstallersRetainedFor30Days`, `InstallersTo30DaysRetentionRate`) VALUES
('2017-08-21', 'com.app.google', 8, 2, 105, 978, 88, 25, 847, 26817862, 8456, 256, 82, 27, 2.5, 99.9, 3, 0.8, 5, 0.7, 7, 0.5);

-- --------------------------------------------------------

--
-- Struttura della tabella `datiappgoogle_old2`
--

DROP TABLE IF EXISTS `datiappgoogle_old2`;
CREATE TABLE IF NOT EXISTS `datiappgoogle_old2` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data` date DEFAULT NULL,
  `TotDw` int(11) DEFAULT NULL,
  `TotRate` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `datiappgoogle_old2`
--

INSERT INTO `datiappgoogle_old2` (`id`, `data`, `TotDw`, `TotRate`) VALUES
(1, '2017-05-05', 116, 5),
(2, '2017-05-10', 15581, 5),
(3, '2017-05-15', 34002, 5),
(4, '2017-05-09', 13123, 5),
(5, '2017-05-14', 30332, 5),
(6, '2017-05-08', 9821, 5),
(7, '2017-05-13', 26223, 5),
(8, '2017-05-17', NULL, NULL),
(9, '2017-05-06', 3122, 5),
(10, '2017-05-11', 18401, 5),
(11, '2017-05-16', 36768, 5),
(12, '2017-05-07', 5981, 5),
(13, '2017-05-12', 22445, 5);

-- --------------------------------------------------------

--
-- Struttura della tabella `store`
--

DROP TABLE IF EXISTS `store`;
CREATE TABLE IF NOT EXISTS `store` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `store`
--

INSERT INTO `store` (`id`, `nome`) VALUES
(0, 'google'),
(1, 'finsa');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
