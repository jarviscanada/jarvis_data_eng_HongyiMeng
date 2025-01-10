# Introduction

This project is a practical exercise to get more familiarity with SQL operations. First, we explored
how to create tables in SQL. Then, we dug into how to perform CRUD operations in an SQL database.

# SQL Queries

###### Table Setup (DDL)

```postgresql
CREATE SCHEMA IF NOT EXISTS cd;

CREATE TABLE IF NOT EXISTS cd.members
(
    memid         INTEGER      NOT NULL PRIMARY KEY,
    surname       VARCHAR(200) NOT NULL,
    firstname     VARCHAR(200) NOT NULL,
    address       VARCHAR(300) NOT NULL,
    zipcode       INTEGER      NOT NULL,
    telephone     VARCHAR(20)  NOT NULL,
    recommendedby INTEGER,
    joindate      TIMESTAMP    NOT NULL,
    CONSTRAINT fk_recommendedby FOREIGN KEY (recommendedby) REFERENCES cd.members (memid) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS cd.facilities
(
    facid             INTEGER      NOT NULL PRIMARY KEY,
    name              VARCHAR(100) NOT NULL,
    membercost        NUMERIC      NOT NULL,
    guestcost         NUMERIC      NOT NULL,
    initialoutlay     NUMERIC      NOT NULL,
    monthlymaintenace NUMERIC      NOT NULL
);

CREATE TABLE IF NOT EXISTS cd.bookings
(
    bookid    INTEGER   NOT NULL PRIMARY KEY,
    facid     INTEGER   NOT NULL REFERENCES cd.facilities (facid),
    membid    INTEGER   NOT NULL REFERENCES cd.members (memid),
    starttime TIMESTAMP NOT NULL,
    slots     INTEGER   NOT NULL,
);
```

###### Question 1: Insert some data into a table

```
INSERT INTO cd.facilities (
    facid, name, membercost, guestcost,
    initialoutlay, monthlymaintenance
)
VALUES
    (9, 'Spa', 20, 30, 100000, 800);
```

###### Question 2: Insert calculated data into a table

```
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
```

###### Question 3: Update some existing data

```
UPDATE
    cd.facilities
SET
    initialoutlay = 10000
WHERE
    facid = 1;
```

###### Question 4: Update a row based on the contents of another row

```
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
```

###### Question 5: Delete all bookings

```
DELETE FROM
    cd.bookings;
```

###### Question 6: Delete a member from the cd.members table

```
DELETE FROM
    cd.members
WHERE
    memid = 37;
```

###### Question 7: Control which rows are retrieved - part 2

```
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
```

###### Question 8: Basic string searches

```
SELECT
    *
FROM
    cd.facilities
WHERE
    name LIKE '%Tennis%';
```

###### Question 9: Matching against multiple possible values

```
SELECT
    *
FROM
    cd.facilities
WHERE
    facid in (1, 5);
```

###### Question 10: Working with dates

```
SELECT
    memid,
    surname,
    firstname,
    joindate
FROM
    cd.members
WHERE
    joindate >= '2012-09-01';
```

###### Question 11: Combining results from multiple queries

```
SELECT
    surname
FROM
    cd.members
UNION
SELECT
    name
FROM
    cd.facilities;
```

###### Question 12: Retrieve the start times of members' bookings

```
SELECT
    starttime
FROM
    cd.bookings
        JOIN cd.members ON cd.members.memid = cd.bookings.memid
WHERE
    CONCAT(firstname, ' ', surname) = 'David Farrell';
```

###### Question 13: Work out the start times of bookings for tennis courts

```
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
```

###### Question 14: Produce a list of all members, along with their recommender

```
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
```

###### Question 15: Produce a list of all members who have recommended another member

```
SELECT
    DISTINCT rec.firstname,
             rec.surname
FROM
    cd.members mem
        JOIN cd.members rec ON mem.recommendedby = rec.memid
ORDER BY
    surname,
    firstname ASC;
```

###### Question 16: Produce a list of all members, along with their recommender, using no joins

```
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
```

###### Question 17: Count the number of recommendations each member makes

```
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
```

###### Question 18: List the total slots booked per facility

```
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
```

###### Question 19: List the total slots booked per facility in a given month

```
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
```

###### Question 20: List the total slots booked per facility per month

```
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
```

###### Question 21: Find the count of members who have made at least one booking

```
SELECT
    COUNT(DISTINCT memid)
FROM
    cd.bookings;
```

###### Question 22: List each member's first booking after September 1st 2012

```
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
```

###### Question 23: Produce a list of member names, with each row containing the total member count

```
SELECT
    COUNT (*) OVER () as COUNT,
  firstname,
  surname
FROM
    cd.members
ORDER BY
    joindate;
```

###### Question 24: Produce a numbered list of members

```
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
```

###### Question 25: Output the facility id that has the highest number of slots booked, again

```
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
```

###### Question 26: Format the names of members

```
SELECT
    CONCAT(surname, ', ', firstname)
FROM
    cd.members;
```

###### Question 27: Find telephone numbers with parentheses

```
SELECT
    memid,
    telephone
FROM
    cd.members
WHERE
    telephone ~ '[()]'
ORDER BY
    memid;
```

###### Question 28: Count the number of members whose surname starts with each letter of the alphabet

```
SELECT
    SUBSTR (surname, 1, 1) as letter,
    COUNT (*)
FROM
    cd.members
GROUP BY
    letter
ORDER BY
    letter;
```



