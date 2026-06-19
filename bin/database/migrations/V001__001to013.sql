-- =============================================================
--  PAGIBIG SEED FILE
--  Compiled from V001–V013
-- =============================================================

-- -------------------------------------------------------------
--  COMPANY DETAILS
-- -------------------------------------------------------------

INSERT IGNORE INTO companydetailstable
  (Company_Code, Company_Name, Company_Address, Office_Assignment, Branch_Location)
VALUES
  ('HWI',    'Huawei Philippines',               'Taguig City, PH',        'BRANCH',      'Quezon City'),
  ('CSC',    'Civil Service Commission',         'Quezon City, PH',        'HEAD OFFICE',  NULL),
  ('SKEC',   'Samsung C&T Engineering',          'Riyadh, Saudi Arabia',   'BRANCH',      'Al Olaya District, Riyadh'),
  ('SCPH',   'Sta. Clara International Corporation', 'Muntinlupa City, PH','HEAD OFFICE',  NULL),
  ('AECO',   'AECOM Philippines',                'Pasig City, PH',         'BRANCH',      'Ortigas Center, Pasig City'),
  ('ORC', 'Oracle Philippines',               'Taguig City, PH',        'HEAD OFFICE',  NULL);


-- -------------------------------------------------------------
--  SCHEMA FIXES  (run before inserts so column types are ready)
-- -------------------------------------------------------------

ALTER TABLE `pagibig`.`companydetailstable`
  CHANGE COLUMN `Office_Assignment` `Office_Assignment` VARCHAR(11) NOT NULL;

ALTER TABLE `pagibig`.`currentemprecordtable`
  CHANGE COLUMN `Employment_Status`     `Employment_Status`     VARCHAR(17) NULL DEFAULT NULL,
  CHANGE COLUMN `TypeOfWork`            `TypeOfWork`            VARCHAR(10) NULL DEFAULT NULL,
  CHANGE COLUMN `Country_Of_Assignment` `Country_Of_Assignment` VARCHAR(25) NULL;

ALTER TABLE `pagibig`.`membertable`
  CHANGE COLUMN `Occupational_Status`  `Occupational_Status`  VARCHAR(21) NOT NULL,
  CHANGE COLUMN `Membership_Type`      `Membership_Type`      VARCHAR(24) NOT NULL,
  CHANGE COLUMN `Membership_Category`  `Membership_Category`  VARCHAR(30) NOT NULL,
  CHANGE COLUMN `Marital_Status`       `Marital_Status`       VARCHAR(17) NOT NULL,
  CHANGE COLUMN `Sex`                  `Sex`                  VARCHAR(6)  NOT NULL;


-- -------------------------------------------------------------
--  USER CREDENTIALS TABLE
-- -------------------------------------------------------------

CREATE TABLE IF NOT EXISTS usercredentials (
  PagIbig_MID_No  CHAR(14)      NOT NULL,
  Password        VARCHAR(255)  NOT NULL,
  Security_Q1     VARCHAR(100)  NOT NULL,
  Security_A1     VARCHAR(100)  NOT NULL,
  Security_Q2     VARCHAR(100)  NOT NULL,
  Security_A2     VARCHAR(100)  NOT NULL,
  Security_Q3     VARCHAR(100)  NOT NULL,
  Security_A3     VARCHAR(100)  NOT NULL,
  PRIMARY KEY (PagIbig_MID_No),
  FOREIGN KEY (PagIbig_MID_No) REFERENCES membertable(PagIbig_MID_No)
    ON DELETE CASCADE ON UPDATE CASCADE
);


-- -------------------------------------------------------------
--  MEMBERS
-- -------------------------------------------------------------

INSERT IGNORE INTO membertable
  (PagIbig_MID_No, Occupational_Status, Membership_Type, Membership_Category,
   Member_Name, Father_Name, Mother_Name, Spouse_Name, Birthdate, Marital_Status,
   Birthplace, Citizenship, Sex, CRN, Frequency_Of_Membership_Savings, TIN, SSS,
   Employee_Number, Present_Home_Address, Permanent_Home_Address,
   Preferred_Mailing_Address, Home_TelNum, Cellphone_Num, Bus_DirectLine,
   Bus_TrunkLine, Local, Email_Address, Allow_Basic, Allow_Other_Sources, Total_Mo_Income)
