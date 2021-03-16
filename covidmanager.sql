-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : mar. 09 mars 2021 à 16:13
-- Version du serveur :  5.7.31
-- Version de PHP : 7.4.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `covidmanager`
--

-- --------------------------------------------------------

--
-- Structure de la table `manage`
--

DROP TABLE IF EXISTS `manage`;
CREATE TABLE IF NOT EXISTS `manage` (
  `idSlot` int(11) NOT NULL AUTO_INCREMENT,
  `idSecratary` int(11) NOT NULL,
  `action` varchar(50) COLLATE latin1_general_ci NOT NULL,
  `manageDate` datetime DEFAULT NULL,
  PRIMARY KEY (`idSlot`,`idSecratary`),
  KEY `idSecratary` (`idSecratary`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `secratary`
--

DROP TABLE IF EXISTS `secratary`;
CREATE TABLE IF NOT EXISTS `secratary` (
  `idSecratary` int(11) NOT NULL AUTO_INCREMENT,
  `lastname` varchar(50) COLLATE latin1_general_ci DEFAULT NULL,
  `login` varchar(50) COLLATE latin1_general_ci DEFAULT NULL,
  `password` varchar(500) COLLATE latin1_general_ci DEFAULT NULL,
  `firstname` varchar(50) COLLATE latin1_general_ci DEFAULT NULL,
  PRIMARY KEY (`idSecratary`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

--
-- Déchargement des données de la table `secratary`
--

INSERT INTO `secratary` (`idSecratary`, `lastname`, `login`, `password`, `firstname`) VALUES
(1, 'Marit', 'victor.marit', '$2y$10$3y2id3lvf73szCQkw1J75.S9fFKpC1G5kxd75lxjlOzJCTkq6pOpK', 'Victor'),
(2, 'Daouk', 'jad.daouk', '$2y$10$3y2id3lvf73szCQkw1J75.S9fFKpC1G5kxd75lxjlOzJCTkq6pOpK', 'Jad'),
(3, 'Hage', 'gregoire.hage', '$2y$10$3y2id3lvf73szCQkw1J75.S9fFKpC1G5kxd75lxjlOzJCTkq6pOpK', 'Grégoire'),
(4, 'Jeanjean', 'adrien.jeanjean', '$2y$10$3y2id3lvf73szCQkw1J75.S9fFKpC1G5kxd75lxjlOzJCTkq6pOpK', 'Adrien');

-- --------------------------------------------------------

--
-- Structure de la table `slot`
--

DROP TABLE IF EXISTS `slot`;
CREATE TABLE IF NOT EXISTS `slot` (
  `idSlot` int(11) NOT NULL AUTO_INCREMENT,
  `startTime` datetime NOT NULL,
  `endTime` datetime NOT NULL,
  `isActive` tinyint(1) NOT NULL,
  PRIMARY KEY (`idSlot`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `vaccine`
--

DROP TABLE IF EXISTS `vaccine`;
CREATE TABLE IF NOT EXISTS `vaccine` (
  `idVaccin` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE latin1_general_ci DEFAULT NULL,
  PRIMARY KEY (`idVaccin`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

--
-- Déchargement des données de la table `vaccine`
--

INSERT INTO `vaccine` (`idVaccin`, `name`) VALUES
(1, 'Pfizer'),
(2, 'Astra Zeneca '),
(3, 'Moderna');

-- --------------------------------------------------------

--
-- Structure de la table `vial`
--

DROP TABLE IF EXISTS `vial`;
CREATE TABLE IF NOT EXISTS `vial` (
  `idVial` int(11) NOT NULL AUTO_INCREMENT,
  `shotNumber` int(11) NOT NULL,
  `idSlot` int(11) DEFAULT NULL,
  `idVaccin` int(11) NOT NULL,
  PRIMARY KEY (`idVial`),
  KEY `idSlot` (`idSlot`),
  KEY `idVaccin` (`idVaccin`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `manage`
--
ALTER TABLE `manage`
  ADD CONSTRAINT `manage_ibfk_1` FOREIGN KEY (`idSlot`) REFERENCES `slot` (`idSlot`),
  ADD CONSTRAINT `manage_ibfk_2` FOREIGN KEY (`idSecratary`) REFERENCES `secratary` (`idSecratary`);

--
-- Contraintes pour la table `vial`
--
ALTER TABLE `vial`
  ADD CONSTRAINT `vial_ibfk_1` FOREIGN KEY (`idSlot`) REFERENCES `slot` (`idSlot`),
  ADD CONSTRAINT `vial_ibfk_2` FOREIGN KEY (`idVaccin`) REFERENCES `vaccine` (`idVaccin`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
