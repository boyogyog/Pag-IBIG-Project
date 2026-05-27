#--Inpudate ko yung company code ni Raven

update companydetailstable
set Company_Code = "ORC"
where company_code = "ORC2";

update companydetailstable
set Company_Code = "ORC1"
where company_code = "ORC";

SELECT * from companydetailstable;

#--Inpudate ko rin yung domain ng ibang attributes natin, from enum to varchar.alter
#--compdetails
ALTER TABLE `pagibig`.`companydetailstable` 
CHANGE COLUMN `Office_Assignment` `Office_Assignment` VARCHAR(11) NOT NULL ;

#--currentemp
ALTER TABLE `pagibig`.`currentemprecordtable` 
CHANGE COLUMN `Employment_Status` `Employment_Status` VARCHAR(17) NULL DEFAULT NULL ,
CHANGE COLUMN `TypeOfWork` `TypeOfWork` VARCHAR(10) NULL DEFAULT NULL ,
CHANGE COLUMN `Country_Of_Assignment` `Country_Of_Assignment` VARCHAR(25) NULL ;

#--heirs - wala
#--member
ALTER TABLE `pagibig`.`membertable` 
CHANGE COLUMN `Occupational_Status` `Occupational_Status` VARCHAR(21) NOT NULL ,
CHANGE COLUMN `Membership_Type` `Membership_Type` VARCHAR(24) NOT NULL ,
CHANGE COLUMN `Membership_Category` `Membership_Category` VARCHAR(30) NOT NULL ,
CHANGE COLUMN `Marital_Status` `Marital_Status` VARCHAR(17) NOT NULL ,
CHANGE COLUMN `Sex` `Sex` VARCHAR(6) NOT NULL ;

#--prevemp - wala