VALUES
  -- V001: James Patrick M. Isidro
  ('1212-3434-5669', 'EMPLOYED', 'EMPLOYED', 'PRIVATE',
   'James Patrick M. Isidro', 'Father Isidro', 'Mother Isidro', NULL, '2005-04-12', 'SINGLE',
   'Pasig City', 'Filipino', 'MALE', '226000000123', 'Monthly', '123-456-789-09', '13-2345168-4',
   123422225, 'Pasig City', 'Pasig City',
   'Present Home Address', '(02) 8123-4571', '0917-000-0001', NULL,
   NULL, NULL, 'patrickjamesisidro@gmail.com', 300220.00, NULL, 3002200.00),

  -- V002: Fern G. Flores
  ('1212-3434-5672', 'EMPLOYED', 'EMPLOYED', 'PRIVATE',
   'Fern G. Flores', 'Father Flores', 'Mother Flores', NULL, '2005-03-12', 'SINGLE',
   'Mandaluyong City', 'Filipino', 'FEMALE', '226000400126', 'Monthly', '123-456-589-19', '13-2343168-7',
   123428225, 'Mandaluyong City', 'Mandaluyong City',
   'Present Home Address', '(02) 8123-4571', '0917-000-0001', NULL,
   NULL, NULL, 'fernflores@gmail.com', 3003201.00, NULL, 3003200.00),

  -- V003: Aleta Fabregas
  ('1212-3434-5673', 'EMPLOYED', 'EMPLOYED', 'PRIVATE',
   'Aleta Fabregas', 'Father Fabregas', 'Mother Fabregas', NULL, '1960-05-14', 'MARRIED',
   'Manila City', 'Filipino', 'FEMALE', '226000400129', 'Monthly', '123-456-599-19', '13-2343168-7',
   123428225, 'Mandaluyong City', 'Mandaluyong City',
   'Present Home Address', '(02) 8123-4571', '0917-000-0001', NULL,
   NULL, NULL, 'fabregas@gmail.com', 3003201.00, NULL, 3003200.00),

  -- V005: Rhian O. Reyes
  ('1212-3434-5689', 'EMPLOYED', 'EMPLOYED', 'PRIVATE',
   'Rhian O. Reyes', 'Father Reyes', 'Mother Reyes', NULL, '1997-08-05', 'SINGLE',
   'Pasig City', 'Filipino', 'MALE', '226000400127', 'Monthly', '123-456-589-20', '13-2343168-8',
   123428226, 'Pasig City', 'Pasig City',
   'Present Home Address', '(02) 8123-4572', '0917-000-0002', NULL,
   NULL, NULL, 'carloreyes@gmail.com', 45000.00, NULL, 45000.00),

  -- V006/V010: Ramon Cruz D. Santos (deduplicated — kept once)
  ('1212-3434-5676', 'EMPLOYED', 'OVERSEAS FILIPINO WORKER', 'PRIVATE',
   'Ramon Cruz D. Santos', 'Eduardo D. Santos', 'Lourdes C. Santos', NULL, '1968-06-14', 'SINGLE',
   'Caloocan City', 'Filipino', 'MALE', '226000000004', 'Monthly', '321-654-987-01', '04-5678901-2',
   99001, 'Block 5 Lot 3 Brgy. Camarin, Caloocan City', 'Block 5 Lot 3 Brgy. Camarin, Caloocan City',
   'Permanent Home Address', NULL, '0920-000-0003', NULL,
   NULL, NULL, 'ramon.santos@gmail.com', 60000.00, NULL, 60000.00),

  -- V007: Felix F. Francisco
  ('1212-3434-5677', 'FIRST TIME JOBSEEKERS', 'EMPLOYED', 'GOVERNMENT',
   'Felix F. Francisco', 'Father Francisco', 'Mother Francisco', 'Friday Francisco', '1991-06-07', 'MARRIED',
   'Quezon City', 'Filipino', 'MALE', '226000400067', 'Monthly', '123-456-589-06', '13-2343168-5',
   12293678, 'Quezon City', 'Quezon City',
   'Present Home Address', '(02) 8223-3248', '0923-555-2988', NULL,
   NULL, NULL, 'felixfran23@gmail.com', 40000.00, NULL, 40000.00),

  -- V008: Peter Partel
  ('1212-3434-5678', 'EMPLOYED', 'EMPLOYED', 'PRIVATE',
   'Peter Partel', 'Ben Partel', 'May Partel', NULL, '1997-04-22', 'SINGLE',
   'Cebu City', 'Filipino', 'MALE', '222492923485', 'Monthly', '123-456-222-02', '13-2342289-8',
   123428226, '6 Gateway St., Barangay 167, Caloocan', '6 Gateway St., Barangay 167, Caloocan',
   'Present Home Address', '(02) 8324-5656', '0917-591-6348', NULL,
   NULL, NULL, 'petpat88@gmail.com', 43200.00, NULL, 43200.00),

  -- V009: Jasmine Shaine B. Arjona
  ('1212-3434-5675', 'EMPLOYED', 'SELF-EMPLOYED', 'PROFESSIONAL/BUSINESS OWNER',
   'Jasmine Shaine B. Arjona', 'Benjamin R. Arjona', 'Rosario B. Arjona',
   'Yeshua Adrielle S. Arjona', '2004-08-22', 'MARRIED',
   'Quezon City', 'Filipino', 'FEMALE', '226000000003', 'Monthly', '987-654-321-02', '33-4567890-1',
   111223344,
   'Block 1 Lot 1 Phase B, Brgy. Mulawin Francisco Homes 1 City of San Jose del Monte, Bulacan',
   'Taguig City, PH',
   'Present Home Address', '(02) 8765-4321', '0918-000-0002', '(02) 8800-1234',
   NULL, '102', 'jasmine.arjona@gmail.com', 45000.00, 5000.00, 50000.00);

