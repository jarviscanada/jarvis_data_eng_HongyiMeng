# Introduction

In this project we created a COBOL program to manage students in a educational institution. The program can be used to add, remove, update and query students.
A report can also be generated grouping students by the classes they are registered in.

Technologies:

- COBOL
- OpenCobolIDE

# Quick Start

### Install OpenCobolIDE

Install the [OpenCobolIDE](https://launchpad.net/cobcide/+download) to compile and execute the program.

### Change Subprogram Types

For the subprograms ending in 000X.cbl, change the program type to "module" by opening the file in OpencobolIDE, then clicking the COBOL menu on top, finally click Program Type and select Module.

### Move the Data File

When the program is compiled, a `bin` folder will be created containing the executable. Move the data file [./STUDENTS_SEQ.DAT](./STUDENTS_SEQ.DAT) into the bin folder before executing the program.
