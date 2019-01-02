-- phpMyAdmin SQL Dump
-- version 4.8.2
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le :  mer. 02 jan. 2019 à 21:47
-- Version du serveur :  10.1.34-MariaDB
-- Version de PHP :  7.2.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `gestionmagasin7`
--

-- --------------------------------------------------------

--
-- Structure de la table `categorie`
--

CREATE TABLE `categorie` (
  `codecateg` bigint(20) NOT NULL,
  `intitule` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `categorie`
--

INSERT INTO `categorie` (`codecateg`, `intitule`) VALUES
(1, 'Téléphones & Tablettes'),
(2, 'Santé & Beauté'),
(3, 'Maison et bureau'),
(4, 'Jeux vidéos & Consoles'),
(5, 'Articles de sport'),
(6, 'Produits pour bébés'),
(7, 'Jouets et Jeux'),
(8, 'Livres, Films et Musique');

-- --------------------------------------------------------

--
-- Structure de la table `client`
--

CREATE TABLE `client` (
  `id` bigint(20) NOT NULL,
  `nom` varchar(255) NOT NULL,
  `prenom` varchar(255) NOT NULL,
  `tele` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `adresse` varchar(255) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `client`
--

INSERT INTO `client` (`id`, `nom`, `prenom`, `tele`, `email`, `adresse`) VALUES
(1, 'ARIBA', 'Ilyas', '0673390987', 'ilyas.ariba@gmail.com', '16-1 Rissani El-Qods Errachidia 52450'),
(2, 'IDBOUKORI', 'Ali', '087654567', 'Idboukori@gmail.com', 'Dchira 23 Agadir'),
(3, 'Mekdad', 'Ayoub', '06765435676', 'Mekdad.Ayoube@gmail.com', 'Tmara Rabat 34 n89'),
(4, 'TAGHAZOUT', 'Imad', '0654356789', 'Imad.Taghazout@hotmail.com', 'Midelt 365 Almassira 56765'),
(5, 'Hillal', 'Zoubir', '0765435787', 'Zoubir.Hilal@yahoo.fr', 'Casablanca Anfa 45654'),
(6, 'Naji', 'Abdelwahab', '0654345676', 'abdelwahab.naji@gmail.com', 'Mohammedia Enset 45'),
(7, 'Fathi', 'Fatima', '07654367876', 'Fatima.Fathi@yahoo.com', 'Fes Elmassira 654'),
(8, 'Alaoui', 'Ahmed', '065456789', 'ahmed.alaoui@gmail.com', 'Rabat El Riad 347'),
(9, 'Ziani', 'Mohammed', '0565435634567', 'mohammed.76@gmail.com', 'Tanger n565');

-- --------------------------------------------------------

--
-- Structure de la table `lc`
--

CREATE TABLE `lc` (
  `codelc` bigint(20) NOT NULL,
  `qt` int(11) NOT NULL,
  `soustotal` double NOT NULL DEFAULT '0',
  `codeprd` bigint(20) NOT NULL,
  `codevente` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `lc`
--

INSERT INTO `lc` (`codelc`, `qt`, `soustotal`, `codeprd`, `codevente`) VALUES
(56, 2, 3222, 5, 30),
(55, 1, 4444, 6, 29),
(54, 3, 3222, 5, 29),
(53, 2, 3422, 3, 29);

-- --------------------------------------------------------

--
-- Structure de la table `produit`
--

CREATE TABLE `produit` (
  `code` bigint(20) NOT NULL,
  `designation` varchar(255) DEFAULT NULL,
  `prixAchat` double NOT NULL,
  `prixVente` double NOT NULL,
  `codecat` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `produit`
--

INSERT INTO `produit` (`code`, `designation`, `prixAchat`, `prixVente`, `codecat`) VALUES
(3, 'RUMEIAI New Sport Suit Hoodie Batman Hooded Men Casual Cotton Fall', 2324, 3422, 2),
(5, 'Baseus Smart Folio Case for 12.9 iPad Pro Case 2018 Magnetic Auto Sleep Wake Up Case for Apple iPad 2018 Case for 11 iPad Pro', 2344, 3222, 1),
(6, 'Intel Windows 10 Mini PC Win Pro G2 CR Z3736F Quad Core CPU Bluetooth 4.0 Wi-Fi 2MP Camera Microcomputer Host', 3444, 4444, 1),
(7, 'Onebot 21.5in All in One Gaming Desktop Computer Mini PC Dual Core Intel 4GB DDR4 RAM', 456666, 754444, 3);

-- --------------------------------------------------------

--
-- Structure de la table `reglement`
--

CREATE TABLE `reglement` (
  `id` bigint(20) NOT NULL,
  `titulaire` varchar(255) DEFAULT NULL,
  `datecheque` date DEFAULT NULL,
  `datepp` date DEFAULT NULL,
  `datepe` date DEFAULT NULL,
  `montant` double NOT NULL,
  `type` varchar(255) NOT NULL,
  `codev` bigint(20) NOT NULL,
  `etat` varchar(255) DEFAULT NULL,
  `isTrait` int(11) NOT NULL DEFAULT '0'
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `reglement`
--

INSERT INTO `reglement` (`id`, `titulaire`, `datecheque`, `datepp`, `datepe`, `montant`, `type`, `codev`, `etat`, `isTrait`) VALUES
(47, '', NULL, NULL, NULL, 899, 'espese', 29, '', 1),
(48, '', NULL, NULL, NULL, 8777, 'cheque', 29, '', 1),
(49, '', NULL, NULL, NULL, 6444, 'espese', 30, '', 0);

-- --------------------------------------------------------

--
-- Structure de la table `vente`
--

CREATE TABLE `vente` (
  `codev` bigint(20) NOT NULL,
  `datev` date NOT NULL,
  `totalv` int(11) NOT NULL,
  `idclient` bigint(20) NOT NULL,
  `stat` varchar(255) NOT NULL DEFAULT 'encour'
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `vente`
--

INSERT INTO `vente` (`codev`, `datev`, `totalv`, `idclient`, `stat`) VALUES
(30, '2019-01-01', 6444, 2, 'termine'),
(29, '2019-01-01', 20954, 1, 'encour');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `categorie`
--
ALTER TABLE `categorie`
  ADD PRIMARY KEY (`codecateg`);

--
-- Index pour la table `client`
--
ALTER TABLE `client`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `lc`
--
ALTER TABLE `lc`
  ADD PRIMARY KEY (`codelc`);

--
-- Index pour la table `produit`
--
ALTER TABLE `produit`
  ADD PRIMARY KEY (`code`),
  ADD KEY `FKm8la4t761sy7g7nphpjx5twbg` (`codecat`);

--
-- Index pour la table `reglement`
--
ALTER TABLE `reglement`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `vente`
--
ALTER TABLE `vente`
  ADD PRIMARY KEY (`codev`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `categorie`
--
ALTER TABLE `categorie`
  MODIFY `codecateg` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT pour la table `client`
--
ALTER TABLE `client`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT pour la table `lc`
--
ALTER TABLE `lc`
  MODIFY `codelc` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=57;

--
-- AUTO_INCREMENT pour la table `produit`
--
ALTER TABLE `produit`
  MODIFY `code` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT pour la table `reglement`
--
ALTER TABLE `reglement`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=50;

--
-- AUTO_INCREMENT pour la table `vente`
--
ALTER TABLE `vente`
  MODIFY `codev` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `produit`
--
ALTER TABLE `produit`
  ADD CONSTRAINT `FKm8la4t761sy7g7nphpjx5twbg` FOREIGN KEY (`codecat`) REFERENCES `categorie` (`codecateg`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
