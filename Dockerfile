FROM openjdk:8-jdk-alpine
MAINTAINER s.m.malekabadi@gmail.com
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/cloud-*.jar
ADD ${JAR_FILE} cloud.jar
ENTRYPOINT ["java","-Dspring.profiles.active=docker","-jar","/cloud.jar"]