-- V004: Catty Hemady (has Membership_Type_Others column)
INSERT IGNORE INTO membertable
  (PagIbig_MID_No, Occupational_Status, Membership_Type, Membership_Type_Others, Membership_Category,
   Member_Name, Father_Name, Mother_Name, Spouse_Name, Birthdate, Marital_Status,
   Birthplace, Citizenship, Sex, CRN, Frequency_Of_Membership_Savings, TIN, SSS,
   Employee_Number, Present_Home_Address, Permanent_Home_Address,
   Preferred_Mailing_Address, Home_TelNum, Cellphone_Num, Bus_DirectLine,
   Bus_TrunkLine, Local, Email_Address, Allow_Basic, Allow_Other_Sources, Total_Mo_Income)
VALUES
  ('1212-3434-5674', 'FIRST TIME JOBSEEKERS', 'SELF-EMPLOYED', 'REMOTE WORKER/FREELANCER', 'OTHER EARNING GROUPS',
   'Catty Hemady', 'Roberto Reyes', 'Catriona Reyes', 'Leon Hemady', '1998-10-12', 'MARRIED',
   'Lubao, Pampanga', 'Filipino', 'FEMALE', '222492939945', 'Monthly', '123-456-239-22', '13-2341118-7',
   52421, '12 B Liwayway St., Marulas, Valenzuela City', '12 B Liwayway St., Marulas, Valenzuela City',
   'Present Home Address', '(02) 8123-4571', '0917-033-0351', NULL,
   NULL, NULL, 'cattyhem32@hotmail.com', 56000.11, 636687, 1020426.00);

-- V008: Peter Partel (also has Membership_Type_Others — re-insert with that column if needed)
-- Already inserted above without Membership_Type_Others (NULL). If your schema requires it, adjust above.


-- -------------------------------------------------------------
--  CURRENT EMPLOYMENT RECORDS
-- -------------------------------------------------------------

INSERT IGNORE INTO currentemprecordtable
  (PagIbig_MID_No, Company_Code, Occupation, Employment_Status, TypeOfWork,
   Country_Of_Assignment, Date_Employed)
VALUES
  ('1212-3434-5669', 'CIS',  'Network Engineer',          'PERMANENT/REGULAR', NULL,         'Philippines',    '2025-01-01'),
  ('1212-3434-5672', 'ORC',  'Systems Analyst',           'PERMANENT/REGULAR', NULL,         'Philippines',    '2025-02-01'),
  ('1212-3434-5673', 'PUP',  'Professor III',             'PERMANENT/REGULAR', NULL,         'Philippines',    '2025-02-01'),
  ('1212-3434-5674', 'HWI',  'UNEMPLOYED',                'CASUAL',            NULL,         '',               '1999-01-01'),
  ('1212-3434-5689', 'HWI',  'UNEMPLOYED',                'CASUAL',            NULL,         'Philippines',    '2025-03-01'),
  ('1212-3434-5676', 'SKEC', 'Mechanical Engineer',       'CONTRACTUAL',       'LAND-BASED', 'Saudi Arabia',   '2023-05-01'),
  ('1212-3434-5677', 'CSC',  'Assistant Commissioner',    'PERMANENT/REGULAR', NULL,         'Philippines',    '2025-02-03'),
  ('1212-3434-5678', 'HWI',  'EMPLOYED',                  'PERMANENT/REGULAR', NULL,         'Philippines',    '2020-02-15'),
  ('1212-3434-5675', 'SCPH', 'Civil Engineer',            'CONTRACTUAL',       NULL,         'Philippines',    '2024-03-15');


