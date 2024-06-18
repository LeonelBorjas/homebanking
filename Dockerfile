FROM gradle:8.5-jdk17-alpine

WORKDIR /home/gradle/homebanking

COPY ./homebanking .

EXPOSE 8080

RUN gradle build


ENTRYPOINT ["java", "-jar","build/libs/homebanking-0.0.1-SNAPSHOT.jar"]