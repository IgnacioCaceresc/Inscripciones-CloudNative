FROM eclipse-temurin:21-jdk-jammy
COPY wallet /app/wallet
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]