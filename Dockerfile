FROM maven:3.6.3-jdk-8-slim AS MAVEN_TOOL_CHAIN
WORKDIR /src
ADD pom.xml /src/pom.xml
ADD src /src/src
RUN mvn package -Dmaven.test.skip=true
FROM openjdk:8-jdk-alpine
MAINTAINER s.m.malekabadi@gmail.com
VOLUME /tmp
EXPOSE 2028
ARG JAR_FILE=target/cloud-*.jar
ADD ${JAR_FILE} cloud.jar
ENTRYPOINT ["java","-Dspring.profiles.active=docker","-jar","/cloud.jar"]

