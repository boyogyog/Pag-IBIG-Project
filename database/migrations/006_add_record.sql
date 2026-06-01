#-- ADDING NEW COMPANY (OFW Employer):
INSERT INTO companydetailstable 
  (Company_Code, Company_Name, Company_Address, Office_Assignment, Branch_Location)
VALUES 
  ('SKEC', 'Samsung C&T Engineering', 'Riyadh, Saudi Arabia', 'BRANCH', 'Al Olaya District, Riyadh');

#-- ADDING MEMBER:
INSERT INTO membertable 
  (PagIbig_MID_No, Occupational_Status, Membership_Type, Membership_Category,
   Member_Name, Father_Name, Mother_Name, Spouse_Name, Birthdate, Marital_Status,
   Birthplace, Citizenship, Sex, CRN, Frequency_Of_Membership_Savings, TIN, SSS,
   Employee_Number, Present_Home_Address, Permanent_Home_Address,
   Preferred_Mailing_Address, Home_TelNum, Cellphone_Num, Bus_DirectLine,
   Bus_TrunkLine, Local, Email_Address, Allow_Basic, Allow_Other_Sources, Total_Mo_Income)
VALUES 
  ('1212-3434-5676', 'EMPLOYED', 'OVERSEAS FILIPINO WORKER', 'PRIVATE',
   'Ramon Cruz D. Santos', 'Eduardo D. Santos', 'Lourdes C. Santos', NULL, '1968-06-14', 'SINGLE',
   'Caloocan City', 'Filipino', 'MALE', '226000000004', 'Monthly',
   '321-654-987-01', '04-5678901-2',
   99001, 'Block 5 Lot 3 Brgy. Camarin, Caloocan City', 'Block 5 Lot 3 Brgy. Camarin, Caloocan City',
   'Permanent Home Address', NULL, '0920-000-0003', NULL,
   NULL, NULL, 'ramon.santos@gmail.com', 60000.00, NULL, 60000.00);

#-- ADDING CURRENT EMPLOYMENT:
INSERT INTO currentemprecordtable 
  (PagIbig_MID_No, Company_Code, Occupation, Employment_Status, TypeOfWork,
   Country_Of_Assignment, Date_Employed)
VALUES 
  ('1212-3434-5676', 'SKEC', 'Mechanical Engineer', 'CONTRACTUAL', 'LAND-BASED',
   'Saudi Arabia', '2023-05-01');

#-- ADDING PREVIOUS EMPLOYMENT:
INSERT INTO prevemptable 
  (PagIbig_MID_No, Company_Code, To_Date, From_Date)
VALUES 
  ('1212-3434-5676', 'SCPH', '2023-04-30', '2019-08-01');

#-- ADDING HEIRS:
INSERT INTO heirstable 
  (PagIbig_MID_No, Heirs_Name, Heirs_Relationship, Heirs_Birthdate)
VALUES 
  ('1212-3434-5676', 'Miguel Jose D. Santos', 'Grandson', '2018-04-12');