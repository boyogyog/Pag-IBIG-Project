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
  ('1212-3434-5669', 'EMPLOYED', 'EMPLOYED', 'PRIVATE',
   'James Patrick M. Isidro', 'Father Isidro', 'Mother Isidro', NULL, '2005-04-12', 'SINGLE',
   'Pasig City', 'Filipino', 'MALE', '226000000123', 'Monthly', '123-456-789-09', '13-2345168-4',
   123422225, 'Pasig City', 'Pasig City',
   'Present Home Address', '(02) 8123-4571', '0917-000-0001', NULL,
   NULL, NULL, 'patrickjamesisidro@gmail.com', 300220.00, NULL, 3002200.00);
   
#-- ADDING CURRENT EMPLOYMENT:
INSERT INTO currentemprecordtable 
  (PagIbig_MID_No, Company_Code, Occupation, Employment_Status, TypeOfWork,
   Country_Of_Assignment, Date_Employed)
VALUES 
  ('1212-3434-5669', 'CIS', 'Network Engineer', 'PERMANENT/REGULAR', NULL,
   'Philippines', '2025-01-01');
   
#-- ADDING PREVIOUS EMPLOYMENT
INSERT INTO prevemptable 
  (PagIbig_MID_No, Company_Code, To_Date, From_Date)
VALUES 
  ('1212-3434-5669', 'AWS', '2024-12-31', '2022-06-01');
  
#-- ADDING HEIRS:
INSERT INTO heirstable 
  (PagIbig_MID_No, Heirs_Name, Heirs_Relationship, Heirs_Birthdate)
VALUES 
  ('1212-3434-5669', 'Mother Isidro', 'Mother', '1970-03-20');
VALUES
  ('1212-3434-5669', 'Father Isidro', 'Father', '1970-01-01');
   
   