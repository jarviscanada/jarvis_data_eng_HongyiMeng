CREATE SCHEMA IF NOT EXISTS cd;

CREATE TABLE IF NOT EXISTS cd.members (
    memid INTEGER NOT NULL PRIMARY KEY,
    surname VARCHAR(200) NOT NULL,
    firstname VARCHAR(200) NOT NULL,
    address VARCHAR(300) NOT NULL,
    zipcode INTEGER NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    recommendedby INTEGER,
    joindate TIMESTAMP NOT NULL,
    CONSTRAINT fk_recommendedby FOREIGN KEY (recommendedby) REFERENCES cd.members (memid) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS cd.facilities (
    facid INTEGER NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    membercost NUMERIC NOT NULL,
    guestcost NUMERIC NOT NULL,
    initialoutlay NUMERIC NOT NULL,
    monthlymaintenace NUMERIC NOT NULL
);

CREATE TABLE IF NOT EXISTS cd.bookings (
    bookid INTEGER NOT NULL PRIMARY KEY,
    facid INTEGER NOT NULL REFERENCES cd.facilities (facid),
    membid INTEGER NOT NULL REFERENCES cd.members (memid),
    starttime TIMESTAMP NOT NULL,
    slots INTEGER NOT NULL
);