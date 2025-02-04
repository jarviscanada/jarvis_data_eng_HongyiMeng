# Introduction

This project consists of two implementation of a grep app in Java. Grep is a command-line utility for global regular expression search and print. 
The application takes in three command-line argument: a regular expression, a root path, and an output file. The app reads in all the 
text files under the root path and writes lines matching the regular expression in the output file.
The first implementation is straight-forward, while the second is an improved version where Java Streams were used. 

Technologies:
 - git
 - Maven
 - SLF4J
 - Docker
 - Stream API
 - lambda functions

# Quick Start
### Run as a jar
```
mvn clean compile package
java -cp target/grep-1.0-SNAPSHOT.jar ca.jrvs.apps.grep.JavaGrepImp REGEX ROOT_PATH OUTPUT_FILE
```
To run the app with Java Stream, replace `ca.jrvs.apps.grep.JavaGrepImp` with `ca.jrvs.apps.grep.JavaGrepLambdaImp`.



#Implemenation
## Pseudocode
```
process() {
    matchedLines = []
    
    files = listFiles(rootPath)
    for (file in files) {
        lines = readLines(file)
        for (line in lines) {
            if(containPattern(line)) {
                matchedLines.add(line)
            }
        }
    }
    writeToFile(matchedLines)
}
```

## Performance Issue
Performance, specifically memory related, issues could arise when the input file size are too large. Since the app 
reading every text file and scan the lines one by one, large files could cause OutOfMemory errors. This can be solved 
by using a BufferedReader, which read in files in chunks. 

# Test
The application was tested manually with the sample text file under grep/data. Various regular expressions and input 
paths were used, and the output compared with the expected result.

# Deployment
The program was packaged using Maven and a Docker image created from the JAR. The image is pushed to Dockerhub where it 
can be shared.

# Improvement
 - create unit tests for the methods in JavaGrepImp
 - update the JavaGrep interface method signatures to only use streams
 - avoid the use of the helper method when listing files