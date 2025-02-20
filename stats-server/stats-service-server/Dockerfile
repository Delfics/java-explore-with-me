FROM eclipse-temurin:21-jre-jammy
#VOLUME /tmp
ARG JAR_FILE=target/*.jar
#RUN echo "Checking contents of target directory" && ls -al target/
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sh", "-c", "java -jar /app.jar"]