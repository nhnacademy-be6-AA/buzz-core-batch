FROM eclipse-temurin:21-jre

WORKDIR /app

COPY /target/core-batch-0.0.1-SNAPSHOT.jar /app/core-batch.jar

ENTRYPOINT ["java", "-jar", "/app/core-batch.jar", "--spring.profiles.active=prod"]
