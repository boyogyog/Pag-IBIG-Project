-- Easy (3 Simple)
-- #1
-- The Pag-IBIG compliance team wants to pull up all currently employed members under a permanent or regular arrangement 
-- who are assigned within the Philippines, to verify their contribution records.

SELECT PagIbig_MID_No, Occupation, Employment_Status, Country_Of_Assignment
FROM currentemprecordtable
WHERE Employment_Status = 'PERMANENT/REGULAR'
AND Country_Of_Assignment = 'Philippines';

-- #2
-- The benefits team needs to identify members who have declared immediate family members — 
-- specifically a Mother, Father, or Spouse, as heirs, to validate beneficiary eligibility for housing loan coverage.

SELECT PagIbig_MID_No, Heirs_Name, Heirs_Relationship, Heirs_Birthdate
FROM heirstable
WHERE Heirs_Relationship IN ('Mother', 'Father', 'Spouse');

-- #3
-- The Pag-IBIG finance team wants to identify members whose total monthly income falls between ₱20,000 and ₱60,000, 
-- to determine which members qualify for a specific housing loan tier based on their income bracket.

SELECT PagIbig_MID_No, Member_Name, Total_Mo_Income, Allow_Basic, Allow_Other_Sources
FROM membertable
WHERE Total_Mo_Income BETWEEN 20000 AND 60000;

-- Moderate (4 Moderate)
-- #4 
-- The Pag-IBIG compliance office wants to identify companies with significant member enrollment to prioritize them for employer contribution audits. 
-- Specifically, they need to see companies with active members, along with the average monthly income of their members and the 
-- total combined monthly income per company, filtering only Filipino citizens with an active employment status.

SELECT cd.Company_Name, COUNT(ce.PagIbig_MID_No) AS Member_Count, AVG(m.Total_Mo_Income) AS Avg_Monthly_Income, SUM(m.Total_Mo_Income) AS Total_Monthly_Income
FROM currentemprecordtable ce
JOIN companydetailstable cd ON ce.Company_Code = cd.Company_Code
JOIN membertable m ON ce.PagIbig_MID_No = m.PagIbig_MID_No
WHERE m.Citizenship= 'Filipino'
    AND m.Occupational_Status = 'EMPLOYED'
GROUP BY cd.Company_Name
HAVING COUNT(ce.PagIbig_MID_No) >= 1
ORDER BY Member_Count DESC;

-- #5 
-- The benefits team needs to review members with high dependent counts for insurance coverage assessment. 
-- List all members along with their spouse and heir count, but only show married members who have declared 
-- 2 or more heirs to prioritize them for comprehensive insurance coverage review.

SELECT m.Member_Name, m.Marital_Status, m.Spouse_Name, COUNT(h.PagIbig_MID_No) AS Heir_Count
FROM membertable m
JOIN heirstable h
ON m.PagIbig_MID_No = h.PagIbig_MID_No
WHERE m.Marital_Status = 'MARRIED'
GROUP BY m.Member_Name, m.Marital_Status, m.Spouse_Name
HAVING COUNT(h.PagIbig_MID_No) >= 2
ORDER BY Heir_Count DESC;

-- #6
-- The membership team wants to identify membership categories that have a significant number of members and strong contribution capacity. 
-- Display the membership type, number of members, average monthly income, and highest monthly income. 
-- Only include membership types with at least 3 members and an average monthly income of ₱200,000 or more. 

SELECT Membership_Type,
    	   COUNT(PagIbig_MID_No) AS Total_Members,
    	   ROUND(AVG(Total_Mo_Income), 2) AS Avg_Monthly_Income,
    	   MAX(Total_Mo_Income) AS Highest_Monthly_Income
FROM membertable
GROUP BY Membership_Type
HAVING AVG(Total_Mo_Income) >= 200000
ORDER BY Avg_Monthly_Income DESC;

-- #7
-- A Pag-IBIG branch officer needs a report of female members grouped by occupational status whose average monthly income exceeds ₱100,000. 
-- The report also includes the income range per group to support the regional director's contribution bracket assessment arrange from highest to lowest.

SELECT Occupational_Status,
    	    COUNT(PagIbig_MID_No)          AS Member_Count,
    	    MIN(Total_Mo_Income)               AS Min_Income,
    	    MAX(Total_Mo_Income)             AS Max_Income,
    	    ROUND(AVG(Total_Mo_Income), 2) AS Avg_Monthly_Income
