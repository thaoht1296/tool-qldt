-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database:`jpn_crawl_qldt`
--

-- --------------------------------------------------------

--
-- Table structure for table `schedules`
--

CREATE TABLE `schedules` (
  `schedule_id` varchar(256) NOT NULL,
  `subject_name` varchar(256) NOT NULL,
  `subject_code` varchar(256) NOT NULL,
  `num_of_credits` int(11) NOT NULL,
  `day_of_week` varchar(256) NOT NULL,
  `date` date NOT NULL,
  `lesson_start` int(11) NOT NULL,
  `num_of_lesson` int(11) NOT NULL,
  `time_start` time NOT NULL,
  `teacher` varchar(256) NOT NULL,
  `class_name` varchar(256) NOT NULL,
  `room` varchar(256) NOT NULL,
  `week` varchar(256) NOT NULL,
  `semester` varchar(256) NOT NULL,
  `created` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `users_qldt`
--

CREATE TABLE `users_qldt` (
  `username_qldt` varchar(11) NOT NULL,
  `password_qldt` varchar(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `users_qldt_schedules`
--

CREATE TABLE `users_qldt_schedules` (
  `username_qldt` varchar(11) NOT NULL,
  `schedule_id` varchar(256) NOT NULL,
  `created` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


--
-- Table structure for table `tbl_ann`
--

CREATE TABLE `tbl_ann` (
  `id` int(11) NOT NULL,
  `url` varchar(1000) NOT NULL,
  `date` varchar(50) NOT NULL,
  `title` varchar(6000) NOT NULL,
  `content` tinytext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


--
-- Indexes for table `schedules`
--
ALTER TABLE `schedules`
  ADD PRIMARY KEY (`schedule_id`);

--
-- Indexes for table `users_qldt`
--
ALTER TABLE `users_qldt`
  ADD PRIMARY KEY (`username_qldt`);

--
-- Indexes for table `users_qldt_schedules`
--
ALTER TABLE `users_qldt_schedules`
  ADD PRIMARY KEY (`username_qldt`,`schedule_id`);
  
--
-- Indexes for table `users_qldt`
--
ALTER TABLE `tbl_ann`
  ADD PRIMARY KEY (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
