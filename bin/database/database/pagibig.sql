-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: pagibig
-- ------------------------------------------------------
-- Server version	9.7.0

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
-- SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
-- SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

CREATE DATABASE IF NOT EXISTS `pagibig`;
USE `pagibig`;


-- SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '2220c749-43e5-11f1-ab7f-dc97bafab00d:1-136';

--
-- Table structure for table `companydetailstable`
--

DROP TABLE IF EXISTS `companydetailstable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `companydetailstable` (
  `Company_Code` varchar(10) NOT NULL,
  `Company_Name` varchar(255) NOT NULL,
  `Company_Address` varchar(255) NOT NULL,
  `Office_Assignment` enum('HEAD OFFICE','BRANCH') NOT NULL,
  `Branch_Location` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Company_Code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Company listed under the PagIbig database.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `companydetailstable`
--

LOCK TABLES `companydetailstable` WRITE;
/*!40000 ALTER TABLE `companydetailstable` DISABLE KEYS */;
INSERT INTO `companydetailstable` VALUES ('ACN','Accenture','Taguig City, PH','HEAD OFFICE',NULL),('AWS','Amazon Web Services','Quezon City, PH','HEAD OFFICE',NULL),('AWS1','Amazon Web Services','Pasay City, PH','HEAD OFFICE',NULL),('CVG','Converge','Pasig City, PH','HEAD OFFICE',NULL),('DICT','Department of Information and Communications Technology (Republic of Philippines)','Quezon City, PH','HEAD OFFICE',NULL),('JPM','JP Morgan Chase Company','New York City, USA','HEAD OFFICE',NULL),('ORC','Oracle','Taguig City, PH','BRANCH','Makati City'),('PLDT','Philippine Long Distance Telephone Company','Quezon City, PH','BRANCH','Caloocan City'),('PUP','Polytechnic University of the Philippines - Sta. Mesa','Manila City, PH','HEAD OFFICE',NULL),('STECH','Seanna Tech','Quezon City, PH','HEAD OFFICE',NULL);
/*!40000 ALTER TABLE `companydetailstable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `currentemprecordtable`
--

