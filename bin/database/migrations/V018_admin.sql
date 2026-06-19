-- Run this in MySQL Workbench against the pagibig database

CREATE TABLE IF NOT EXISTS admincredentials (
    Admin_ID       INT          NOT NULL AUTO_INCREMENT,
    Username       VARCHAR(50)  NOT NULL UNIQUE,
    Password       VARCHAR(255) NOT NULL,
    Admin_Name     VARCHAR(100) NOT NULL,
    PRIMARY KEY (Admin_ID)
);

-- Default admin account: username = admin, password = admin123
INSERT INTO admincredentials (Username, Password, Admin_Name)
VALUES ('admin', 'admin123', 'System Administrator');

-- merge membershiptype others
-- Copy "Others" value into Membership_Type where it's not null
SET SQL_SAFE_UPDATES = 0;

-- Merge Membership_Type_Others into Membership_Type
UPDATE membertable 
SET Membership_Type = Membership_Type_Others
WHERE Membership_Type_Others IS NOT NULL AND Membership_Type_Others != '';

-- Merge Membership_Category_Others into Membership_Category  
UPDATE membertable 
SET Membership_Category = Membership_Category_Others
WHERE Membership_Category_Others IS NOT NULL AND Membership_Category_Others != '';

-- Increase varchar size and drop the Others columns
ALTER TABLE membertable 
    MODIFY COLUMN Membership_Type VARCHAR(100),
    MODIFY COLUMN Membership_Category VARCHAR(100),
    DROP COLUMN Membership_Type_Others,
    DROP COLUMN Membership_Category_Others;