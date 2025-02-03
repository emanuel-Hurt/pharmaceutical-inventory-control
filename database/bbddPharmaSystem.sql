CREATE DATABASE  IF NOT EXISTS `ddbb_farma` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `ddbb_farma`;
-- MySQL dump 10.13  Distrib 8.0.22, for macos10.15 (x86_64)
--
-- Host: localhost    Database: ddbb_farma
-- ------------------------------------------------------
-- Server version	5.7.30

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `client` (
  `cod_client` varchar(11) NOT NULL,
  `name_client` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`cod_client`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `passwords`
--

DROP TABLE IF EXISTS `passwords`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `passwords` (
  `id_password` int(11) NOT NULL AUTO_INCREMENT,
  `the_password` varchar(15) NOT NULL,
  `id_user` int(11) NOT NULL,
  PRIMARY KEY (`id_password`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `percentages`
--

DROP TABLE IF EXISTS `percentages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `percentages` (
  `id_percentage` int(11) NOT NULL AUTO_INCREMENT,
  `sale_percentage` decimal(10,2) NOT NULL,
  `discount_percentage` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id_percentage`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product_sales`
--

DROP TABLE IF EXISTS `product_sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_sales` (
  `id_sale` int(11) NOT NULL AUTO_INCREMENT,
  `quantity` int(11) NOT NULL,
  `total_price` decimal(10,2) NOT NULL,
  `id_sales_detail` int(11) NOT NULL,
  `id_product` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  PRIMARY KEY (`id_sale`),
  KEY `fk_sales_sales_details1_idx` (`id_sales_detail`),
  KEY `fk_product_sales_products1_idx` (`id_product`),
  KEY `fk_product_sales_users1_idx` (`id_user`),
  CONSTRAINT `fk_product_sales_products1` FOREIGN KEY (`id_product`) REFERENCES `products` (`id_product`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_product_sales_users1` FOREIGN KEY (`id_user`) REFERENCES `users` (`id_user`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_sales_sales_details1` FOREIGN KEY (`id_sales_detail`) REFERENCES `sales_details` (`id_sales_detail`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id_product` int(11) NOT NULL AUTO_INCREMENT,
  `name_product` varchar(45) NOT NULL,
  `pharma_form` varchar(20) NOT NULL,
  `concentration` varchar(25) NOT NULL,
  `due_date` date NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `existence` int(11) NOT NULL,
  `cod_product` varchar(10) DEFAULT NULL,
  `num_lote` varchar(20) DEFAULT NULL,
  `id_provider` int(11) NOT NULL,
  `generic_name` varchar(45) NOT NULL,
  PRIMARY KEY (`id_product`),
  KEY `fk_products_providers1_idx` (`id_provider`),
  CONSTRAINT `fk_products_providers1` FOREIGN KEY (`id_provider`) REFERENCES `providers` (`id_provider`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `providers`
--

DROP TABLE IF EXISTS `providers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `providers` (
  `id_provider` int(11) NOT NULL AUTO_INCREMENT,
  `name_provider` varchar(45) NOT NULL,
  `email` varchar(45) DEFAULT NULL,
  `phone` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_provider`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `purchase`
--

DROP TABLE IF EXISTS `purchase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase` (
  `id_purchase` int(11) NOT NULL AUTO_INCREMENT,
  `quantity` int(11) NOT NULL,
  `cost` decimal(10,2) NOT NULL,
  `id_product` int(11) NOT NULL,
  `purchase_date` date NOT NULL,
  `detail` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_purchase`),
  KEY `id_product_idx` (`id_product`),
  CONSTRAINT `id_product` FOREIGN KEY (`id_product`) REFERENCES `products` (`id_product`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id_rol` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id_rol`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sales_details`
--

DROP TABLE IF EXISTS `sales_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales_details` (
  `id_sales_detail` int(11) NOT NULL AUTO_INCREMENT,
  `date_sale` date NOT NULL,
  `total_price` decimal(10,2) NOT NULL,
  `description` varchar(300) DEFAULT NULL,
  `cod_client` varchar(11) NOT NULL,
  `hour_sale` time NOT NULL,
  PRIMARY KEY (`id_sales_detail`),
  KEY `fk_sales_details_client1_idx` (`cod_client`),
  CONSTRAINT `fk_sales_details_client1` FOREIGN KEY (`cod_client`) REFERENCES `client` (`cod_client`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `times_notifications`
--

DROP TABLE IF EXISTS `times_notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `times_notifications` (
  `id_time_notification` int(11) NOT NULL AUTO_INCREMENT,
  `quantity_to_notify` int(11) NOT NULL,
  PRIMARY KEY (`id_time_notification`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id_user` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `m_last_name` varchar(45) DEFAULT NULL,
  `cell_phone` int(11) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `password` varchar(62) NOT NULL,
  `id_rol` int(11) NOT NULL,
  `username` varchar(30) NOT NULL,
  PRIMARY KEY (`id_user`),
  KEY `fk_users_roles1_idx` (`id_rol`),
  CONSTRAINT `fk_users_roles1` FOREIGN KEY (`id_rol`) REFERENCES `roles` (`id_rol`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-03 15:24:09
