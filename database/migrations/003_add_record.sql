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
  ('1212-3434-5673', 'EMPLOYED', 'EMPLOYED', 'PRIVATE',
   'Aleta Fabregas', 'Father Fabregas', 'Mother Fabregas', NULL, '1960-05-14', 'MARRIED',
   'Manila City', 'Filipino', 'FEMALE', '226000400129', 'Monthly', '123-456-599-19', '13-2343168-7',
   123428225, 'Mandaluyong City', 'Mandaluyong City',
   'Present Home Address', '(02) 8123-4571', '0917-000-0001', NULL,
   NULL, NULL, 'fabregas@gmail.com', 3003201.00, NULL, 3003200.00);
   
#-- ADDING CURRENT EMPLOYMENT:
INSERT INTO currentemprecordtable 
  (PagIbig_MID_No, Company_Code, Occupation, Employment_Status, TypeOfWork,
   Country_Of_Assignment, Date_Employed)
VALUES 
  ('1212-3434-5673', 'PUP', 'Professor III', 'PERMANENT/REGULAR', NULL,
   'Philippines', '2025-02-01');
   
#-- ADDING PREVIOUS EMPLOYMENT
INSERT INTO prevemptable 
  (PagIbig_MID_No, Company_Code, To_Date, From_Date)
VALUES 
  ('1212-3434-5673', 'ACN', '2024-10-27', '2022-08-03');
  
#-- ADDING HEIRS:
INSERT INTO heirstable 
  (PagIbig_MID_No, Heirs_Name, Heirs_Relationship, Heirs_Birthdate)
VALUES 
  ('1212-3434-5673', 'Mother Fabregas', 'Mother', '1940-06-20'),
  ('1212-3434-5673', 'Father Fabregas', 'Father', '1942-02-01');