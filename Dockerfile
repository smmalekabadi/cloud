FROM alpine:edge
MAINTAINER s.m.malekabadi@gmail.com
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=./target/cloud-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} cloud.jar
ENTRYPOINT ["java","-jar","/cloud.jar"]

