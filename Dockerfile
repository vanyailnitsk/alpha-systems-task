FROM openjdk:17-oracle
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
CMD ["./mvnw","spring-boot:run"]
EXPOSE 8080
#FROM openjdk:17-oracle
#COPY target/*.jar app.jar
#EXPOSE 8081
#ENTRYPOINT ["java","-jar","app.jar"]