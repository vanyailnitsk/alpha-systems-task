FROM openjdk:17-oracle
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
CMD ["./mvnw","spring-boot:run"]
EXPOSE 8080