-- -------------------------------------------------------------
--  PREVIOUS EMPLOYMENT RECORDS
-- -------------------------------------------------------------

INSERT IGNORE INTO prevemptable
  (PagIbig_MID_No, Company_Code, To_Date, From_Date)
VALUES
  ('1212-3434-5669', 'AWS',  '2024-12-31', '2022-06-01'),
  ('1212-3434-5672', 'ORC',  '2024-10-27', '2022-08-03'),
  ('1212-3434-5673', 'ACN',  '2024-10-27', '2022-08-03'),
  ('1212-3434-5674', 'HWI',  '1999-10-12', '2007-10-12'),
  ('1212-3434-5689', 'ORC',  '2025-01-31', '2021-05-10'),
  ('1212-3434-5676', 'SCPH', '2023-04-30', '2019-08-01'),
  ('1212-3434-5677', 'CSC',  '2025-02-03', '2025-02-03'),
  ('1212-3434-5678', 'ACN',  '2020-01-15', '2018-10-22'),
  ('1212-3434-5675', 'AECO', '2024-02-28', '2021-07-01');


-- -------------------------------------------------------------
--  HEIRS
-- -------------------------------------------------------------

INSERT IGNORE INTO heirstable
  (PagIbig_MID_No, Heirs_Name, Heirs_Relationship, Heirs_Birthdate)
VALUES
  -- V001: James Patrick M. Isidro (fixed broken double-VALUES syntax)
  ('1212-3434-5669', 'Mother Isidro',          'Mother',    '1970-03-20'),
  ('1212-3434-5669', 'Father Isidro',          'Father',    '1970-01-01'),
  -- V002: Fern G. Flores
  ('1212-3434-5672', 'Mother Flores',          'Mother',    '1975-06-20'),
  ('1212-3434-5672', 'Father Flores',          'Father',    '1974-02-01'),
  -- V003: Aleta Fabregas
  ('1212-3434-5673', 'Mother Fabregas',        'Mother',    '1940-06-20'),
  ('1212-3434-5673', 'Father Fabregas',        'Father',    '1942-02-01'),
  -- V004: Catty Hemady
  ('1212-3434-5674', 'Roberto Reyes',          'Father',    '1976-01-09'),
  ('1212-3434-5674', 'Catriona Reyes',         'Mother',    '1980-02-01'),
  ('1212-3434-5674', 'Sevvy Hemady',           'Son',       '2010-11-28'),
  ('1212-3434-5674', 'Maria Hemady',           'Daughter',  '2012-05-13'),
  -- V005: Rhian O. Reyes
  ('1212-3434-5689', 'Mother Reyes',           'Mother',    '1968-09-14'),
  ('1212-3434-5689', 'Father Reyes',           'Father',    '1966-03-22'),
  -- V006/V010: Ramon Cruz D. Santos
  ('1212-3434-5676', 'Miguel Jose D. Santos',  'Grandson',  '2018-04-12'),
  -- V007: Felix F. Francisco
  ('1212-3434-5677', 'Friday Francisco',       'Spouse',    '1992-07-23'),
  ('1212-3434-5677', 'Mother Francisco',       'Mother',    '1940-12-01'),
  -- V008: Peter Partel
  ('1212-3434-5678', 'Ben Partel',             'Father',    '1967-12-09'),
  ('1212-3434-5678', 'May Partel',             'Mother',    '1968-04-07'),
  -- V009: Jasmine Shaine B. Arjona
  ('1212-3434-5675', 'Yeshua Adrielle S. Arjona', 'Husband','2005-04-15'),
  ('1212-3434-5675', 'Rosario B. Arjona',      'Mother',    '1968-11-05'),
  ('1212-3434-5675', 'Benjamin R. Arjona',     'Father',    '1965-03-17');


-- -------------------------------------------------------------
--  USER CREDENTIALS
-- -------------------------------------------------------------

INSERT INTO usercredentials
  (PagIbig_MID_No, Password, Security_Q1, Security_A1, Security_Q2, Security_A2, Security_Q3, Security_A3)
VALUES
  ('1212-3434-5656', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5657', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5658', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5659', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5672', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5673', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5674', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5675', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5676', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5677', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5678', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5689', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5694', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5695', 'adminuser', '', '', '', '', '', '')
ON DUPLICATE KEY UPDATE
  Password    = VALUES(Password),
  Security_Q1 = VALUES(Security_Q1),
  Security_A1 = VALUES(Security_A1),
  Security_Q2 = VALUES(Security_Q2),
  Security_A2 = VALUES(Security_A2),
  Security_Q3 = VALUES(Security_Q3),
  Security_A3 = VALUES(Security_A3);