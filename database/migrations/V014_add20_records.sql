-- ============================================================
-- New seed data: 20 members (MID 5701–5720)
-- ============================================================

-- --------------------------------------------------------
-- membertable
-- --------------------------------------------------------
INSERT INTO membertable
  (PagIbig_MID_No, Occupational_Status, Membership_Type, Membership_Type_Others,
   Membership_Category, Membership_Category_Others, Member_Name, Father_Name, Mother_Name,
   Spouse_Name, Birthdate, Marital_Status, Birthplace, Citizenship, Sex, CRN,
   Frequency_Of_Membership_Savings, TIN, SSS, Employee_Number,
   Present_Home_Address, Permanent_Home_Address, Preferred_Mailing_Address,
   Home_TelNum, Cellphone_Num, Bus_DirectLine, Bus_TrunkLine, Local,
   Email_Address, Allow_Basic, Allow_Other_Sources, Total_Mo_Income)
VALUES
-- 1. Male, Married, Government, PH local
('1212-3434-5701','EMPLOYED','EMPLOYED',NULL,'GOVERNMENT',NULL,
 'Carlos M. Dela Cruz','Manuel R. Dela Cruz','Natividad M. Santos','Maria Luisa T. Dela Cruz',
 '1985-03-10','MARRIED','Manila City','Filipino','MALE','226000500101',
 'Monthly','456-789-123-01','04-1234567-0',30001,
 '12 Mabuhay St., Brgy. Commonwealth, Quezon City','12 Mabuhay St., Brgy. Commonwealth, Quezon City',
 'Present Home Address','(02) 8411-2233','0918-111-2233',
 '(02) 8700-1010','(02) 8700-2020','201',
 'carlos.delacruz@gmail.com',55000.00,8000.00,63000.00),

-- 2. Female, Single, Private, PH local
('1212-3434-5702','EMPLOYED','EMPLOYED',NULL,'PRIVATE',NULL,
 'Maria Isabella R. Lim','Richard S. Lim','Caridad R. Lim',NULL,
 '1995-07-22','SINGLE','Cebu City','Filipino','FEMALE','226000500102',
 'Monthly','456-789-123-02','04-1234567-1',30002,
 'Unit 4B Greenfield Tower, Mandaluyong City','Unit 4B Greenfield Tower, Mandaluyong City',
 'Present Home Address',NULL,'0932-222-3344',NULL,NULL,NULL,
 'isabella.lim@gmail.com',38000.00,NULL,38000.00),

-- 3. Male, OFW, Land-based, Saudi Arabia
('1212-3434-5703','EMPLOYED','OVERSEAS FILIPINO WORKER',NULL,'PRIVATE',NULL,
 'Eduardo B. Mendoza','Bernardo L. Mendoza','Josefa B. Reyes',NULL,
 '1980-09-15','SINGLE','Batangas City','Filipino','MALE','226000500103',
 'Monthly','456-789-123-03','04-1234567-2',30003,
 'Lot 7 Blk 3, Brgy. Palahanan, Batangas City','Lot 7 Blk 3, Brgy. Palahanan, Batangas City',
 'Permanent Home Address',NULL,'0920-333-4455',NULL,NULL,NULL,
 'eduardo.mendoza@gmail.com',75000.00,NULL,75000.00),

-- 4. Female, Married, Self-Employed / Professional Business Owner
('1212-3434-5704','EMPLOYED','SELF-EMPLOYED',NULL,'PROFESSIONAL/BUSINESS OWNER',NULL,
 'Angela C. Torres','Francisco C. Castillo','Remedios C. Torres','Miguel A. Torres',
 '1978-12-01','MARRIED','Iloilo City','Filipino','FEMALE','226000500104',
 'Quarterly','456-789-123-04','04-1234567-3',NULL,
 '88 Sunset Ave., Brgy. Malabanias, Angeles City','88 Sunset Ave., Brgy. Malabanias, Angeles City',
 'Present Home Address','(045) 888-1234','0917-444-5566',
 '(045) 888-5678',NULL,NULL,
 'angela.torres@businessmail.com',90000.00,25000.00,115000.00),

