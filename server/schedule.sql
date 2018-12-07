PRAGMA foreign_keys=OFF;

CREATE TABLE IF NOT EXISTS schedule(id integer primary key not null, start_time time, name text not null, info text);
BEGIN TRANSACTION;
INSERT INTO "schedule" VALUES(1, "10:00", 'Mässan öppnar','Vi öppnar dörrarna till GÖSTA 2019. 100 st goodie bags delas ut');
INSERT INTO "schedule" VALUES(2,'11:00', 'Panel', 'Panelen diskuterar ett utvalt ämne');
INSERT INTO "schedule" VALUES(3,'13:00','Tävling', 'Var med och tävla om en sonoshögtalare, gå till GÖSTA-montern för mer info');


COMMIT;
