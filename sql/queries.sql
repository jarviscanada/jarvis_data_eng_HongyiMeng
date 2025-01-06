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


-- Aggregation

-- Exercise 17 count 3

SELECT
    recommendedby,
    COUNT (*) as count
FROM
    cd.members
WHERE
    recommendedby IS NOT NULL
GROUP BY
    recommendedby
ORDER BY
    recommendedby;

-- Exercise 18 fachours

SELECT
    f.facid,
    SUM(slots) AS "Total Slots"
FROM
    cd.facilities f
        LEFT JOIN cd.bookings b on f.facid = b.facid
GROUP BY
    f.facid
ORDER BY
    f.facid;

-- Exercise 19 facours by month

SELECT
    f.facid,
    SUM (slots) AS "Total Slots"
FROM
    cd.facilities f
        LEFT JOIN cd.bookings b ON f.facid = b.facid
WHERE
    starttime >= '2012-09-01'
  AND starttime < '2012-10-01'
GROUP BY
    f.facid
ORDER BY
    "Total Slots";

-- Exercise 20 fachours by month 2

SELECT
    facid,
    EXTRACT (
            MONTH
            FROM
            starttime
    ) AS month,
  SUM(slots) AS "Total Slots"
FROM
    cd.bookings
WHERE
    EXTRACT (
    YEAR
    FROM
    starttime
    ) = '2012'
GROUP BY
    facid,
    EXTRACT (
    MONTH
    FROM
    starttime
    )
ORDER BY
    facid,
    month;

-- Exercise 21 members 1

SELECT
    COUNT(DISTINCT memid)
FROM
    cd.bookings;

-- Exercise 22 n booking

SELECT
    surname,
    firstname,
    member.memid,
    starttime
FROM
    cd.members member
        JOIN (
        SELECT
            cd.bookings.memid,
            MIN(starttime) as starttime
        FROM
            cd.bookings
        WHERE
            starttime >= '2012-09-01'
        GROUP BY
            cd.bookings.memid
    ) booking ON member.memid = booking.memid
ORDER BY
    member.memid;

-- Exercise 23 count members

SELECT
    COUNT (*) OVER () as COUNT,
  firstname,
  surname
FROM
    cd.members
ORDER BY
    joindate;

-- Exercise 24 num members

SELECT
    COUNT (*) OVER (
    ORDER BY
      joindate
  ),
        firstname,
    surname
FROM
    cd.members
ORDER BY
    joindate;

-- Exercise 25 fachours 4

SELECT
    facid,
    total
FROM
    (
        SELECT
            facid,
            SUM(slots) AS total,
            RANK () OVER(
        ORDER BY
          SUM(slots) DESC
      ) as rank
        FROM
            cd.bookings
        GROUP BY
            facid
    ) as sub
WHERE
    rank = 1;

-- String

-- Exercise 26 concat

SELECT
    CONCAT(surname, ', ', firstname)
FROM
    cd.members;

-- Exercise 27 reg

SELECT
    memid,
    telephone
FROM
    cd.members
WHERE
    telephone ~ '[()]'
ORDER BY
    memid;

-- Exercise 28 substr

SELECT
    SUBSTR (surname, 1, 1) as letter,
    COUNT (*)
FROM
    cd.members
GROUP BY
    letter
ORDER BY
    letter;
