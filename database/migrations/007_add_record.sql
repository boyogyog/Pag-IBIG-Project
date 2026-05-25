-- ADDING MEMBER:
INSERT INTO membertable 
  (PagIbig_MID_No, Occupational_Status, Membership_Type, Membership_Category,
   Member_Name, Father_Name, Mother_Name, Spouse_Name, Birthdate, Marital_Status,
   Birthplace, Citizenship, Sex, CRN, Frequency_Of_Membership_Savings, TIN, SSS,
   Employee_Number, Present_Home_Address, Permanent_Home_Address,
   Preferred_Mailing_Address, Home_TelNum, Cellphone_Num, Bus_DirectLine,
   Bus_TrunkLine, Local, Email_Address, Allow_Basic, Allow_Other_Sources, Total_Mo_Income)
VALUES 
  ('1212-3434-5677', 'FIRST TIME JOBSEEKERS', 'EMPLOYED', 'GOVERNMENT',
   'Felix F. Francisco', 'Father Francisco', 'Mother Francisco', 'Friday Francisco', '1991-06-07', 'MARRIED',
   'Quezon City', 'Filipino', 'MALE', '226000400067', 'Monthly', '123-456-589-06', '13-2343168-5',
   12293678, 'Quezon City', 'Quezon City',
   'Present Home Address', '(02) 8223-3248', '0923-555-2988', NULL,
   NULL, NULL, 'felixfran23@gmail.com', 40000.00, NULL, 40000.00);

   
-- ADDING CURRENT EMPLOYMENT:
INSERT INTO currentemprecordtable 
  (PagIbig_MID_No, Company_Code, Occupation, Employment_Status, TypeOfWork,
   Country_Of_Assignment, Date_Employed)
VALUES 
  ('1212-3434-5677', 'CSC', 'Assistant Commissioner', 'PERMANENT/REGULAR', NULL,
   'Philippines', '2025-02-03');

   
-- ADDING PREVIOUS EMPLOYMENT:
INSERT INTO prevemptable 
  (PagIbig_MID_No, Company_Code, To_Date, From_Date)
VALUES 
  ('1212-3434-5677', 'CSC', '2025-02-03', '2025-02-03');

  
-- ADDING HEIRS:
INSERT INTO heirstable 
  (PagIbig_MID_No, Heirs_Name, Heirs_Relationship, Heirs_Birthdate)
VALUES 
  ('1212-3434-5677', 'Friday Francisco', 'Spouse', '1992-07-23'),
  ('1212-3434-5677', 'Mother Francisco', 'Mother', '1940-12-01');

  
-- ADDING COMPANY DETAILS:
INSERT INTO companydetailstable 
  (Company_Code, Company_Name, Company_Address, Office_Assignment, Branch_Location)
VALUES 
  ('CSC', 'Civil Service Commission', 'Quezon City, PH', 'HEAD OFFICE', NULL);