#
# Build stage
#
FROM maven:3.8.1-openjdk-11 AS build
COPY docker .
RUN mvn clean package -Pprod -DskipTests
#
# Package stage
#
FROM openjdk:11
COPY --from=build /target/telegram-schedule-0.0.1-SNAPSHOT.jar telegram-schedule.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","telegram-schedule.jar"]