-- 5. Male, First Time Jobseeker, Government
('1212-3434-5705','FIRST TIME JOBSEEKERS','EMPLOYED',NULL,'GOVERNMENT',NULL,
 'Juan Paolo S. Bautista','Salvador R. Bautista','Erlinda S. Reyes','Kristine Joy M. Bautista',
 '2000-05-30','MARRIED','Dagupan City','Filipino','MALE','226000500105',
 'Monthly','456-789-123-05','04-1234567-4',30005,
 'Blk 12 Lot 5, Villa Verde Subd., Dagupan City','Blk 12 Lot 5, Villa Verde Subd., Dagupan City',
 'Present Home Address','(075) 522-1122','0956-555-6677',NULL,NULL,NULL,
 'juanpaolo.bautista@gov.ph',28000.00,NULL,28000.00),

-- 6. Female, Unemployed, OFW category, Legally Separated
('1212-3434-5706','UNEMPLOYED','OVERSEAS FILIPINO WORKER',NULL,'OTHER EARNING GROUPS',NULL,
 'Maricel P. Navarro','Pedro V. Navarro','Lorna P. Navarro',NULL,
 '1987-04-18','LEGALLY SEPARATED','Zamboanga City','Filipino','FEMALE','226000500106',
 'Semi-Annual','456-789-123-06','04-1234567-5',NULL,
 'Purok 3, Brgy. Tetuan, Zamboanga City','Purok 3, Brgy. Tetuan, Zamboanga City',
 'Permanent Home Address',NULL,'0999-666-7788',NULL,NULL,NULL,
 'maricel.navarro@yahoo.com',50000.00,10000.00,60000.00),

-- 7. Male, Married, Private Household
('1212-3434-5707','EMPLOYED','EMPLOYED',NULL,'PRIVATE HOUSEHOLD',NULL,
 'Renato O. Garcia','Octavio G. Garcia','Milagros O. Dela Paz','Carina B. Garcia',
 '1972-08-25','MARRIED','Davao City','Filipino','MALE','226000500107',
 'Monthly','456-789-123-07','04-1234567-6',30007,
 '99 Durian St., Brgy. Matina, Davao City','99 Durian St., Brgy. Matina, Davao City',
 'Present Home Address','(082) 233-4455','0910-777-8899',NULL,NULL,NULL,
 'renato.garcia@gmail.com',22000.00,5000.00,27000.00),

-- 8. Female, Single, Private, young professional
('1212-3434-5708','EMPLOYED','EMPLOYED',NULL,'PRIVATE',NULL,
 'Sophia Anne B. Cruz','Benjamin L. Cruz','Teresita B. Mangubat',NULL,
 '2001-11-09','SINGLE','Marikina City','Filipino','FEMALE','226000500108',
 'Monthly','456-789-123-08','04-1234567-7',30008,
 'Apt. 2C, The Residences, Cainta, Rizal','Apt. 2C, The Residences, Cainta, Rizal',
 'Present Home Address',NULL,'0945-888-9900',NULL,NULL,NULL,
 'sophia.cruz@gmail.com',25000.00,NULL,25000.00),

-- 9. Male, Widowed, Self-Employed / Other Earning Groups
('1212-3434-5709','EMPLOYED','SELF-EMPLOYED',NULL,'OTHER EARNING GROUPS',NULL,
 'Ernesto V. Ramos','Vicente A. Ramos','Purificacion V. Santos',NULL,
 '1965-02-14','WIDOWED','Laguna','Filipino','MALE','226000500109',
 'Quarterly','456-789-123-09','04-1234567-8',NULL,
 'Phase 2, Villa Sta. Rosa, Sta. Rosa City, Laguna','Phase 2, Villa Sta. Rosa, Sta. Rosa City, Laguna',
 'Present Home Address','(049) 544-3322','0928-999-0011',NULL,NULL,NULL,
 'ernesto.ramos@outlook.com',35000.00,15000.00,50000.00),

