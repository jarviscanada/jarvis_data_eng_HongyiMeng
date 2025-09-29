       IDENTIFICATION DIVISION.
       PROGRAM-ID. PRGR0007.
       ENVIRONMENT DIVISION.
       INPUT-OUTPUT SECTION.
       FILE-CONTROL.
           SELECT REPFILE ASSIGN TO "REPFILE"
               ORGANIZATION IS SEQUENTIAL.
           SELECT LOGFILE ASSIGN TO "LOGFILE"
               ORGANIZATION IS SEQUENTIAL.
       DATA DIVISION.
       FILE SECTION.
       FD REPFILE.
       01 REP-DATA.
           05 REP-LINE PIC X(91).
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
                   SELECT STUD_ID,
                       STUD_NAME,
                       STUD_DOB,
                       STUD_COURSE,
                       STUD_INS_DATE,
                       STUD_UPDT_DATE
                   FROM TSTUDENT
                   ORDER BY STUD_INS_DATE DESC NULLS LAST
           END-EXEC.
       01 WS-WORK-AREAS.
           88 NO-MORE-ROWS VALUE HIGH-VALUE.
           05  PREV-DATE PIC X(8).
           05 REPORT-SEPERATOR.
               10 FILLER PIC X(91) VALUE "------------------------------
      -        "--------------------------------------------------------
      -        "-----".
           05 REPORT-TITLE.
               10 FILLER PIC X(32) VALUE SPACES.
               10 FILLER PIC X(23) VALUE "STUDENTS BY INSERT DATE".
               10 FILLER PIC X(36) VALUE SPACES.
           05 REPORT-HEADER.
               10 FILLER PIC X VALUE SPACES.
               10 FILLER PIC X(2) VALUE "ID".
               10 FILLER PIC X(3) VALUE SPACES.
               10 FILLER PIC X VALUE "|".
               10 FILLER PIC X(1) VALUE SPACES.
               10 FILLER PIC X(12) VALUE "STUDENT NAME".
               10 FILLER PIC X(14) VALUE SPACES.
               10 FILLER PIC X VALUE "|".
               10 FILLER PIC X(1) VALUE SPACES.
               10 FILLER PIC X(8) VALUE "BIRTHDAY".
               10 FILLER PIC X(1) VALUE SPACES.
               10 FILLER PIC X VALUE "|".
               10 FILLER PIC X(1) VALUE SPACES.
               10 FILLER PIC X(6) VALUE "COURSE".
               10 FILLER PIC X(10) VALUE SPACES.
               10 FILLER PIC X VALUE "|".
               10 FILLER PIC X(1) VALUE SPACES.
               10 FILLER PIC X(11) VALUE "INSERT DATE".
               10 FILLER PIC X(1) VALUE SPACES.
               10 FILLER PIC X VALUE "|".
               10 FILLER PIC X(1) VALUE SPACES.
               10 FILLER PIC X(11) VALUE "UPDATE DATE".
               10 FILLER PIC X(1) VALUE SPACES.
           05 REPORT-RECORD.
               10 FILLER PIC X VALUE SPACES.
               10 REPORT-STUDENT-ID PIC 9(4).
               10 FILLER PIC X(1) VALUE SPACES.
               10 FILLER PIC X VALUE "|".
               10 FILLER PIC X(1) VALUE SPACES.
               10 REPORT-STUDENT-NAME PIC X(25).
               10 FILLER PIC X(1) VALUE SPACES.
               10 FILLER PIC X VALUE "|".
               10 FILLER PIC X(1) VALUE SPACES.
               10 REPORT-STUDENT-BDAY PIC 9(8).
               10 FILLER PIC X(1) VALUE SPACES.
               10 FILLER PIC X VALUE "|".
               10 FILLER PIC X(1) VALUE SPACES.
               10 REPORT-COURSE PIC X(15).
               10 FILLER PIC X(1) VALUE SPACES.
               10 FILLER PIC X VALUE "|".
               10 FILLER PIC X(1) VALUE SPACES.
               10 REPORT-REC-INCLUSION-DATE PIC 9(8).
               10 FILLER PIC X(4) VALUE SPACES.
               10 FILLER PIC X VALUE "|".
               10 FILLER PIC X(1) VALUE SPACES.
               10 REPORT-REC-UPDATE-DATE PIC 9(8).
               10 FILLER PIC X(4) VALUE SPACES.
           05 REPORT-TOTAL-LINE.
               10 FILLER PIC X(17) VALUE "TOTAL STUDENTS : ".
               10 REPORT-TOTAL PIC ZZZ9.
           05 WS-RECORD-COUNT PIC 9999.
       PROCEDURE DIVISION.
       0100-START.
           OPEN INPUT REPFILE.
           PERFORM 0200-OPEN-CURSOR.
           PERFORM 0300-WRITE-REPORT.
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
       0300-WRITE-REPORT.
           WRITE REP-DATA FROM REPORT-SEPERATOR AFTER ADVANCING 1 LINE.
           WRITE REP-DATA FROM REPORT-TITLE AFTER ADVANCING 1 LINE.
           WRITE REP-DATA FROM REPORT-SEPERATOR AFTER ADVANCING 1 LINE.
           WRITE REP-DATA FROM REPORT-HEADER AFTER ADVANCING 1 LINE.
           WRITE REP-DATA FROM REPORT-SEPERATOR AFTER ADVANCING 1 LINE.
           PERFORM 0400-WRITE-RECORD UNTIL NO-MORE-ROWS.
           MOVE WS-STUDENT-COUNT TO REPORT-TOTAL.
           WRITE REP-DATA FROM REPORT-TOTAL-LINE AFTER ADVANCING 2 LINE.
           PERFORM 9000-END-PROGRAM.
       0400-WRITE-RECORD.
           EXEC SQL
               FETCH C1
               INTO :REPORT-STUDENT-ID,
                    :REPORT-STUDENT-NAME,
                    :REPORT-STUDENT-BDAY,
                    :REPORT-COURSE,
                    :REPORT-REC-INCLUSION-DATE,
                    :REPORT-REC-UPDATE-DATE
           END-EXEC.
           IF SQLCODE EQUAL +0
             IF PREV-DATE EQ REPORT-REC-UPDATE-DATE
               WRITE REP-DATA FROM REPORT-RECORD AFTER ADVANCING 1 LINE
             ELSE
               MOVE REPORT-REC-UPDATE-DATE TO PREV-DATE
               WRITE REP-DATA FROM REPORT-RECORD AFTER ADVANCING 2 LINE
             END-IF
             ADD 1 TO WS-STUDENT-COUNT
           ELSE
               SET NO-MORE-RECORD TO TRUE
           END-IF.
           PERFORM 0400-READ-NEXT.
       9000-END-PROGRAM.
           EXEC SQL
                CLOSE C1
           END-EXEC.
           CLOSE REPFILE.
           STOP RUN.
       END PROGRAM PRGR0007.
