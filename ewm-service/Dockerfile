FROM eclipse-temurin:21-jre-jammy
VOLUME /tmp
ARG JAR_FILE=ewm/target/ewm-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} ewm-service.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /ewm-service.jar"]