-- 10. Female, Married, OFW Sea-based
('1212-3434-5710','EMPLOYED','OVERSEAS FILIPINO WORKER',NULL,'PRIVATE',NULL,
 'Liezel D. Aquino','Danilo M. Aquino','Gloria D. Perez','Roberto C. Aquino',
 '1990-06-03','MARRIED','Lucena City','Filipino','FEMALE','226000500110',
 'Monthly','456-789-123-10','04-1234567-9',30010,
 'No. 15 Sampaguita St., Brgy. Gulang-Gulang, Lucena City',
 'No. 15 Sampaguita St., Brgy. Gulang-Gulang, Lucena City',
 'Permanent Home Address',NULL,'0917-000-1122',NULL,NULL,NULL,
 'liezel.aquino@gmail.com',80000.00,NULL,80000.00),

-- 11. Male, Married, Government, Annual savings
('1212-3434-5711','EMPLOYED','EMPLOYED',NULL,'GOVERNMENT',NULL,
 'Roland G. Santiago','Gregorio A. Santiago','Felicitas G. Delos Reyes','Gina P. Santiago',
 '1975-10-19','MARRIED','Cabanatuan City','Filipino','MALE','226000500111',
 'Annual','456-789-123-11','04-1234568-0',30011,
 'Block 7 Lot 2, Camella Homes, Cabanatuan City','Block 7 Lot 2, Camella Homes, Cabanatuan City',
 'Employer/Business Address','(044) 600-1122','0923-111-2200',
 '(044) 600-3344',NULL,'301',
 'roland.santiago@gov.ph',65000.00,10000.00,75000.00),

-- 12. Female, Single, First Time Jobseeker, Private
('1212-3434-5712','FIRST TIME JOBSEEKERS','EMPLOYED',NULL,'PRIVATE',NULL,
 'Carmela T. Villanueva','Teodoro R. Villanueva','Corazon T. Mendiola',NULL,
 '2003-01-27','SINGLE','Bataan','Filipino','FEMALE','226000500112',
 'Monthly','456-789-123-12','04-1234568-1',30012,
 'Purok Ilang-Ilang, Brgy. Poblacion, Balanga City',
 'Purok Ilang-Ilang, Brgy. Poblacion, Balanga City',
 'Present Home Address',NULL,'0915-222-3311',NULL,NULL,NULL,
 'carmela.villanueva@gmail.com',18000.00,NULL,18000.00),

-- 13. Male, Married, Private, Annulled → ANNULLED not in existing set; use SEPARATED
('1212-3434-5713','EMPLOYED','EMPLOYED',NULL,'PRIVATE',NULL,
 'Dennis K. Chan','Kwok L. Chan','Rosalinda K. Uy','Patricia A. Chan',
 '1983-07-30','MARRIED','Binondo, Manila','Filipino','MALE','226000500113',
 'Monthly','456-789-123-13','04-1234568-2',30013,
 '5F The Pinnacle Condo, Makati City','5F The Pinnacle Condo, Makati City',
 'Present Home Address','(02) 8888-4455','0917-333-4422',
 '(02) 8999-1100','(02) 8999-2200','401',
 'dennis.chan@gmail.com',120000.00,30000.00,150000.00),

-- 14. Female, Married, OFW, Sea-based nurse
('1212-3434-5714','EMPLOYED','OVERSEAS FILIPINO WORKER',NULL,'PROFESSIONAL/BUSINESS OWNER',NULL,
 'Lourdes R. Evangelista','Ricardo S. Evangelista','Nora R. Santos','Jose Evangelista',
 '1982-04-05','MARRIED','Ilocos Norte','Filipino','FEMALE','226000500114',
 'Monthly','456-789-123-14','04-1234568-3',30014,
 'Brgy. 22, City of Laoag, Ilocos Norte','Brgy. 22, City of Laoag, Ilocos Norte',
 'Permanent Home Address',NULL,'0920-444-5533',NULL,NULL,NULL,
 'lourdes.evangelista@gmail.com',95000.00,5000.00,100000.00),

