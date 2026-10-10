FROM eclipse-temurin:25-jdk
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn -q -DskipTests package
EXPOSE 8080
CMD ["java", "-jar", "target/gridweaver-backend-1.0.0.jar"]
