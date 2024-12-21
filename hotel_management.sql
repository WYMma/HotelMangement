-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Dec 21, 2024 at 02:22 AM
-- Server version: 8.0.31
-- PHP Version: 8.3.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `hotel_management`
--

-- --------------------------------------------------------

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
CREATE TABLE IF NOT EXISTS `account` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `role` varchar(20) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `account`
--

INSERT INTO `account` (`id`, `username`, `password`, `email`, `first_name`, `last_name`, `phone`, `role`, `active`, `created_at`) VALUES
(1, 'admin', 'admin123', 'admin@hotel.com', 'Admin', 'User', '1234567890', 'admin', 1, '2024-12-20 11:27:49'),
(2, 'agent', 'agent123', 'agent@hotel.com', 'Flen', 'Fouleni', '93726082', 'agent', 1, '2024-12-20 11:27:49'),
(3, 'agent2', 'agent123', 'agent@agent.fr', 'Agent', 'System', '93726082', 'agent', 1, '2024-12-20 12:26:23'),
(6, 'test', 'test123', 'agent@test.com', 'Agent', 'System', '93726082', 'agent', 1, '2024-12-20 12:33:49');

-- --------------------------------------------------------

--
-- Table structure for table `hotel`
--

DROP TABLE IF EXISTS `hotel`;
CREATE TABLE IF NOT EXISTS `hotel` (
  `id` int NOT NULL AUTO_INCREMENT,
  `agent_id` int NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` text,
  `city` varchar(100) NOT NULL,
  `address` varchar(200) NOT NULL,
  `stars` int NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `agent_id` (`agent_id`)
) ;

--
-- Dumping data for table `hotel`
--

INSERT INTO `hotel` (`id`, `agent_id`, `name`, `description`, `city`, `address`, `stars`, `image`, `created_at`) VALUES
(1, 2, 'Dar Djerba', 'Un hôtel de catégorie 4 étoiles, une vraie merveille pour les amoureux de la mer et des vacances estivales au bord de l’une des plus belles plages au monde, celle de de Djerba! un refuge unique pour se détendre entre les bras de la nature et de l’authenticité de l’endroit.\r\nSituation de l’hôtel Dar Jerba Narjess\r\nSitué à Djerba, l’île des rêves! l’hôtel Dar Jerba Narjess est à quelques minutes du parc Djerba explore; l’un des endroits les plus visités de Djerba. l’hôtel est à seulement quelques kilomètres de la ville Midoun. La galerie d’art Lalla Hadria est aussi à proximité pour une visite complète de la ville.\r\nHébergement à l’hôtel Dar Jerba Narjess\r\nLes chambres de l’hôtel Dar Jerba Narjess sont très bien équipées afin de rendre votre séjour inoubliable. Les chambres de cet établissement se distinguent par une décoration des plus raffinées pour des vacances en douceur. 335 perles sont là pour accueillir les hôtes de Dar Jerba Narjess; des chambres triples, quadruples, communicantes et des chambres spéciales pour les personnes à mobilité réduite. Toutes les nécessités du confort existent dans les chambres de Dar Jerba Narjess y compris le sèche-cheveux, le coffre-fort, le téléphone et la TV à satéllite.\r\nRestaurant &amp;amp;amp;amp; Bars à l’hôtel Dar Jerba Narjess\r\nL’hôtel dispose d’un restaurant buffet pour le petit déjeuner, un restaurant Tunisien: Sultan, un restaurant italien: l’Italiano, un bar central appelé La Terrasse pour des moments de relaxation, un autre bar au bord de la piscine pour et un bar au bord de la plage pour des baignades inoubliables. Dar Jerba Narjess permet des moments de détente et d’échanges dans son café maure: Le Narguilé et son snack-bar.\r\nActivités et Loisirs à l’hôtel Dar Jerba Narjess\r\nl’hôtel Dar Jerba Narjess offre à ses hôtes deux piscines extérieures dont une pour enfants, une piscine couverte L’hôtel donne un accès à 4 courts de tennis, un terrain de Basket-ball un Mini terrain de football et un autre de Beach-volley - les résidents de Dar Jerba Nerjess peuvent pratiquer plusieurs sports comme la gymnastique, le stretching, l’aérobic, le water -polo et plein d’autres activités à découvrir une fois à l’hôtel!', 'MEDNINE', 'Djerba, Midoun', 4, 'https:&amp;#x2F;&amp;#x2F;image.resabooking.com&amp;#x2F;images&amp;#x2F;image_panoramique&amp;#x2F;monarque_dar_jerba_zahra_2.jpg', '2024-12-20 11:29:59');

-- --------------------------------------------------------

--
-- Table structure for table `reservations`
--

DROP TABLE IF EXISTS `reservations`;
CREATE TABLE IF NOT EXISTS `reservations` (
  `id` int NOT NULL AUTO_INCREMENT,
  `hotel_id` int NOT NULL,
  `room_type_id` int NOT NULL,
  `guest_name` varchar(100) NOT NULL,
  `guest_email` varchar(100) NOT NULL,
  `guest_phone` varchar(20) NOT NULL,
  `check_in_date` date NOT NULL,
  `check_out_date` date NOT NULL,
  `number_of_guests` int NOT NULL,
  `special_requests` text,
  `status` varchar(20) NOT NULL DEFAULT 'PENDING',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `hotel_id` (`hotel_id`),
  KEY `room_type_id` (`room_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `reservations`
--

INSERT INTO `reservations` (`id`, `hotel_id`, `room_type_id`, `guest_name`, `guest_email`, `guest_phone`, `check_in_date`, `check_out_date`, `number_of_guests`, `special_requests`, `status`, `created_at`) VALUES
(1, 1, 2, 'Yassin Manita', 'yassinmanita12@gmail.com', '93726082', '2024-12-20', '2024-12-24', 2, NULL, 'PENDING', '2024-12-20 11:54:25'),
(2, 1, 1, 'Yassin Manita', 'yassinmanita12@gmail.com', '93726082', '2024-12-21', '2024-12-26', 1, NULL, 'PENDING', '2024-12-20 11:56:27'),
(3, 1, 1, 'Yassin Manita', 'yassinmanita12@gmail.com', '93726082', '2024-12-20', '2024-12-21', 1, NULL, 'PENDING', '2024-12-20 12:07:47'),
(4, 1, 1, 'Yassin Manita', 'yassinmanita12@gmail.com', '93726082', '2024-12-20', '2024-12-25', 1, NULL, 'PENDING', '2024-12-20 12:28:34');

-- --------------------------------------------------------

--
-- Table structure for table `room_type`
--

DROP TABLE IF EXISTS `room_type`;
CREATE TABLE IF NOT EXISTS `room_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `hotel_id` int NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` text,
  `capacity` int NOT NULL,
  `price_per_night` decimal(10,2) NOT NULL,
  `available_rooms` int NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `hotel_id` (`hotel_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `room_type`
--

INSERT INTO `room_type` (`id`, `hotel_id`, `name`, `description`, `capacity`, `price_per_night`, `available_rooms`, `created_at`) VALUES
(1, 1, 'Standard Single', 'Cozy room with a single bed, perfect for solo travelers', 1, 99.99, 5, '2024-12-20 11:41:19'),
(2, 1, 'Standard Double', 'Comfortable room with a double bed', 2, 149.99, 8, '2024-12-20 11:41:19'),
(3, 1, 'Standard Twin', 'Room with two single beds', 2, 159.99, 6, '2024-12-20 11:41:19'),
(4, 1, 'Deluxe Double', 'Spacious room with a queen-size bed and city view', 2, 199.99, 4, '2024-12-20 11:41:19'),
(5, 1, 'Deluxe Suite', 'Luxury suite with separate living area and king-size bed', 3, 299.99, 3, '2024-12-20 11:41:19'),
(6, 1, 'Family Room', 'Large room with one double and two single beds', 4, 259.99, 4, '2024-12-20 11:41:19'),
(7, 1, 'Family Suite', 'Two-bedroom suite perfect for families', 6, 399.99, 2, '2024-12-20 11:41:19'),
(8, 1, 'Honeymoon Suite', 'Romantic suite with king-size bed and jacuzzi', 2, 349.99, 1, '2024-12-20 11:41:19'),
(9, 1, 'Executive Suite', 'Premium suite with office space and luxury amenities', 2, 449.99, 2, '2024-12-20 11:41:19');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `hotel`
--
ALTER TABLE `hotel`
  ADD CONSTRAINT `hotel_ibfk_1` FOREIGN KEY (`agent_id`) REFERENCES `account` (`id`);

--
-- Constraints for table `reservations`
--
ALTER TABLE `reservations`
  ADD CONSTRAINT `reservations_ibfk_1` FOREIGN KEY (`hotel_id`) REFERENCES `hotel` (`id`),
  ADD CONSTRAINT `reservations_ibfk_2` FOREIGN KEY (`room_type_id`) REFERENCES `room_type` (`id`);

--
-- Constraints for table `room_type`
--
ALTER TABLE `room_type`
  ADD CONSTRAINT `room_type_ibfk_1` FOREIGN KEY (`hotel_id`) REFERENCES `hotel` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
