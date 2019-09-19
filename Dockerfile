FROM openjdk:8-jdk-alpine

WORKDIR /marcc
COPY /target/marcc-rest-1.0.0-SNAPSHOT.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "marcc-rest-1.0.0-SNAPSHOT.jar"]