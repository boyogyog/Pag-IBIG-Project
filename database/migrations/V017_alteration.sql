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

