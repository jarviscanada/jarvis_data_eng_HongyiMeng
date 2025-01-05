-- Modifying data

-- Exercise 1 insert

INSERT INTO cd.facilities (
    facid, name, membercost, guestcost,
    initialoutlay, monthlymaintenance
)
VALUES
    (9, 'Spa', 20, 30, 100000, 800);

-- Exercise 2 insert 3

INSERT INTO cd.facilities (
    facid, name, membercost, guestcost,
    initialoutlay, monthlymaintenance
)
VALUES
    (
        (
            SELECT
                MAX(facid) + 1
            FROM
                cd.facilities
        ),
        'Spa',
        20,
        30,
        100000,
        800
    );

-- Exercise 3 update

UPDATE
    cd.facilities
SET
    initialoutlay = 10000
WHERE
    facid = 1;

-- Exercise 4 update calculated

UPDATE
    cd.facilities
SET
    membercost = (
                     SELECT
                         membercost
                     FROM
                         cd.facilities
                     WHERE
                         facid = 0
                 ) * 1.1,
    guestcost = (
                    SELECT
                        guestcost
                    FROM
                        cd.facilities
                    WHERE
                        facid = 0
                ) * 1.1
WHERE
    facid = 1;

-- Exercise 5 delete

DELETE FROM
    cd.bookings;

-- Exercise 6 delete wh

DELETE FROM
    cd.members
WHERE
    memid = 37;

-- Basics

-- Exercise 7 where 2

SELECT
    facid,
    name,
    membercost,
    monthlymaintenance
FROM
    cd.facilities
WHERE
    membercost > 0
  AND membercost * 50 < monthlymaintenance;

-- Exercise 8 where 3

SELECT
    *
FROM
    cd.facilities
WHERE
    name LIKE '%Tennis%';

-- Exercise 9 where 4

SELECT
    *
FROM
    cd.facilities
WHERE
    facid in (1, 5);

-- Exercise 10 date

SELECT
    memid,
    surname,
    firstname,
    joindate
FROM
    cd.members
WHERE
    joindate >= '2012-09-01';

-- Exercise 11 union

SELECT
    surname
FROM
    cd.members
UNION
SELECT
    name
FROM
    cd.facilities;

-- Exercise 12 simple join

SELECT
    starttime
FROM
    cd.bookings
        JOIN cd.members ON cd.members.memid = cd.bookings.memid
WHERE
    CONCAT(firstname, ' ', surname) = 'David Farrell';

-- Exercise 13 simples join 2

SELECT
    starttime AS start,
    name
FROM
    cd.bookings
        JOIN cd.facilities ON cd.bookings.facid = cd.facilities.facid
WHERE
    DATE(cd.bookings.starttime) = '2012-09-21'
  AND name LIKE '%Tennis Court%'
ORDER BY
    starttime ASC;

-- Exercise 14 self 2

SELECT
    mem.firstname AS memfname,
    mem.surname AS memsname,
    rec.firstname AS recfname,
    rec.surname AS recsname
FROM
    cd.members mem
        LEFT JOIN cd.members rec ON mem.recommendedby = rec.memid
ORDER BY
    mem.surname,
    mem.firstname ASC;

-- Exercise 15 self

SELECT
    DISTINCT rec.firstname,
             rec.surname
FROM
    cd.members mem
        JOIN cd.members rec ON mem.recommendedby = rec.memid
ORDER BY
    surname,
    firstname ASC;

-- Exercise 16 sub

SELECT
    DISTINCT CONCAT(mem.firstname, ' ', mem.surname) AS member,
             (
                 SELECT
                     CONCAT(rec.firstname, ' ', rec.surname) AS recommender
                 FROM
                     cd.members rec
                 WHERE
                     rec.memid = mem.recommendedby
             )
FROM
    cd.members mem
ORDER BY
    member,
    recommender;

