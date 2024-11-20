FROM openjdk:17-jdk-alpine

RUN mkdir /app
RUN addgroup -S spring && adduser -S spring -G spring
WORKDIR /app

COPY build/libs/accounts-0.0.1-SNAPSHOT.jar /app/accounts-0.0.1-SNAPSHOT.jar

RUN chown -R spring:spring .
USER spring:spring

ENTRYPOINT ["java","-jar","accounts-0.0.1-SNAPSHOT.jar"]