DROP TABLE IF EXISTS `currentemprecordtable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `currentemprecordtable` (
  `PagIbig_MID_No` char(14) NOT NULL,
  `Company_Code` varchar(10) NOT NULL,
  `Occupation` varchar(50) NOT NULL,
  `Employment_Status` enum('PERMANENT/REGULAR','CASUAL','CONTRACTUAL','PROJECT BASED','PART-TIME/TEMPORARY') DEFAULT NULL,
  `TypeOfWork` enum('LAND-BASED','SEA-BASED') DEFAULT NULL,
  `Country_Of_Assignment` varchar(25) NOT NULL,
  `Date_Employed` date NOT NULL,
  PRIMARY KEY (`PagIbig_MID_No`),
  UNIQUE KEY `PagIbig_MID_No_UNIQUE` (`PagIbig_MID_No`),
  KEY `fk_CurrentEmpT_CompT_idx` (`Company_Code`),
  CONSTRAINT `fk_CurrentEmpT_CompT` FOREIGN KEY (`Company_Code`) REFERENCES `companydetailstable` (`Company_Code`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_CurrentEmpT_MemberT` FOREIGN KEY (`PagIbig_MID_No`) REFERENCES `membertable` (`PagIbig_MID_No`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Current employment record of member.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `currentemprecordtable`
--

LOCK TABLES `currentemprecordtable` WRITE;
/*!40000 ALTER TABLE `currentemprecordtable` DISABLE KEYS */;
INSERT INTO `currentemprecordtable` VALUES ('1212-3434-5656','STECH','Network Engineer','CONTRACTUAL',NULL,'Philippines','2028-11-11'),('1212-3434-5657','PUP','Data Analyst','PERMANENT/REGULAR',NULL,'Philippines','2028-11-12'),('1212-3434-5658','CVG','Network Engineer','CASUAL',NULL,'Philippines','2028-11-13'),('1212-3434-5659','JPM','Data Analyst','PROJECT BASED','LAND-BASED','U.S.A','2028-11-14');
/*!40000 ALTER TABLE `currentemprecordtable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `heirstable`
--

DROP TABLE IF EXISTS `heirstable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `heirstable` (
  `PagIbig_MID_No` char(14) NOT NULL,
  `Heir_Code` int NOT NULL AUTO_INCREMENT,
  `Heirs_Name` varchar(50) DEFAULT NULL,
  `Heirs_Relationship` varchar(20) DEFAULT NULL,
  `Heirs_Birthdate` date NOT NULL,
  PRIMARY KEY (`PagIbig_MID_No`,`Heir_Code`),
  UNIQUE KEY `PagIbig_MID_No_UNIQUE` (`PagIbig_MID_No`),
  UNIQUE KEY `Heir_Code_UNIQUE` (`Heir_Code`),
  CONSTRAINT `fk_HeirsT_MemberT` FOREIGN KEY (`PagIbig_MID_No`) REFERENCES `membertable` (`PagIbig_MID_No`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Members table for dependents.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `heirstable`
--

LOCK TABLES `heirstable` WRITE;
/*!40000 ALTER TABLE `heirstable` DISABLE KEYS */;
/*!40000 ALTER TABLE `heirstable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `membertable`
--

DROP TABLE IF EXISTS `membertable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `membertable` (
  `PagIbig_MID_No` char(14) NOT NULL,
  `Occupational_Status` enum('EMPLOYED','UNEMPLOYED','FIRST TIME JOBSEEKERS') NOT NULL,
  `Membership_Type` enum('EMPLOYED','OVERSEAS FILIPINO WORKER','SELF-EMPLOYED') NOT NULL,
  `Membership_Type_Others` varchar(100) DEFAULT NULL,
  `Membership_Category` enum('PRIVATE','GOVERNMENT','PRIVATE HOUSEHOLD','OVERSEAS FILIPINO WORKER','PROFESSIONAL/BUSINESS OWNER','JOB ORDER PERSONNEL','OTHER EARNING GROUPS') NOT NULL,
  `Membership_Category_Others` varchar(100) DEFAULT NULL,
  `Member_Name` varchar(50) NOT NULL,
  `Father_Name` varchar(50) DEFAULT NULL,
  `Mother_Name` varchar(50) DEFAULT NULL,
  `Spouse_Name` varchar(50) DEFAULT NULL,
  `Birthdate` date NOT NULL,
  `Marital_Status` enum('SINGLE','MARRIED','WIDOWED','LEGALLY SEPARATED','ANNULED') NOT NULL,
  `Birthplace` varchar(45) NOT NULL,
  `Citizenship` varchar(10) NOT NULL,
  `Sex` enum('MALE','FEMALE') NOT NULL,
  `CRN` char(12) DEFAULT NULL,
  `Frequency_Of_Membership_Savings` varchar(15) NOT NULL,
  `TIN` char(14) DEFAULT NULL,
  `SSS` char(12) DEFAULT NULL,
  `Employee_Number` int DEFAULT NULL,
  `Present_Home_Address` varchar(255) NOT NULL,
  `Permanent_Home_Address` varchar(255) NOT NULL,
  `Preferred_Mailing_Address` varchar(25) NOT NULL,
  `Home_TelNum` varchar(20) DEFAULT NULL,
  `Cellphone_Num` varchar(13) NOT NULL,
  `Bus_DirectLine` varchar(20) DEFAULT NULL,
  `Bus_TrunkLine` varchar(20) DEFAULT NULL,
  `Local` varchar(6) DEFAULT NULL,
  `Email_Address` varchar(255) NOT NULL,
  `Allow_Basic` decimal(10,2) NOT NULL,
  `Allow_Other_Sources` decimal(10,2) DEFAULT NULL,
  `Total_Mo_Income` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`PagIbig_MID_No`),
  UNIQUE KEY `PagIbig_MID_No_UNIQUE` (`PagIbig_MID_No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Member''s relevant details.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `membertable`
--

LOCK TABLES `membertable` WRITE;
/*!40000 ALTER TABLE `membertable` DISABLE KEYS */;
INSERT INTO `membertable` VALUES ('1212-3434-5656','EMPLOYED','EMPLOYED',NULL,'PRIVATE',NULL,'Robert D. Sarmiento','Daniel M. Sarmiento','Georgia L. Diana','Cynthia W. Sarmiento','1990-11-21','MARRIED','Quezon City','Filipino','MALE','226000000000','Monthly','123-456-789-01','13-2345678-0',2147483647,'810 Southroad St., Jude Luxury Homes, Tandang Sora, Quezon City, Metro Manila','810 Southroad St., Jude Luxury Homes, Tandang Sora, Quezon City, Metro Manila','Present Home Address','(02) 8123-4567','0917-123-4567','(02) 8888-9999','(02) 8555-0000','101','robertoboypaos@gmail.com',120000.00,40000.00,160000.00),('1212-3434-5657','EMPLOYED','EMPLOYED',NULL,'GOVERNMENT',NULL,'Migel H. Tan','Michael T. Tan','Angelica H. Tan','Spouse Tan','2006-03-12','MARRIED','Valenzuela City','Filipino','MALE','225501000000','Quarterly','123-456-789-02','13-2345678-1',2147483647,'#23 La-Huerta Subdivision, Marulas, Valenzuela City','#23 La-Huerta Subdivision, Marulas, Valenzuela City','Present Home Address','(02) 8123-4568','0956-740-1232','(02) 8888-10000','(02) 8555-0001','102','migellltan@gmail.com',250000.00,90000.00,340000.00),('1212-3434-5658','EMPLOYED','OVERSEAS FILIPINO WORKER',NULL,'OVERSEAS FILIPINO WORKER',NULL,'James Escanillas','Father Escanillas','Mother Escanillas','Spouse Escanillas','2006-12-03','MARRIED','Quezon City','Filipino','MALE','226000000001','Monthly','123-456-789-03','13-2345678-2',2147483647,'154 Olibas St. Nawasa Side Brgy Pasong Tamo QC','154 Olibas St. Nawasa Side Brgy Pasong Tamo QC','Permanent Home Address','(02) 8123-4569','0992-843-9679','(02) 8888-10001','(02) 8555-0002','103','jamesescanillas3@gmail.com',250000.00,90000.00,340000.00),('1212-3434-5659','EMPLOYED','EMPLOYED',NULL,'PRIVATE HOUSEHOLD',NULL,'Raven Rayo','Father Rayo','Mother Rayo','Spouse Surname','2005-09-07','MARRIED','Bulacan','Filipino','FEMALE','225501000001','Monthly','123-456-789-04','13-2345678-3',2147483647,'Bliss, Poblacion, Norzagaray Bulacan','Bliss, Poblacion, Norzagaray Bulacan','Employer/Business Address','(02) 8123-4570','0967-738-5677','(02) 8888-10002','(02) 8555-0003','104','ravenjoyce07@gmail.com',250000.00,90000.00,340000.00);
/*!40000 ALTER TABLE `membertable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prevemptable`
--

DROP TABLE IF EXISTS `prevemptable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prevemptable` (
  `PagIbig_MID_No` char(14) NOT NULL,
  `Prev_Emp_Code` int NOT NULL AUTO_INCREMENT,
  `Company_Code` varchar(10) NOT NULL,
  `To_Date` date NOT NULL,
  `From_Date` date NOT NULL,
  PRIMARY KEY (`Prev_Emp_Code`,`PagIbig_MID_No`),
  UNIQUE KEY `Prev_Emp_Code_UNIQUE` (`Prev_Emp_Code`),
  KEY `PagIbig_MID_No_idx` (`PagIbig_MID_No`),
  KEY `fk_PrevEmpT_CompT_idx` (`Company_Code`),
  CONSTRAINT `fk_PrevEmpT_CompT` FOREIGN KEY (`Company_Code`) REFERENCES `companydetailstable` (`Company_Code`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_PrevEmpT_MemberT` FOREIGN KEY (`PagIbig_MID_No`) REFERENCES `membertable` (`PagIbig_MID_No`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Previous employment table of member.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prevemptable`
--

LOCK TABLES `prevemptable` WRITE;
/*!40000 ALTER TABLE `prevemptable` DISABLE KEYS */;
INSERT INTO `prevemptable` VALUES ('1212-3434-5656',1,'AWS','2029-03-01','2028-07-01'),('1212-3434-5656',2,'ORC','2033-09-01','2031-09-01'),('1212-3434-5656',3,'CVG','2031-10-01','2031-01-01'),('1212-3434-5657',4,'ACN','2030-02-01','2028-02-01'),('1212-3434-5657',5,'PUP','2040-02-01','2030-02-01'),('1212-3434-5658',6,'PLDT','2029-09-01','2028-03-01'),('1212-3434-5659',7,'AWS1','2030-01-01','2028-01-01');
/*!40000 ALTER TABLE `prevemptable` ENABLE KEYS */;
UNLOCK TABLES;

-- SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-16 20:32:54
