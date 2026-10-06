FROM eclipse-temurin:21-jdk AS build 
WORKDIR /app 
COPY . .
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8
RUN ./mvnw clean package -DskipTests


FROM eclipse-temurin:21-jdk AS build 
WORKDIR /app
COPY --from=build /APP/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]