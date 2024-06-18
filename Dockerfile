FROM gradle:8.5-jdk17-alpine

COPY ./homebanking .

EXPOSE 8080

RUN /bin/sh -c "gradle build --debug"

ENTRYPOINT ["java", "-jar","homebanking/build/libs/homebanking-0.0.1-SNAPSHOT.jar"]