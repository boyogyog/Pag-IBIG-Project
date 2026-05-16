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
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '2220c749-43e5-11f1-ab7f-dc97bafab00d:1-91';

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
  UNIQUE KEY `PagIbig_MID_No_UNIQUE` (`PagIbig_MID_No`),
  KEY `PagIbig_MID_No_idx` (`PagIbig_MID_No`),
  KEY `fk_PrevEmpT_CompT_idx` (`Company_Code`),
  CONSTRAINT `fk_PrevEmpT_CompT` FOREIGN KEY (`Company_Code`) REFERENCES `companydetailstable` (`Company_Code`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_PrevEmpT_MemberT` FOREIGN KEY (`PagIbig_MID_No`) REFERENCES `membertable` (`PagIbig_MID_No`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Previous employment table of member.';
/*!40101 SET character_set_client = @saved_cs_client */;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-02  0:53:31
