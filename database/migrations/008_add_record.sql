-- ADDING MEMBER:
INSERT INTO membertable 
  (PagIbig_MID_No, Occupational_Status, Membership_Type, Membership_Type_Others, Membership_Category,
   Member_Name, Father_Name, Mother_Name, Spouse_Name, Birthdate, Marital_Status,
   Birthplace, Citizenship, Sex, CRN, Frequency_Of_Membership_Savings, TIN, SSS,
   Employee_Number, Present_Home_Address, Permanent_Home_Address,
   Preferred_Mailing_Address, Home_TelNum, Cellphone_Num, Bus_DirectLine,
   Bus_TrunkLine, Local, Email_Address, Allow_Basic, Allow_Other_Sources, Total_Mo_Income)
VALUES 
  ('1212-3434-5678', 'EMPLOYED', 'EMPLOYED', NULL, 'PRIVATE',
   'Peter Partel', 'Ben Partel', 'May Partel', NULL, '1997-04-22', 'SINGLE',
   'Cebu City', 'Filipino', 'MALE', '222492923485', 'Monthly', '123-456-222-02', '13-2342289-8',
   123428226, '6 Gateway St., Barangay 167, Caloocan', '6 Gateway St., Barangay 167, Caloocan',
   'Present Home Address', '(02) 8324-5656', '0917-591-6348', NULL,
   NULL, NULL, 'petpat88@gmail.com', 43200.00, NULL, 43200.00);

   
-- ADDING CURRENT EMPLOYMENT:
INSERT INTO currentemprecordtable 
  (PagIbig_MID_No, Company_Code, Occupation, Employment_Status, TypeOfWork,
   Country_Of_Assignment, Date_Employed)
VALUES 
  ('1212-3434-5678', 'HWI', 'EMPLOYED', 'PERMANENT/REGULAR', NULL,
   'Philippines', '2020-02-15');

   
-- ADDING PREVIOUS EMPLOYMENT:
INSERT INTO prevemptable 
  (PagIbig_MID_No, Company_Code, To_Date, From_Date)
VALUES 
  ('1212-3434-5678', 'ACN', '2020-01-15', '2018-10-22');

  
-- ADDING HEIRS:
INSERT INTO heirstable 
  (PagIbig_MID_No, Heirs_Name, Heirs_Relationship, Heirs_Birthdate)
VALUES 
  ('1212-3434-5678', 'Ben Partel', 'Father', '1967-12-09'),
  ('1212-3434-5678', 'May Partel', 'Mother', '1968-04-07');