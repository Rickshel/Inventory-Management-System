-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 21, 2023 at 02:38 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `imsdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `productchanges`
--

CREATE TABLE `productchanges` (
  `ChangeID` int(11) NOT NULL,
  `ProductID` int(11) DEFAULT NULL,
  `ColumnName` varchar(255) DEFAULT NULL,
  `OldValue` varchar(255) DEFAULT NULL,
  `NewValue` varchar(255) DEFAULT NULL,
  `Timestamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `productchanges`
--

INSERT INTO `productchanges` (`ChangeID`, `ProductID`, `ColumnName`, `OldValue`, `NewValue`, `Timestamp`) VALUES
(1, 5, 'Name', 'Computer', 'Computer', '2023-12-20 16:38:55'),
(2, 5, 'Description', 'Expensive', 'Expensive', '2023-12-20 16:38:55'),
(3, 5, 'Quantity', '21', '4', '2023-12-20 16:38:55'),
(4, 5, 'ReorderPoint', '1', '1', '2023-12-20 16:38:55'),
(5, 3, 'Name', 'chair', 'chair', '2023-12-20 16:38:56'),
(6, 3, 'Description', 'not so cool', 'not so cool', '2023-12-20 16:38:56'),
(7, 3, 'Quantity', '25', '2', '2023-12-20 16:38:56'),
(8, 3, 'ReorderPoint', '0', '0', '2023-12-20 16:38:56'),
(9, 6, 'Name', 'Monitor', 'Monitor', '2023-12-21 09:22:01'),
(10, 6, 'Description', 'Keyboard', 'Keyboard', '2023-12-21 09:22:01'),
(11, 6, 'Quantity', '0', '5', '2023-12-21 09:22:01'),
(12, 6, 'ReorderPoint', '1', '1', '2023-12-21 09:22:01'),
(13, 5, 'Name', 'Computer', 'Computer', '2023-12-21 09:22:01'),
(14, 5, 'Description', 'Expensive', 'Expensive', '2023-12-21 09:22:01'),
(15, 5, 'Quantity', '0', '9', '2023-12-21 09:22:01'),
(16, 5, 'ReorderPoint', '1', '1', '2023-12-21 09:22:01'),
(17, 7, 'Name', 'Dragon', 'Dragon', '2023-12-21 09:22:01'),
(18, 7, 'Description', 'Very scary', 'Very scary', '2023-12-21 09:22:01'),
(19, 7, 'Quantity', '0', '16', '2023-12-21 09:22:01'),
(20, 7, 'ReorderPoint', '1', '1', '2023-12-21 09:22:01'),
(21, 3, 'Name', 'chair', 'chair', '2023-12-21 11:18:23'),
(22, 3, 'Description', 'not so cool', 'not so cool', '2023-12-21 11:18:23'),
(23, 3, 'Quantity', '2', '5', '2023-12-21 11:18:23'),
(24, 3, 'ReorderPoint', '0', '0', '2023-12-21 11:18:23'),
(25, 6, 'Name', 'Monitor', 'Monitor', '2023-12-21 11:18:23'),
(26, 6, 'Description', 'Keyboard', 'Keyboard', '2023-12-21 11:18:23'),
(27, 6, 'Quantity', '5', '5', '2023-12-21 11:18:23'),
(28, 6, 'ReorderPoint', '1', '0', '2023-12-21 11:18:23');

-- --------------------------------------------------------

--
-- Table structure for table `productmovements`
--

CREATE TABLE `productmovements` (
  `MovementID` int(11) NOT NULL,
  `ProductID` int(11) DEFAULT NULL,
  `MovementType` enum('Receipt','Transfer','Sale') NOT NULL,
  `Quantity` int(11) NOT NULL,
  `Timestamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `productmovements`
--

INSERT INTO `productmovements` (`MovementID`, `ProductID`, `MovementType`, `Quantity`, `Timestamp`) VALUES
(2, 1, 'Receipt', 1, '2023-12-17 04:46:25'),
(3, 1, 'Receipt', 1, '2023-12-19 10:19:23');

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `ProductID` int(11) NOT NULL,
  `Name` varchar(255) NOT NULL,
  `Description` text DEFAULT NULL,
  `Quantity` int(11) NOT NULL,
  `ReorderPoint` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`ProductID`, `Name`, `Description`, `Quantity`, `ReorderPoint`) VALUES
(1, 'Laptop Pro', 'High-performance laptop', 20, 1),
(2, 'Laptop', 'Powerful laptop', 10, 1),
(3, 'chair', 'not so cool', 5, 0),
(5, 'Computer', 'Expensive', 0, 1),
(6, 'Monitor', 'Keyboard', 55, 0),
(7, 'Dragon', 'Very scary', 13, 1);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `UserID` int(11) NOT NULL,
  `Username` varchar(255) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `UserType` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`UserID`, `Username`, `Password`, `UserType`) VALUES
(1, 'admin', 'admin', 'Administrator'),
(3, 'John', 'john', 'Warehouse Staff'),
(4, 'Staff', 'staff', 'Inventory Manager'),
(6, 'manage', 'manage', 'Warehouse Staff');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `productchanges`
--
ALTER TABLE `productchanges`
  ADD PRIMARY KEY (`ChangeID`),
  ADD KEY `ProductID` (`ProductID`);

--
-- Indexes for table `productmovements`
--
ALTER TABLE `productmovements`
  ADD PRIMARY KEY (`MovementID`),
  ADD KEY `ProductID` (`ProductID`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`ProductID`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`UserID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `productchanges`
--
ALTER TABLE `productchanges`
  MODIFY `ChangeID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT for table `productmovements`
--
ALTER TABLE `productmovements`
  MODIFY `MovementID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `ProductID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `UserID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `productchanges`
--
ALTER TABLE `productchanges`
  ADD CONSTRAINT `productchanges_ibfk_1` FOREIGN KEY (`ProductID`) REFERENCES `products` (`ProductID`);

--
-- Constraints for table `productmovements`
--
ALTER TABLE `productmovements`
  ADD CONSTRAINT `productmovements_ibfk_1` FOREIGN KEY (`ProductID`) REFERENCES `products` (`ProductID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