-- 15. Male, Single, Private, Non-Filipino
('1212-3434-5715','EMPLOYED','EMPLOYED',NULL,'PRIVATE',NULL,
 'Ngo Wei Bin','Ngo Kang Wei','Lin Shu Bin',NULL,
 '1991-09-12','SINGLE','Fujian, China','Other','MALE','226000500115',
 'Monthly','456-789-123-15','04-1234568-4',30015,
 'Unit 12A Pacific Plaza, Bonifacio Global City, Taguig',
 'Unit 12A Pacific Plaza, Bonifacio Global City, Taguig',
 'Present Home Address','(02) 8818-1234','0917-555-6644',
 '(02) 8818-5678',NULL,'501',
 'ngo.weibin@gmail.com',85000.00,15000.00,100000.00),

-- 16. Female, Widowed, Self-Employed, Remote Worker
('1212-3434-5716','EMPLOYED','SELF-EMPLOYED','REMOTE WORKER/FREELANCER','OTHER EARNING GROUPS',NULL,
 'Patricia N. Reyes','Nelson A. Reyes','Corazon N. Ocampo',NULL,
 '1988-03-28','WIDOWED','Cagayan de Oro','Filipino','FEMALE','226000500116',
 'Monthly','456-789-123-16','04-1234568-5',NULL,
 'Block 4 Lot 9, Xavier Estates, Cagayan de Oro','Block 4 Lot 9, Xavier Estates, Cagayan de Oro',
 'Present Home Address',NULL,'0945-666-7755',NULL,NULL,NULL,
 'patricia.reyes@gmail.com',42000.00,8000.00,50000.00),

-- 17. Male, Married, Government, Senior
('1212-3434-5717','EMPLOYED','EMPLOYED',NULL,'GOVERNMENT',NULL,
 'Arsenio D. Castillo','Diego F. Castillo','Adoracion D. Florendo','Nelia C. Castillo',
 '1960-11-03','MARRIED','Pampanga','Filipino','MALE','226000500117',
 'Quarterly','456-789-123-17','04-1234568-6',30017,
 'Villa Lucia Subd., Brgy. Dolores, San Fernando, Pampanga',
 'Villa Lucia Subd., Brgy. Dolores, San Fernando, Pampanga',
 'Employer/Business Address','(045) 455-1010','0923-777-8866',
 '(045) 455-2020',NULL,'601',
 'arsenio.castillo@gov.ph',75000.00,5000.00,80000.00),

-- 18. Female, Single, Private Household, young
('1212-3434-5718','EMPLOYED','EMPLOYED',NULL,'PRIVATE HOUSEHOLD',NULL,
 'Glaiza M. Soriano','Manuel T. Soriano','Marita M. Buenaventura',NULL,
 '2002-08-17','SINGLE','Cavite City','Filipino','FEMALE','226000500118',
 'Monthly','456-789-123-18','04-1234568-7',30018,
 'No. 3 Narra St., Brgy. Zapote, Las Pinas City',
 'No. 3 Narra St., Brgy. Zapote, Las Pinas City',
 'Present Home Address',NULL,'0910-888-9977',NULL,NULL,NULL,
 'glaiza.soriano@gmail.com',16000.00,2000.00,18000.00),

-- 19. Male, Married, OFW, Land-based, Middle East
('1212-3434-5719','EMPLOYED','OVERSEAS FILIPINO WORKER',NULL,'PRIVATE',NULL,
 'Rommel J. Dela Vega','Julian D. Dela Vega','Conception J. Macaraeg','Susan C. Dela Vega',
 '1977-05-20','MARRIED','Pangasinan','Filipino','MALE','226000500119',
 'Monthly','456-789-123-19','04-1234568-8',30019,
 'Poblacion, Urdaneta City, Pangasinan','Poblacion, Urdaneta City, Pangasinan',
 'Permanent Home Address',NULL,'0920-999-0088',NULL,NULL,NULL,
 'rommel.delavega@gmail.com',88000.00,NULL,88000.00),

-- 20. Female, Married, First Time Jobseeker, Government
('1212-3434-5720','FIRST TIME JOBSEEKERS','EMPLOYED',NULL,'GOVERNMENT',NULL,
 'Kristine Mae O. Panganiban','Oscar C. Panganiban','Dolores O. Magsino','Luis R. Panganiban',
 '1999-02-14','MARRIED','Antipolo City','Filipino','FEMALE','226000500120',
 'Monthly','456-789-123-20','04-1234568-9',30020,
 'Brgy. San Isidro, Antipolo City, Rizal','Brgy. San Isidro, Antipolo City, Rizal',
 'Present Home Address','(02) 8697-3344','0956-000-1199',NULL,NULL,NULL,
 'kristine.panganiban@gov.ph',32000.00,NULL,32000.00);


