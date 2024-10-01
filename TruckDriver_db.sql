USE TruckDriverDB;
GO

BEGIN TRANSACTION;

CREATE TABLE "ACCOUNT" (
	"AccountId"	INTEGER,
	"FirstName"	TEXT,
	"LastName"	TEXT,
	"PhoneNumber"	INTEGER,
	"Email"	TEXT,
	"Password"	TEXT,
	"Enable"	INTEGER,
	"DriverStatus"	INTEGER,
	PRIMARY KEY("AccountId")
);
CREATE TABLE "Session" (
	"AccountId"	INTEGER,
	"TimeBegin"	TEXT,
	"TimeEnd"	TEXT,
	"IsON"	INTEGER
);
CREATE TABLE "Vehicle" (
	"VehicleId"	INTEGER	NOT NULL IDENTITY (1, 1),
	"Plate"	TEXT,
	"Vin"	TEXT,
	"Manufacturer"	TEXT,
	"Model"	TEXT,
	"Color"	TEXT,
	"Capacity"	TEXT,
	"Trailer"	TEXT,
	"AccountId"	INTEGER,
	PRIMARY KEY("VehicleId")
);
CREATE TABLE "TrailerType" (
	"TrailerTypeId"	INTEGER	NOT NULL IDENTITY (1, 1),
	"TrailerTypeName"	TEXT,
	PRIMARY KEY("TrailerTypeId")
);
CREATE TABLE "ACTIVITY" (
	"activityId"	INTEGER,
	"contractor"	TEXT,
	"contractorEmployee"	TEXT,
	"contractorPhone"	TEXT,
	"destination"	TEXT,
	"TypeOfLoad"	TEXT,
	"ShippingOffer"	TEXT,
	"activityStatus"	INTEGER,
	"activityDate"	TEXT,
	"AccountId"	INTEGER,
	PRIMARY KEY("activityId")
);
COMMIT;
