-- 3 Basic SQL Codes
-- 1. Get all members count
SELECT *
FROM companydetailstable;

-- 2. Get a specific member by Pag-IBIG MID
SELECT * FROM membertable WHERE PagIbig_MID_No = '1212-3434-5657';

-- 3. Get all companies that are HEAD OFFICE
SELECT * FROM companydetailstable WHERE Office_Assignment = 'HEAD OFFICE';

-- 4 Moderate SQL Codes
-- 1. Get member name with their current company name
SELECT m.Member_Name, c.Company_Name, e.Occupation
FROM membertable m
JOIN currentemprecordtable e ON m.PagIbig_MID_No = e.PagIbig_MID_No
JOIN companydetailstable c ON e.Company_Code = c.Company_Code;

-- 2. Count members per membership category
SELECT Membership_Category, COUNT(*) AS Total
FROM membertable
GROUP BY Membership_Category;

-- 3. Get all previous employers of a specific member
SELECT m.Member_Name, c.Company_Name, p.From_Date, p.To_Date
FROM prevemptable p
JOIN membertable m ON p.PagIbig_MID_No = m.PagIbig_MID_No
JOIN companydetailstable c ON p.Company_Code = c.Company_Code
WHERE p.PagIbig_MID_No = '1212-3434-5656';

-- 4. Get members earning above a certain income, ordered highest first
SELECT Member_Name, Total_Mo_Income
FROM membertable
WHERE Total_Mo_Income > 100000
ORDER BY Total_Mo_Income DESC;

-- 3 Difficult SQL Codes
-- 1. Get members who have MORE than 1 previous employer
SELECT m.Member_Name, COUNT(p.Prev_Emp_Code) AS Prev_Employers
FROM membertable m
JOIN prevemptable p ON m.PagIbig_MID_No = p.PagIbig_MID_No
GROUP BY m.PagIbig_MID_No
HAVING COUNT(p.Prev_Emp_Code) > 1;

-- 2. Get members whose total income is above the average income of all members
SELECT Member_Name, Total_Mo_Income
FROM membertable
WHERE Total_Mo_Income > (
    SELECT AVG(Total_Mo_Income) FROM membertable
);

-- 3. Get members who have NO heirs recorded yet
SELECT Member_Name
FROM membertable
WHERE PagIbig_MID_No NOT IN (
    SELECT PagIbig_MID_No FROM heirstable
);