-- --------------------------------------------------------
-- currentemprecordtable
-- --------------------------------------------------------
INSERT INTO currentemprecordtable
  (PagIbig_MID_No, Company_Code, Occupation, Employment_Status, TypeOfWork,
   Country_Of_Assignment, Date_Employed)
VALUES
('1212-3434-5701','CSC','Senior Administrative Officer','PERMANENT/REGULAR',NULL,'Philippines','2018-06-01'),
('1212-3434-5702','ACN','Business Analyst','CONTRACTUAL',NULL,'Philippines','2023-03-15'),
('1212-3434-5703','SKEC','Welder / Pipefitter','CONTRACTUAL','LAND-BASED','Saudi Arabia','2022-09-01'),
('1212-3434-5704','ORC','IT Consultant','CONTRACTUAL',NULL,'Philippines','2021-01-10'),
('1212-3434-5705','DICT','Systems Developer','PERMANENT/REGULAR',NULL,'Philippines','2024-07-01'),
('1212-3434-5706','JPM','Financial Analyst','PROJECT BASED','LAND-BASED','U.S.A','2023-06-01'),
('1212-3434-5707','FOU','Customer Service Representative','CASUAL',NULL,'Philippines','2022-04-01'),
('1212-3434-5708','AWS','Cloud Support Associate','CONTRACTUAL',NULL,'Philippines','2024-01-08'),
('1212-3434-5709','STECH','IT Freelancer','CASUAL',NULL,'Philippines','2020-05-01'),
('1212-3434-5710','SCPH','Sea-based Nurse','CONTRACTUAL','SEA-BASED','International Waters','2021-11-01'),
('1212-3434-5711','CSC','Deputy Director IV','PERMANENT/REGULAR',NULL,'Philippines','2010-03-01'),
('1212-3434-5712','BPI','Bank Teller','CONTRACTUAL',NULL,'Philippines','2025-01-20'),
('1212-3434-5713','ORC1','Senior Sales Engineer','PERMANENT/REGULAR',NULL,'Philippines','2017-08-01'),
('1212-3434-5714','AECO','Project Nurse','CONTRACTUAL','SEA-BASED','International Waters','2020-03-01'),
('1212-3434-5715','ACN','Solution Architect','PERMANENT/REGULAR',NULL,'Philippines','2019-06-01'),
('1212-3434-5716','AWS1','Cloud Freelancer','CASUAL',NULL,'Philippines','2022-07-15'),
('1212-3434-5717','PUP','Professor IV','PERMANENT/REGULAR',NULL,'Philippines','2005-06-01'),
('1212-3434-5718','PLDT','Customer Relations Staff','CASUAL',NULL,'Philippines','2024-09-01'),
('1212-3434-5719','SKEC','Civil Engineer','CONTRACTUAL','LAND-BASED','Saudi Arabia','2023-01-15'),
('1212-3434-5720','DICT','Policy Analyst I','PERMANENT/REGULAR',NULL,'Philippines','2025-03-01');


-- --------------------------------------------------------
-- prevemptable  (AUTO_INCREMENT continues from 22)
-- --------------------------------------------------------
INSERT INTO prevemptable
  (PagIbig_MID_No, Company_Code, To_Date, From_Date)
