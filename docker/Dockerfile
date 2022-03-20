FROM openjdk:17-alpine3.14
ARG JAR_FILE=build/libs/*-final.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-DJAVA_OPTS='-Xms128m -Xmx2g'","-jar","/app.jar"]
