-- phpMyAdmin SQL Dump
-- version 4.8.0.1
-- https://www.phpmyadmin.net/
--
-- Хост: 127.0.0.1
-- Время создания: Дек 28 2019 г., 15:51
-- Версия сервера: 10.1.32-MariaDB
-- Версия PHP: 7.2.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `itea_bd`
--

-- --------------------------------------------------------

--
-- Структура таблицы `points`
--

CREATE TABLE `points` (
  `id` int(11) UNSIGNED NOT NULL,
  `lng` float(10,6) NOT NULL,
  `lat` float(10,6) NOT NULL,
  `tag` varchar(50) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

--
-- Дамп данных таблицы `points`
--

INSERT INTO `points` (`id`, `lng`, `lat`, `tag`) VALUES
(1, 55.760246, 37.625271, 'cinema'),
(2, 55.767921, 37.629108, 'cafe'),
(3, 55.760963, 37.624516, 'art'),
(4, 55.762012, 37.626835, 'cafe'),
(5, 55.758640, 37.629299, 'cafe'),
(6, 55.762260, 37.622143, 'art'),
(7, 55.757473, 37.617630, 'cinema'),
(8, 55.773445, 37.631641, 'cinema');

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `points`
--
ALTER TABLE `points`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `points`
--
ALTER TABLE `points`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
