-- DROP FLYWAY
 DROP TABLE `pagibig`.`flyway_schema_history`;

-- FIX SOME ERRORS
UPDATE `pagibig`.`companydetailstable` SET `Company_Name` = 'Oracle Philippines' WHERE (`Company_Code` = 'ORC1');
UPDATE `pagibig`.`companydetailstable` SET `Office_Assignment` = 'BRANCH' WHERE (`Company_Code` = 'AWS1');
UPDATE `pagibig`.`companydetailstable` SET `Office_Assignment` = 'HEAD OFFICE', `Branch_Location` = '' WHERE (`Company_Code` = 'BPI');


-- Add 10 companies and 10 records
-- ============================================================
-- 10 New Companies
-- ============================================================
INSERT IGNORE INTO companydetailstable
  (Company_Code, Company_Name, Company_Address, Office_Assignment, Branch_Location)
VALUES
('GLO','Globe Telecom','Taguig City, PH','HEAD OFFICE',NULL),
('SMPH','SM Prime Holdings','Pasay City, PH','HEAD OFFICE',NULL),
('BDO','Banco de Oro Unibank','Makati City, PH','HEAD OFFICE',NULL),
('MNL','Manila Electric Company (Meralco)','Pasig City, PH','HEAD OFFICE',NULL),
('GCH','GCash (Mynt)','Taguig City, PH','HEAD OFFICE',NULL),
('DOH','Department of Health','Manila City, PH','HEAD OFFICE',NULL),
('NTT','NTT Data Philippines','Makati City, PH','BRANCH','Bonifacio Global City, Taguig'),
('IBM','IBM Philippines','Makati City, PH','HEAD OFFICE',NULL),
('SAP','SAP Philippines','Taguig City, PH','BRANCH','Bonifacio Global City, Taguig'),
('DOST','Department of Science and Technology','Taguig City, PH','HEAD OFFICE',NULL),
('PUP1','Polytechnic University of the Philippines - Taguig', 'Manila City, PH', 'BRANCH','Taguig City'),
('PUP2','Polytechnic University of the Philippines - Paranaque', 'Manila City, PH', 'BRANCH','Paranaque City'),
('PUP3','Polytechnic University of the Philippines - Caloocan', 'Manila City, PH', 'BRANCH','Caloocan City'),
('PUP4','Polytechnic University of the Philippines - SMB', 'Manila City, PH', 'BRANCH','Sta. Maria, Bulacan');



-- ============================================================
-- 10 New Members (MID 5721–5730)
-- ============================================================
INSERT INTO membertable
  (PagIbig_MID_No, Occupational_Status, Membership_Type, Membership_Type_Others,
   Membership_Category, Membership_Category_Others, Member_Name, Father_Name, Mother_Name,
   Spouse_Name, Birthdate, Marital_Status, Birthplace, Citizenship, Sex, CRN,
   Frequency_Of_Membership_Savings, TIN, SSS, Employee_Number,
   Present_Home_Address, Permanent_Home_Address, Preferred_Mailing_Address,
   Home_TelNum, Cellphone_Num, Bus_DirectLine, Bus_TrunkLine, Local,
   Email_Address, Allow_Basic, Allow_Other_Sources, Total_Mo_Income)
VALUES
-- 1. Male, Married, Private, Globe
('1212-3434-5721','EMPLOYED','EMPLOYED',NULL,'PRIVATE',NULL,
 'Marco Luis A. Reyes','Alfredo C. Reyes','Estrella A. Delos Santos','Pia B. Reyes',
 '1988-04-11','MARRIED','Quezon City','Filipino','MALE','226000600101',
 'Monthly','567-890-234-01','05-2345679-0',40001,
 '45 Mapagkawanggawa St., Brgy. Pinyahan, Quezon City',
 '45 Mapagkawanggawa St., Brgy. Pinyahan, Quezon City',
 'Present Home Address','(02) 8411-3300','0917-211-3300',
 '(02) 8730-1000',NULL,'701',
 'marco.reyes@gmail.com',58000.00,7000.00,65000.00),

