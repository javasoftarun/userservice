# Use a JDK image to build the application
FROM gradle:8.5-jdk17 AS build
COPY --chown=gradle:gradle . /userservice
WORKDIR /userservice
RUN gradle build --no-daemon

# Use a smaller JRE image to run the application
FROM eclipse-temurin:17-jre
EXPOSE 8083
COPY --from=build /userservice/build/libs/userservice-0.0.1-SNAPSHOT.jar userservice-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","userservice-0.0.1-SNAPSHOT.jar"]
