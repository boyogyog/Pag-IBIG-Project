-- seed.sql
-- Run this AFTER the schema is created to populate initial data.

-- ============================================================
-- 1. COMPANIES
-- ============================================================
INSERT INTO companydetailstable VALUES 
('ACN','Accenture','Taguig City, PH','HEAD OFFICE',NULL),
('AWS','Amazon Web Services','Quezon City, PH','HEAD OFFICE',NULL),
('AWS1','Amazon Web Services','Pasay City, PH','HEAD OFFICE',NULL),
('CVG','Converge','Pasig City, PH','HEAD OFFICE',NULL),
('DICT','Department of Information and Communications Technology (Republic of Philippines)','Quezon City, PH','HEAD OFFICE',NULL),
('JPM','JP Morgan Chase Company','New York City, USA','HEAD OFFICE',NULL),
('ORC','Oracle','Taguig City, PH','BRANCH','Makati City'),
('PLDT','Philippine Long Distance Telephone Company','Quezon City, PH','BRANCH','Caloocan City'),
('PUP','Polytechnic University of the Philippines - Sta. Mesa','Manila City, PH','HEAD OFFICE',NULL),
('STECH','Seanna Tech','Quezon City, PH','HEAD OFFICE',NULL);

-- ============================================================
-- 2. MEMBERS
-- ============================================================
INSERT INTO membertable VALUES 
('1212-3434-5656','EMPLOYED','EMPLOYED',NULL,'PRIVATE',NULL,'Robert D. Sarmiento','Daniel M. Sarmiento','Georgia L. Diana','Cynthia W. Sarmiento','1990-11-21','MARRIED','Quezon City','Filipino','MALE','226000000000','Monthly','123-456-789-01','13-2345678-0',2147483647,'810 Southroad St., Jude Luxury Homes, Tandang Sora, Quezon City, Metro Manila','810 Southroad St., Jude Luxury Homes, Tandang Sora, Quezon City, Metro Manila','Present Home Address','(02) 8123-4567','0917-123-4567','(02) 8888-9999','(02) 8555-0000','101','robertoboypaos@gmail.com',120000.00,40000.00,160000.00),
('1212-3434-5657','EMPLOYED','EMPLOYED',NULL,'GOVERNMENT',NULL,'Migel H. Tan','Michael T. Tan','Angelica H. Tan','Spouse Tan','2006-03-12','MARRIED','Valenzuela City','Filipino','MALE','225501000000','Quarterly','123-456-789-02','13-2345678-1',2147483647,'#23 La-Huerta Subdivision, Marulas, Valenzuela City','#23 La-Huerta Subdivision, Marulas, Valenzuela City','Present Home Address','(02) 8123-4568','0956-740-1232','(02) 8888-10000','(02) 8555-0001','102','migellltan@gmail.com',250000.00,90000.00,340000.00),
('1212-3434-5658','EMPLOYED','OVERSEAS FILIPINO WORKER',NULL,'OVERSEAS FILIPINO WORKER',NULL,'James Escanillas','Father Escanillas','Mother Escanillas','Spouse Escanillas','2006-12-03','MARRIED','Quezon City','Filipino','MALE','226000000001','Monthly','123-456-789-03','13-2345678-2',2147483647,'154 Olibas St. Nawasa Side Brgy Pasong Tamo QC','154 Olibas St. Nawasa Side Brgy Pasong Tamo QC','Permanent Home Address','(02) 8123-4569','0992-843-9679','(02) 8888-10001','(02) 8555-0002','103','jamesescanillas3@gmail.com',250000.00,90000.00,340000.00),
('1212-3434-5659','EMPLOYED','EMPLOYED',NULL,'PRIVATE HOUSEHOLD',NULL,'Raven Rayo','Father Rayo','Mother Rayo','Spouse Surname','2005-09-07','MARRIED','Bulacan','Filipino','FEMALE','225501000001','Monthly','123-456-789-04','13-2345678-3',2147483647,'Bliss, Poblacion, Norzagaray Bulacan','Bliss, Poblacion, Norzagaray Bulacan','Employer/Business Address','(02) 8123-4570','0967-738-5677','(02) 8888-10002','(02) 8555-0003','104','ravenjoyce07@gmail.com',250000.00,90000.00,340000.00);

-- ============================================================
-- 3. CURRENT EMPLOYMENT
-- ============================================================
INSERT INTO currentemprecordtable VALUES 
('1212-3434-5656','STECH','Network Engineer','CONTRACTUAL',NULL,'Philippines','2028-11-11'),
('1212-3434-5657','PUP','Data Analyst','PERMANENT/REGULAR',NULL,'Philippines','2028-11-12'),
('1212-3434-5658','CVG','Network Engineer','CASUAL',NULL,'Philippines','2028-11-13'),
('1212-3434-5659','JPM','Data Analyst','PROJECT BASED','LAND-BASED','U.S.A','2028-11-14');

-- ============================================================
-- 4. PREVIOUS EMPLOYMENT
-- ============================================================
INSERT INTO prevemptable (PagIbig_MID_No, Company_Code, To_Date, From_Date) VALUES 
('1212-3434-5656','AWS','2029-03-01','2028-07-01'),
('1212-3434-5656','ORC','2033-09-01','2031-09-01'),
('1212-3434-5656','CVG','2031-10-01','2031-01-01'),
('1212-3434-5657','ACN','2030-02-01','2028-02-01'),
('1212-3434-5657','PUP','2040-02-01','2030-02-01'),
('1212-3434-5658','PLDT','2029-09-01','2028-03-01'),
('1212-3434-5659','AWS1','2030-01-01','2028-01-01');

-- heirstable has no initial data, skip for now.