-- 2. Female, Single, Private, SM Prime
('1212-3434-5722','EMPLOYED','EMPLOYED',NULL,'PRIVATE',NULL,
 'Hannah Grace C. Soriano','Cesar D. Soriano','Nilda C. Bautista',NULL,
 '1997-09-03','SINGLE','Parañaque City','Filipino','FEMALE','226000600102',
 'Monthly','567-890-234-02','05-2345679-1',40002,
 'Blk 8 Lot 12, BF Homes, Parañaque City',
 'Blk 8 Lot 12, BF Homes, Parañaque City',
 'Present Home Address',NULL,'0945-322-4411',NULL,NULL,NULL,
 'hannah.soriano@gmail.com',32000.00,NULL,32000.00),

-- 3. Male, Married, Government, DOH
('1212-3434-5723','EMPLOYED','EMPLOYED',NULL,'GOVERNMENT',NULL,
 'Dr. Raul F. Aguilar','Fidel M. Aguilar','Cecilia F. Perez','Linda C. Aguilar',
 '1976-01-28','MARRIED','Iloilo City','Filipino','MALE','226000600103',
 'Monthly','567-890-234-03','05-2345679-2',40003,
 'No. 7 Dahlia St., Brgy. Don Bosco, Parañaque City',
 'No. 7 Dahlia St., Brgy. Don Bosco, Parañaque City',
 'Present Home Address','(02) 8842-5500','0923-433-5500',
 '(02) 8651-7800',NULL,'801',
 'raul.aguilar@doh.gov.ph',85000.00,12000.00,97000.00),

-- 4. Female, Married, OFW, Sea-based, NTT
('1212-3434-5724','EMPLOYED','OVERSEAS FILIPINO WORKER',NULL,'PRIVATE',NULL,
 'Ana Kristine D. Ferrer','Domingo L. Ferrer','Luz D. Villanueva','Mark A. Ferrer',
 '1984-07-19','MARRIED','Batangas City','Filipino','FEMALE','226000600104',
 'Monthly','567-890-234-04','05-2345679-3',40004,
 'Phase 3 Sunrise Villas, Lipa City, Batangas',
 'Phase 3 Sunrise Villas, Lipa City, Batangas',
 'Permanent Home Address',NULL,'0920-544-6622',NULL,NULL,NULL,
 'ana.ferrer@gmail.com',92000.00,NULL,92000.00),

-- 5. Male, Single, Private, IBM
('1212-3434-5725','EMPLOYED','EMPLOYED',NULL,'PRIVATE',NULL,
 'Lorenzo B. Santos','Benjamin O. Santos','Cristina B. Ocampo',NULL,
 '1993-12-25','SINGLE','Makati City','Filipino','MALE','226000600105',
 'Monthly','567-890-234-05','05-2345679-4',40005,
 '3F The Columns, Makati City','3F The Columns, Makati City',
 'Present Home Address','(02) 8816-4400','0917-655-7733',
 '(02) 8816-5500','(02) 8816-6600','901',
 'lorenzo.santos@gmail.com',110000.00,20000.00,130000.00),

-- 6. Female, Widowed, Government, DOST
('1212-3434-5726','EMPLOYED','EMPLOYED',NULL,'GOVERNMENT',NULL,
 'Dr. Evelyn M. Cruz','Marcelo P. Magsino','Norma M. Aquino',NULL,
 '1969-06-06','WIDOWED','Laguna','Filipino','FEMALE','226000600106',
 'Quarterly','567-890-234-06','05-2345679-5',40006,
 'No. 14 Acacia Ave., Brgy. Bagong Ilog, Pasig City',
 'No. 14 Acacia Ave., Brgy. Bagong Ilog, Pasig City',
 'Present Home Address','(02) 8641-2222','0928-766-8844',
 '(02) 8837-2000',NULL,'1001',
 'evelyn.cruz@dost.gov.ph',95000.00,15000.00,110000.00),

