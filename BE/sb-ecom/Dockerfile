## Use an official Maven image to build the Spring Boot application
#FROM maven:3.9.9-eclipse-temurin-21 AS build
#
## Set the working directory
#WORKDIR /app
#
## Copy the pom.xml and install dependencies
#COPY pom.xml .
#RUN mvn dependency:go-offline
#
##  Copy the source code and build the application
#COPY src ./src
#RUN mvn package -DskipTests
#
## Use an official OpenJDK image to run the Spring Boot application
#FROM eclipse-temurin:21-jre
#
## Set the working directory
#WORKDIR /app
#
## Copy the built JAR file from the build stage
#COPY --from=build /app/target/sb-ecom-0.0.1-SNAPSHOT.jar .
#
## Expose the application port
#EXPOSE 8080
#
## Set the entry point to run the Spring Boot application
#ENTRYPOINT ["java", "-jar", "sb-ecom-0.0.1-SNAPSHOT.jar"]

FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q clean package -DskipTests
FROM eclipse-temurin:21-jre
WORKDIR /app
RUN useradd --system --no-create-home appuser
COPY --from=build /app/target/*.jar app.jar
USER appuser
#ENV JAVA_TOOL_OPTIONS=-XX:MaxRAMPercentage=75
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]