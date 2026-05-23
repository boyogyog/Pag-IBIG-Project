#-- SQL RECORD ADDING SYNTAX:

#-- ADDING MEMBER:
INSERT INTO membertable 
  (PagIbig_MID_No, Occupational_Status, Membership_Type, Membership_Category,
   Member_Name, Father_Name, Mother_Name, Spouse_Name, Birthdate, Marital_Status,
   Birthplace, Citizenship, Sex, CRN, Frequency_Of_Membership_Savings, TIN, SSS,
   Employee_Number, Present_Home_Address, Permanent_Home_Address,
   Preferred_Mailing_Address, Home_TelNum, Cellphone_Num, Bus_DirectLine,
   Bus_TrunkLine, Local, Email_Address, Allow_Basic, Allow_Other_Sources, Total_Mo_Income)
VALUES 
  ('1212-3434-5689', 'EMPLOYED', 'EMPLOYED', 'PRIVATE',
   'Rhian O. Reyes', 'Father Reyes', 'Mother Reyes', NULL, '1997-08-05', 'SINGLE',
   'Pasig City', 'Filipino', 'MALE', '226000400127', 'Monthly', '123-456-589-20', '13-2343168-8',
   123428226, 'Pasig City', 'Pasig City',
   'Present Home Address', '(02) 8123-4572', '0917-000-0002', NULL,
   NULL, NULL, 'carloreyes@gmail.com', 45000.00, NULL, 45000.00);
   
#-- ADDING CURRENT EMPLOYMENT:
INSERT INTO currentemprecordtable 
  (PagIbig_MID_No, Company_Code, Occupation, Employment_Status, TypeOfWork,
   Country_Of_Assignment, Date_Employed)
VALUES 
  ('1212-3434-5689', 'HWI' , 'UNEMPLOYED', 'CASUAL', NULL,
   'Philippines', '2025-03-01');

#-- ADDING COMPANY FOR PREVIOUS EMPLOYMENT:
INSERT INTO companydetailstable 
  (Company_Code, Company_Name, Company_Address, Office_Assignment, Branch_Location)
VALUES 
  ('ORACLE', 'Oracle Philippines', 'Taguig City, PH', 'HEAD OFFICE', NULL);

#-- ADDING PREVIOUS EMPLOYMENT
INSERT INTO prevemptable 
  (PagIbig_MID_No, Company_Code, To_Date, From_Date)
VALUES 
  ('1212-3434-5689', 'ORC', '2025-01-31', '2021-05-10');
  
#-- ADDING HEIRS:
INSERT INTO heirstable 
  (PagIbig_MID_No, Heirs_Name, Heirs_Relationship, Heirs_Birthdate)
VALUES 
  ('1212-3434-5689', 'Mother Reyes', 'Mother', '1968-09-14'),
  ('1212-3434-5689', 'Father Reyes', 'Father', '1966-03-22');
