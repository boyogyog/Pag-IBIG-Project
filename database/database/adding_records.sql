#-- SQL RECORD ADDING SYNTAX:
#-- ADDING COMPANY:

INSERT INTO companydetailstable 
  (Company_Code, Company_Name, Company_Address, Office_Assignment, Branch_Location)
VALUES 
  ('CIS', 'Cisco Philippines', 'Taguig City, PH', 'HEAD OFFICE', NULL);
  
#-- ADDING MEMBER:
INSERT INTO membertable 
  (PagIbig_MID_No, Occupational_Status, Membership_Type, Membership_Category,
   Member_Name, Father_Name, Mother_Name, Spouse_Name, Birthdate, Marital_Status,
   Birthplace, Citizenship, Sex, CRN, Frequency_Of_Membership_Savings, TIN, SSS,
   Employee_Number, Present_Home_Address, Permanent_Home_Address,
   Preferred_Mailing_Address, Home_TelNum, Cellphone_Num, Bus_DirectLine,
   Bus_TrunkLine, Local, Email_Address, Allow_Basic, Allow_Other_Sources, Total_Mo_Income)
VALUES 
  ('1212-3434-5660', 'EMPLOYED', 'EMPLOYED', 'PRIVATE',
   'Yeshua Adrielle S. Arjona', 'Father Arjona', 'Mother Arjona', NULL, '2005-04-15', 'SINGLE',
   'Manila City', 'Filipino', 'MALE', '226000000002', 'Monthly', '123-456-789-05', '13-2345678-4',
   12345, 'SJDM Bulacan', 'SJDM Bulacan',
   'Present Home Address', '(02) 8123-4571', '0917-000-0001', NULL,
   NULL, NULL, 'yeshua@gmail.com', 30000.00, NULL, 30000.00);
   
#-- ADDING CURRENT EMPLOYMENT:
INSERT INTO currentemprecordtable 
  (PagIbig_MID_No, Company_Code, Occupation, Employment_Status, TypeOfWork,
   Country_Of_Assignment, Date_Employed)
VALUES 
  ('1212-3434-5660', 'CIS', 'Software Developer', 'PERMANENT/REGULAR', NULL,
   'Philippines', '2025-01-01');
   
#-- ADDING PREVIOUS EMPLOYMENT
INSERT INTO prevemptable 
  (PagIbig_MID_No, Company_Code, To_Date, From_Date)
VALUES 
  ('1212-3434-5660', 'AWS', '2024-12-31', '2022-06-01');
  
#-- ADDING HEIRS:
INSERT INTO heirstable 
  (PagIbig_MID_No, Heirs_Name, Heirs_Relationship, Heirs_Birthdate)
VALUES 
  ('1212-3434-5660', 'Maria D. Cruz', 'Mother', '1970-03-20');
   
   