VALUES
('1212-3434-5701','PUP','2018-05-31','2015-06-01'),
('1212-3434-5702','FOU','2023-03-14','2021-07-01'),
('1212-3434-5703','SCPH','2022-08-31','2019-03-01'),
('1212-3434-5704','CIS','2020-12-31','2018-01-01'),
('1212-3434-5705','ACN','2024-06-30','2022-11-01'),
('1212-3434-5707','PLDT','2022-03-31','2020-08-01'),
('1212-3434-5708','ORC','2023-12-31','2022-05-01'),
('1212-3434-5710','HWI','2021-10-31','2018-06-01'),
('1212-3434-5711','DICT','2010-02-28','2004-01-01'),
('1212-3434-5712','CVG','2024-12-31','2023-09-01'),
('1212-3434-5713','AWS','2017-07-31','2014-02-01'),
('1212-3434-5714','JPM','2020-02-29','2017-05-01'),
('1212-3434-5715','ORC1','2019-05-31','2016-03-01'),
('1212-3434-5717','CSC','2005-05-31','2000-07-01'),
('1212-3434-5719','AECO','2022-12-31','2019-06-01'),
('1212-3434-5720','BPI1','2025-02-28','2023-07-01');


-- --------------------------------------------------------
-- heirstable  (AUTO_INCREMENT continues from 27)
-- --------------------------------------------------------
INSERT INTO heirstable
  (PagIbig_MID_No, Heirs_Name, Heirs_Relationship, Heirs_Birthdate)
VALUES
('1212-3434-5701','Maria Luisa T. Dela Cruz','Spouse','1987-09-14'),
('1212-3434-5701','Marco Luis R. Dela Cruz','Son','2015-02-10'),
('1212-3434-5703','Bernardo L. Mendoza','Father','1955-06-20'),
('1212-3434-5704','Miguel A. Torres','Spouse','1975-11-30'),
('1212-3434-5704','Sophia M. Torres','Daughter','2008-03-22'),
('1212-3434-5705','Kristine Joy M. Bautista','Spouse','2001-04-07'),
('1212-3434-5707','Carina B. Garcia','Spouse','1974-12-01'),
('1212-3434-5707','Andres G. Garcia','Son','2005-08-18'),
('1212-3434-5709','Vicente A. Ramos','Father','1940-05-11'),
('1212-3434-5710','Roberto C. Aquino','Spouse','1988-10-25'),
('1212-3434-5711','Gina P. Santiago','Spouse','1977-03-14'),
('1212-3434-5711','Ralph G. Santiago','Son','2002-07-30'),
('1212-3434-5713','Patricia A. Chan','Spouse','1985-09-09'),
('1212-3434-5714','Jose Evangelista','Spouse','1980-11-22'),
('1212-3434-5716','Corazon N. Ocampo','Mother','1962-02-17'),
('1212-3434-5717','Nelia C. Castillo','Spouse','1963-04-28'),
('1212-3434-5717','Camille D. Castillo','Daughter','1992-01-05'),
('1212-3434-5719','Susan C. Dela Vega','Spouse','1979-07-12'),
('1212-3434-5719','Joshua J. Dela Vega','Son','2007-03-03'),
('1212-3434-5720','Luis R. Panganiban','Spouse','1997-11-19');


-- --------------------------------------------------------
-- usercredentials
-- --------------------------------------------------------
INSERT INTO usercredentials
  (PagIbig_MID_No, Password, Security_Q1, Security_A1, Security_Q2, Security_A2, Security_Q3, Security_A3)
VALUES
('1212-3434-5701','adminuser','','','','','',''),
('1212-3434-5702','adminuser','','','','','',''),
('1212-3434-5703','adminuser','','','','','',''),
('1212-3434-5704','adminuser','','','','','',''),
('1212-3434-5705','adminuser','','','','','',''),
('1212-3434-5706','adminuser','','','','','',''),
('1212-3434-5707','adminuser','','','','','',''),
('1212-3434-5708','adminuser','','','','','',''),
('1212-3434-5709','adminuser','','','','','',''),
('1212-3434-5710','adminuser','','','','','',''),
('1212-3434-5711','adminuser','','','','','',''),
('1212-3434-5712','adminuser','','','','','',''),
('1212-3434-5713','adminuser','','','','','',''),
('1212-3434-5714','adminuser','','','','','',''),
('1212-3434-5715','adminuser','','','','','',''),
('1212-3434-5716','adminuser','','','','','',''),
('1212-3434-5717','adminuser','','','','','',''),
('1212-3434-5718','adminuser','','','','','',''),
('1212-3434-5719','adminuser','','','','','',''),
('1212-3434-5720','adminuser','','','','','','');