FROM membertable
WHERE Sex = 'FEMALE'
GROUP BY Occupational_Status
HAVING AVG(Total_Mo_Income) > 100000
ORDER BY Avg_Monthly_Income DESC;

-- Difficult (3 Difficult)
-- #8
-- The PagIbig Fund office in Pasig City wants to identify which local companies have highly compensated employee groups. 
-- For each Pasig City company, display their company code, name, address, employment status, total members, total monthly income, 
-- and average monthly income. Show only groups with an average monthly income exceeding ₱100,000, grouped by company name, address, 
-- and employment status, ordered by company name and total monthly income. 

SELECT co.Company_Code,
	   co.Company_Name, 
	   co.Company_Address, 
                cu.Employment_Status,
                COUNT(m.Member_Name)   AS Total_Members,
                SUM(m.Total_Mo_Income) AS Total_Monthly_Income,
               AVG(m.Total_Mo_Income) AS Avg_Monthly_Income
FROM membertable m 
JOIN currentemprecordtable cu 
ON m.PagIbig_MID_No = cu.PagIbig_MID_No
JOIN companydetailstable co 
ON cu.Company_Code = co.Company_Code
WHERE co.Company_Address LIKE '%Pasig%'
GROUP BY co.Company_Code, co.Company_Name, co.Company_Address, cu.Employment_Status
HAVING AVG(m.Total_Mo_Income) > 100000
ORDER BY co.Company_Name, Total_Monthly_Income;

-- #9
-- The HR department wants to recognize employees who are excelling in their first job. 
-- Display the top earner of each company who has no previous employment record, showing their PagIbig MID No., name, occupation, current company and code, 
-- total monthly income, and number of heirs. Order by total monthly income descending.

SELECT m.PagIbig_MID_No AS "PagIbig MID No.",
	   m.Member_Name AS "Rookie Top Earners",
               cu.Occupation,
          	   co.Company_Code AS "Company Code",
               co.Company_Name AS "Current Company",
               m.Total_Mo_Income "Total Monthly Income",
               COUNT(h.PagIbig_MID_No) AS "No. of Heirs"
FROM membertable m JOIN currentemprecordtable cu ON m.PagIbig_MID_No = cu.PagIbig_MID_No
JOIN companydetailstable co ON co.Company_Code = cu.Company_Code
LEFT JOIN heirstable h ON h.PagIbig_MID_No = m.PagIbig_MID_No
WHERE m.Total_Mo_Income = (
	SELECT MAX(m2.Total_Mo_Income)
    FROM membertable m2 JOIN currentemprecordtable cu2 ON m2.PagIbig_MID_No = cu2.PagIbig_MID_No
    WHERE cu2.Company_Code = cu.Company_Code
) AND m.PagIbig_MID_No NOT IN (
	SELECT PagIbig_MID_No
    FROM prevemptable
)
GROUP BY m.PagIbig_MID_No,  m.Member_Name, cu.Occupation, co.Company_Code, Company_Name, m.Total_Mo_Income
ORDER BY m.Total_Mo_Income DESC;

-- #10
-- The audit team wants a report of all government-sector members currently on record.  
-- Display their PagIbig MID No., full name, age, sex, marital status, occupation, company name, employment status, 
-- years in service, and total monthly income. Order by total monthly income highest to lowest.
SELECT m.PagIbig_MID_No, m.Member_Name,
    TIMESTAMPDIFF(YEAR, m.Birthdate, CURDATE()) AS Age,
    m.Sex, m.Marital_Status,
    ce.Occupation, cd.Company_Name, ce.Employment_Status,
    TIMESTAMPDIFF(YEAR, ce.Date_Employed, CURDATE()) AS Years_In_Service,
    CONCAT('PHP', FORMAT(m.Total_Mo_Income, 2)) AS Total_Monthly_Income

FROM membertable m
JOIN currentemprecordtable ce
ON m.PagIbig_MID_No = ce.PagIbig_MID_No
JOIN companydetailstable cd
ON ce.Company_Code = cd.Company_Code

WHERE m.Membership_Category = 'GOVERNMENT'
AND ce.Employment_Status = 'PERMANENT/REGULAR'

ORDER BY m.Total_Mo_Income DESC;