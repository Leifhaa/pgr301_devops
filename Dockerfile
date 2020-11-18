#Multistage build:
#Step 1: Compile and build code
FROM maven:3.6-jdk-11 as builder
#Create /app catalog
WORKDIR /app
#Copy POM file to catalog
COPY pom.xml .
COPY src ./src
##Package with 1 thread per core
RUN mvn -T 1C package -DskipTests



#Step 2: Push to container registry.
# Use AdoptOpenJDK for base image
# It's important to use OpenJDK 8u191 or above that has container support enabled.
# https://hub.docker.com/r/adoptopenjdk/openjdk8
# https://docs.docker.com/develop/develop-images/multistage-build/#use-multi-stage-builds
FROM adoptopenjdk/openjdk11:alpine-slim
#Copy the .JAR file(s) from catalog to our container
COPY --from=builder /app/target/*.jar /app/application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]

