PRAGMA foreign_keys=OFF;

CREATE TABLE IF NOT EXISTS companies(id integer primary key not null, name text not null, email text, info text, fileName text, caseNo integer);
BEGIN TRANSACTION;
INSERT INTO "companies" VALUES(1,'Acando','acando@mail.se', 'Text om Acando','Acando.png', 1);
INSERT INTO "companies" VALUES(2,'ATEA','info@atea.se', 'Text om ATEA:s företag','ATEA.png', 2);
INSERT INTO "companies" VALUES(3,'Academic Work','info@academicwork.se', 'Här finns info om Academic Work','AcademicWork.png', 3);
INSERT INTO "companies" VALUES(4,'ÅF','info@af.se', 'Här står info om ÅF','AF.png', 4);
INSERT INTO "companies" VALUES(5,'Alektum Group','info@alektum.com', 'Information om Alektum Group','AlektumGroup.png', 5);
INSERT INTO "companies" VALUES(6,'CGI','info@cgi.com', 'Text om CGI','CGI.png', 6);
INSERT INTO "companies" VALUES(7,'Canea','info@canea.com', 'Information om Canea','Canea.png', 7);
INSERT INTO "companies" VALUES(8,'Capgemini','info@capgemini.com', 'Capgeminis information går här','Capgemini.png', 8);
INSERT INTO "companies" VALUES(9,'Centiro','info@centiro.com', 'Här finns info om Centiro','Centiro.png', 9);
INSERT INTO "companies" VALUES(10,'FourOne','info@fourone.se', 'FourOne text text text','FourOne.png', 10);
INSERT INTO "companies" VALUES(11,'IFS','info@ifsworld.com', 'Här kommer information om IFS','IFS.png', 11);
INSERT INTO "companies" VALUES(12,'Knowit','info@knowit.se', 'Här kommer information om Knowit','Knowit.png', 12);
INSERT INTO "companies" VALUES(13,'Lime','info@lime.com', 'Information om företaget Lime','Lime.png', 13);
INSERT INTO "companies" VALUES(14,'PwC','info@pwc.com', 'Här finns info om PwC','PwC.png', 14);
INSERT INTO "companies" VALUES(15,'Sigma','info@sigmatechnologies.com', 'Här står det info om Sigma','Sigma.png', 15);
INSERT INTO "companies" VALUES(16,'Skatteverket','info@skatteverket.se', 'Information om Skatteverket','Skatteverket.png', 16);
INSERT INTO "companies" VALUES(17,'Stratsys','info@stratsys.se', 'Text om Stratsys','Stratsys.png', 17);
INSERT INTO "companies" VALUES(18,'Trafikverket','info@trafikverket.se', 'Information om Trafikverket','Trafikverket.png', 18);
INSERT INTO "companies" VALUES(19,'WirelessCar','info@wirelesscar.com', 'Här finns info om WirelessCar','WirelessCar.png', 19);




COMMIT;
