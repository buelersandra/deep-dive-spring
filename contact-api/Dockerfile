FROM openjdk:17
ENV TZ=Africa/Accra
EXPOSE 16000
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} contact-0.0.1.jar
ENTRYPOINT ["java","-jar","/contact-0.0.1.jar"]