-- 7. Male, Married, First Time Jobseeker, GCash
('1212-3434-5727','FIRST TIME JOBSEEKERS','EMPLOYED',NULL,'PRIVATE',NULL,
 'Andrei Kyle P. Lim','Philip J. Lim','Maricel P. Ong','Sophia A. Lim',
 '2001-03-07','MARRIED','Taguig City','Filipino','MALE','226000600107',
 'Monthly','567-890-234-07','05-2345679-6',40007,
 'Unit 5B McKinley Hill Garden Villas, Taguig City',
 'Unit 5B McKinley Hill Garden Villas, Taguig City',
 'Present Home Address',NULL,'0956-877-9955',NULL,NULL,NULL,
 'andrei.lim@gmail.com',26000.00,NULL,26000.00),

-- 8. Female, Single, Self-Employed, SAP
('1212-3434-5728','EMPLOYED','SELF-EMPLOYED',NULL,'PROFESSIONAL/BUSINESS OWNER',NULL,
 'Bianca Isabelle R. Tan','Ricardo S. Tan','Gloria R. Chan',NULL,
 '1990-10-31','SINGLE','Mandaluyong City','Filipino','FEMALE','226000600108',
 'Monthly','567-890-234-08','05-2345679-7',NULL,
 'Brgy. Wack-Wack, Mandaluyong City','Brgy. Wack-Wack, Mandaluyong City',
 'Present Home Address','(02) 8532-1100','0945-988-0066',
 '(02) 8532-2200',NULL,NULL,
 'bianca.tan@gmail.com',75000.00,18000.00,93000.00),

-- 9. Male, Married, Private, Meralco
('1212-3434-5729','EMPLOYED','EMPLOYED',NULL,'PRIVATE',NULL,
 'Enrique D. Villanueva','Domingo E. Villanueva','Pilar D. Hernandez','Rosa C. Villanueva',
 '1981-08-14','MARRIED','Pasig City','Filipino','MALE','226000600109',
 'Monthly','567-890-234-09','05-2345679-8',40009,
 'No. 22 Meralco Ave., Brgy. Ugong, Pasig City',
 'No. 22 Meralco Ave., Brgy. Ugong, Pasig City',
 'Present Home Address','(02) 8631-3300','0917-099-1177',
 '(02) 8672-1600','(02) 8672-1700','1101',
 'enrique.villanueva@gmail.com',72000.00,8000.00,80000.00),

-- 10. Female, Married, Government, BDO
('1212-3434-5730','EMPLOYED','EMPLOYED',NULL,'GOVERNMENT',NULL,
 'Sheila Marie O. Dela Rosa','Onofre C. Dela Rosa','Teresita O. Gutierrez','Alvin P. Dela Rosa',
 '1985-05-23','MARRIED','Antipolo City','Filipino','FEMALE','226000600110',
 'Monthly','567-890-234-10','05-2345679-9',40010,
 'Brgy. Mayamot, Antipolo City, Rizal','Brgy. Mayamot, Antipolo City, Rizal',
 'Present Home Address','(02) 8697-4455','0923-100-2288',NULL,NULL,NULL,
 'sheila.delarosa@gmail.com',48000.00,6000.00,54000.00);


-- ============================================================
-- currentemprecordtable
-- ============================================================
INSERT INTO currentemprecordtable
  (PagIbig_MID_No, Company_Code, Occupation, Employment_Status, TypeOfWork,
   Country_Of_Assignment, Date_Employed)
