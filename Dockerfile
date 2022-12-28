FROM maven:3.6.3-jdk-11-slim AS maven

COPY ./src /tmp/src
COPY ./pom.xml /tmp

WORKDIR /tmp
RUN mvn clean package -DskipTests

FROM openjdk:11-jdk-slim

ARG JAR_FILE=/tmp/target/*.jar
COPY --from=maven ${JAR_FILE} app.jar

EXPOSE 8383
ENTRYPOINT ["java", "-jar", "-Dspring.profile.active=deploy", "app.jar"]