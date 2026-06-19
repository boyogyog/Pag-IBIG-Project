ALTER TABLE membertable
  MODIFY COLUMN Citizenship VARCHAR(50),
  MODIFY COLUMN Membership_Type_Others VARCHAR(50),
  MODIFY COLUMN Membership_Category_Others VARCHAR(50);

ALTER TABLE heirstable
MODIFY COLUMN Heirs_Relationship VARCHAR(50);

ALTER TABLE currentemprecordtable
  MODIFY COLUMN Country_Of_Assignment VARCHAR(50);
  
-- Tanggal doble ng aws
update companydetailstable
set branch_location = "Ortigas, Pasay City",
company_address = "Quezon City, PH"
where company_code = "aws1";

-- Tanggal yung ORACLE 
DELETE FROM `pagibig`.`companydetailstable` WHERE (`Company_Code` = 'ORACLE');

-- lagyan ng pass lahat ng accs na hindi gawa from gui:
INSERT INTO usercredentials (pagibig_mid_no, password, Security_Q1, Security_A1, Security_Q2, Security_A2, Security_Q3, Security_A3)
VALUES
  ('1212-3434-5660', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5669', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5696', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5697', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5698', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5721', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5722', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5723', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5724', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5725', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5726', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5727', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5728', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5729', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5730', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5731', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5732', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5733', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5734', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5735', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5736', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5737', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5738', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5739', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5740', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5741', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5742', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5743', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5744', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5745', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5746', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5747', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5748', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5749', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5750', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5751', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5752', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5753', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5754', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5755', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5756', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5757', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5758', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5759', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5760', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5761', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5762', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5763', 'adminuser', '', '', '', '', '', ''),
  ('1212-3434-5764', 'adminuser', '', '', '', '', '', '');