VALUES
('1212-3434-5721','GLOBE','Network Operations Engineer','PERMANENT/REGULAR',NULL,'Philippines','2019-08-01'),
('1212-3434-5722','SMPH','Mall Operations Supervisor','CONTRACTUAL',NULL,'Philippines','2023-06-01'),
('1212-3434-5723','DOH','Medical Officer IV','PERMANENT/REGULAR',NULL,'Philippines','2008-03-01'),
('1212-3434-5724','NTT','IT Project Coordinator','CONTRACTUAL','SEA-BASED','International Waters','2022-02-01'),
('1212-3434-5725','IBM','Senior Software Engineer','PERMANENT/REGULAR',NULL,'Philippines','2021-04-01'),
('1212-3434-5726','DOST','Science Research Director','PERMANENT/REGULAR',NULL,'Philippines','2003-07-01'),
('1212-3434-5727','GCH','Junior Product Analyst','CONTRACTUAL',NULL,'Philippines','2025-02-01'),
('1212-3434-5728','SAP','SAP Functional Consultant','CASUAL',NULL,'Philippines','2023-09-01'),
('1212-3434-5729','MNL','Distribution Engineer','PERMANENT/REGULAR',NULL,'Philippines','2015-11-01'),
('1212-3434-5730','BDO','Branch Manager','PERMANENT/REGULAR',NULL,'Philippines','2016-01-01');


-- ============================================================
-- prevemptable
-- ============================================================
INSERT INTO prevemptable
  (PagIbig_MID_No, Company_Code, To_Date, From_Date)
VALUES
('1212-3434-5721','PLDT','2019-07-31','2015-03-01'),
('1212-3434-5722','FOU','2023-05-31','2021-10-01'),
('1212-3434-5723','PUP','2008-02-28','2004-06-01'),
('1212-3434-5724','ACN','2022-01-31','2018-09-01'),
('1212-3434-5725','ORC','2021-03-31','2018-01-01'),
('1212-3434-5726','DICT','2003-06-30','1999-08-01'),
('1212-3434-5728','CIS','2023-08-31','2020-05-01'),
('1212-3434-5729','CVG','2015-10-31','2012-04-01'),
('1212-3434-5730','BPI','2015-12-31','2012-07-01');


-- ============================================================
-- heirstable
-- ============================================================
INSERT INTO heirstable
  (PagIbig_MID_No, Heirs_Name, Heirs_Relationship, Heirs_Birthdate)
VALUES
('1212-3434-5721','Pia B. Reyes','Spouse','1990-12-05'),
('1212-3434-5721','Lucas M. Reyes','Son','2018-06-14'),
('1212-3434-5723','Linda C. Aguilar','Spouse','1978-09-30'),
('1212-3434-5723','Raul Jr. F. Aguilar','Son','2005-04-17'),
('1212-3434-5724','Mark A. Ferrer','Spouse','1982-03-22'),
('1212-3434-5726','Marcelo P. Magsino','Father','1945-11-08'),
('1212-3434-5727','Sophia A. Lim','Spouse','2002-07-19'),
('1212-3434-5729','Rosa C. Villanueva','Spouse','1983-10-01'),
('1212-3434-5729','Elena D. Villanueva','Daughter','2010-02-27'),
('1212-3434-5730','Alvin P. Dela Rosa','Spouse','1983-08-11');


-- ============================================================
-- usercredentials
-- ============================================================
INSERT INTO usercredentials
  (PagIbig_MID_No, Password, Security_Q1, Security_A1, Security_Q2, Security_A2, Security_Q3, Security_A3)
VALUES
('1212-3434-5721','adminuser','','','','','',''),
('1212-3434-5722','adminuser','','','','','',''),
('1212-3434-5723','adminuser','','','','','',''),
('1212-3434-5724','adminuser','','','','','',''),
('1212-3434-5725','adminuser','','','','','',''),
('1212-3434-5726','adminuser','','','','','',''),
('1212-3434-5727','adminuser','','','','','',''),
('1212-3434-5728','adminuser','','','','','',''),
('1212-3434-5729','adminuser','','','','','',''),
('1212-3434-5730','adminuser','','','','','','');