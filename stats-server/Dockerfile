FROM eclipse-temurin:21-jre-jammy
VOLUME /tmp
ARG JAR_FILE=stats-service-server/target/stats-service-server-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} stats-server.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /stats-server.jar"]