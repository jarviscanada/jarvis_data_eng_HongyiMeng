      ******************************************************************
      * Author: Hong Yi Meng
      * Date: 2025-06-12
      * Purpose: Main program of a student registration system
      * Tectonics: cobc
      ******************************************************************
       IDENTIFICATION DIVISION.
       PROGRAM-ID. PRGMENU.
       DATA DIVISION.
       FILE SECTION.
       WORKING-STORAGE SECTION.
       01 MENU-SEPARATOR PIC X(40) VALUE
           "+--------------------------------------+".
       01 MENU-HD.
        05 FILLER PIC X VALUE "|".
        05 FILLER PIC X(9) VALUE SPACES.
        05 FILLER PIC X(17) VALUE "M A I N   M E N U".
        05 FILLER PIC X(12) VALUE SPACES.
        05 FILLER PIC X VALUE "|".
       01 MENU-SUB-HD.
        05 FILLER PIC X VALUE "|".
        05 FILLER PIC X(15) VALUE SPACES.
        05 FILLER PIC X(7) VALUE "OPTIONS".
        05 FILLER PIC X(16) VALUE SPACES.
        05 FILLER PIC X VALUE "|".
       01 MENU-OPTION.
        05 FILLER PIC X VALUE "|".
        05 FILLER PIC X(3) VALUE SPACES.
        05 OPTION-NUMBER PIC 9.
        05 FILLER PIC X(3) VALUE " - ".
        05 OPTION-TEXT PIC X(31).
        05 FILLER PIC X VALUE "|".
       01 MENU-OPTIONS.
        05 OPTIONS-COUNT PIC 9 VALUE 9.
        05 OPTION-TEXTS PIC X(31) OCCURS 9 TIMES.
       01  SUBSCRIPT PIC 99 VALUE ZEROES.

       01 PROMPT-TEXT.
        05 FILLER PIC x(30) VALUE "CHOOSE YOUR OPTION (1 TO 9) >>".
       01 USER-INPUT.
        88 EXIT-PROGRAM VALUE 9.
        05 OPTION-SELECTED PIC 9.
       PROCEDURE DIVISION.
       0100-START.
           PERFORM 0200-INIT-OPTIONS.
           PERFORM 0300-PRINT-MENU.
           PERFORM 0400-PROMPT-AND-EXECUTE.

       0200-INIT-OPTIONS.
           MOVE "GENERATE VSAM FILE" TO OPTION-TEXTS(1).
           MOVE "INSERT STUDENT DATA" TO OPTION-TEXTS(2).
           MOVE "UPDATE STUDENT DATA" TO OPTION-TEXTS(3).
           MOVE "DELETE STUDENT DATA" TO OPTION-TEXTS(4).
           MOVE "CLASS QUERY (ALL STUDENTS)" TO OPTION-TEXTS(5).
           MOVE "QUERY STUDENT BY ID" TO OPTION-TEXTS(6).
           MOVE "QUERY BY DATE OF INCLUSION" TO OPTION-TEXTS(7).
           MOVE "REPORT FILE WITH DATE BREAK" TO OPTION-TEXTS(8).
           MOVE "EXIT" TO OPTION-TEXTS(9).
       0300-PRINT-MENU.
           DISPLAY MENU-SEPARATOR.
           DISPLAY MENU-HD.
           DISPLAY MENU-SEPARATOR.
           DISPLAY MENU-SUB-HD.
           DISPLAY MENU-SEPARATOR.
           PERFORM VARYING SUBSCRIPT FROM 1 BY 1
            UNTIL SUBSCRIPT > OPTIONS-COUNT
            MOVE SUBSCRIPT TO OPTION-NUMBER
            MOVE OPTION-TEXTS(SUBSCRIPT) TO OPTION-TEXT
            DISPLAY MENU-OPTION
           END-PERFORM.
           DISPLAY MENU-SEPARATOR.
           PERFORM 0400-PROMPT-AND-EXECUTE UNTIL EXIT-PROGRAM.
           PERFORM 9000-END-PROGRAM.
       0400-PROMPT-AND-EXECUTE.
           DISPLAY PROMPT-TEXT.
           ACCEPT OPTION-SELECTED.
           EVALUATE OPTION-SELECTED
            WHEN 1
               CALL "PRGV0001"
            WHEN 2
               CALL "PRGI0002"
            WHEN 3
               CALL "PRGU0003"
            WHEN 4
               CALL "PRGD0004"
            WHEN 5
               CALL "PRGQ0005"
            WHEN 6
               CALL "PRGQ0006"
            WHEN 7
               CALL "PRGQ0007"
            WHEN 8
               CALL "PRGR0008"
           END-EVALUATE.
       9000-END-PROGRAM.
            STOP RUN.
       END PROGRAM PRGMENU.
