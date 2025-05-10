FROM amazoncorretto:11-alpine
COPY target/stockquote*.jar /usr/local/app/stockquote/lib/stockquote.jar
ENTRYPOINT ["java","-jar","/usr/local/app/stockquote/lib/stockquote.jar"]