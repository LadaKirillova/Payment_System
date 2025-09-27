FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY LoadGenerator.java .
RUN javac LoadGenerator.java
CMD ["java", "LoadGenerator"]