       IDENTIFICATION DIVISION.
       PROGRAM-ID. PRGI0002.
       ENVIRONMENT DIVISION.
       INPUT-OUTPUT SECTION.
       FILE-CONTROL.
           SELECT INSFILE ASSIGN TO "INSFILE"
           FILE STATUS IS FILE-CHECK-KEY
           ORGANIZATION IS SEQUENTIAL.
           SELECT LOGFILE ASSIGN TO "LOGFILE"
               ORGANIZATION IS SEQUENTIAL.
       DATA DIVISION.
       FILE SECTION.
       FD INSFILE
           DATA RECORD IS STUDENT-RECORD.
       COPY STUDREC.
       FD LOGFILE.
       01 LOG-DATA.
           05 LOG-LINE PIC X(80).
       WORKING-STORAGE SECTION.
           EXEC SQL
               INCLUDE SQLCA
           END-EXEC.
           EXEC SQL
               INCLUDE TSTUDENT
           END-EXEC.
           EXEC SQL
               DECLARE C1 CURSOR FOR
                   SELECT STUD_ID
                   FROM TSTUDENT
                   ORDER BY STUD_ID
           END-EXEC.
       01 WS-WORK-AREAS.
           05 FILE-CHECK-KEY PIC X(2).
           05 CURR-ID-STR PIC X(4).
           05 CURR-ID PIC 9(4).
           05 NEXT-ID PIC 9(4).
           05 NEXT-ID-STR PIC X(4).
           05  WS-CURRENT-DATE-FIELDS.
             10  WS-CURRENT-DATE PIC X(8).
              15  WS-CURRENT-YEAR    PIC  X(4).
              15  WS-CURRENT-MONTH   PIC  X(2).
              15  WS-CURRENT-DAY     PIC  X(2).
             10  FILLER PIC X(12).
           05 WS-STUDENT.
               10 WS-RNAME PIC X(25).
               10 WS-RDOB PIC X(8).
               10 WS-RCOURSE PIC X(15).
           05 FILE-ERROR.
               10 FILLER PIC X(34)
               VALUE "ERROR OPENING FILE, CODE: ".
               10 FILE-ERROR-CODE PIC X(2).
           05 SQL-ERROR.
               10 FILLER PIC X(17) VALUE "SQL ERROR, CODE: ".
               10 SQL-ERROR-CODE PIC X(2).
               10 SQL-ERROR-MSG PIC X(81).
           05 SUMMARY-LINE.
               10 SUM-TOTAL-COUNT PIC ZZZ9.
               10 FILLER PIC X(18) VALUE " LINES PROCESSED. ".
               10 SUM-ERROR-COUNT PIC ZZZ9.
               10 FILLER PIC X(8) VALUE " ERRORS.".
           05 WS-TOTAL-COUNT PIC 9(4) VALUE ZERO.
           05 WS-ERROR-COUNT PIC 9(4) VALUE ZERO.
       PROCEDURE DIVISION.
       0100-START.
           OPEN INPUT INSFILE.
           OPEN OUTPUT LOGFILE.
           IF (FILE-CHECK-KEY NOT = "00")
               DISPLAY "ERROR OPENING DATA FILE, CODE: ",
               FILE-CHECK-KEY
               MOVE FILE-CHECK-KEY TO RETURN-CODE
               PERFORM 9000-END-PROGRAM
           END-IF.
           MOVE FUNCTION CURRENT-DATE TO WS-CURRENT-DATE-FIELDS.
           PERFORM 0200-OPEN-CURSOR.
           PERFORM 0300-GET-NEXT-ID.
           PERFORM 0400-PROCESS-INPUT.
           PERFORM 0700-LOG-SUMMARY.
           PERFORM 9000-END-PROGRAM.
       0200-OPEN-CURSOR.
           EXEC SQL
               OPEN C1
           END-EXEC.
           IF SQLCODE NOT EQUAL +0
               DISPLAY "ERROR OPENING CURSOR, CODE: ",  SQLCODE
               MOVE SQLCODE TO RETURN-CODE
               GO TO 9000-END-PROGRAM
           END-IF.
       0300-GET-NEXT-ID.
           EXEC SQL SELECT MAX(STUD_ID)
               INTO :CURR-ID-STR
               FROM TSTUDENT
           END-EXEC.
           IF SQLCODE NOT EQUAL +0
               DISPLAY "ERROR GETTING NEXT ID, CODE: ",  SQLCODE
               MOVE SQLCODE TO RETURN-CODE
               GO TO 9000-END-PROGRAM
           END-IF.
           IF (CURR-ID-STR = SPACES)
               THEN MOVE 0 TO CURR-ID
           ELSE
               MOVE CURR-ID-STR TO CURR-ID
           END-IF.
           ADD 1 TO CURR-ID GIVING NEXT-ID.
       0300-GET-NEXT-ID-REAL.

       0400-PROCESS-INPUT.
           PERFORM 0600-READ-NEXT.
           PERFORM 0500-INSERT-RECORD UNTIL ENDOFFILE.
       0500-INSERT-RECORD.
           MOVE NEXT-ID TO NEXT-ID-STR.
           EXEC SQL
               INSERT INTO TSTUDENT
                   (STUD_ID, STUD_NAME, STUD_DOB, STUD_COURSE, STUD_INS_DATE)
               VALUES
                   (:NEXT-ID-STR,
                   :WS-RNAME,
                   :WS-RDOB,
                   :WS-RCOURSE,
                   :WS-CURRENT-DATE)
           END-EXEC.
           IF SQLCODE NOT EQUAL +0
               DISPLAY "ERROR INSERTING, CODE: ",  SQLCODE
           END-IF.
           ADD 1 TO NEXT-ID.
           PERFORM 0600-READ-NEXT.
       0600-READ-NEXT.
           READ INSFILE
               AT END SET ENDOFFILE TO TRUE
           END-READ.
           MOVE RSTUD-NAME TO WS-RNAME.
           MOVE RSTUD-DOB TO WS-RDOB.
           MOVE RSTUD-COURSE TO WS-RCOURSE.
       0700-LOG-SUMMARY.
           MOVE WS-TOTAL-COUNT TO SUM-TOTAL-COUNT.
           MOVE WS-ERROR-COUNT TO SUM-ERROR-COUNT.
           WRITE LOG-DATA FROM SUMMARY-LINE.
       9000-END-PROGRAM.
            EXEC SQL
                CLOSE C1
            END-EXEC.
           CLOSE INSFILE, LOGFILE.
            STOP RUN.
       END PROGRAM PRGI0002.
