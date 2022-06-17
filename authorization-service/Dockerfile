FROM openjdk:8-jdk-alpine
WORKDIR /
ADD target/authorization-service.jar authorization-service.jar
EXPOSE 8100
CMD java -jar authorization-service.jar