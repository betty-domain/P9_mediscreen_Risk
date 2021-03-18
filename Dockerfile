FROM openjdk:8-jdk-alpine
COPY target/*.jar mediscreen-risk.jar
ENTRYPOINT ["java","-jar","/mediscreen-risk.jar"]