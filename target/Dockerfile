FROM openjdk:11

# Install curl and git
RUN apt-get update && apt-get install -y curl git

# Update git to fix CVE-2022-41903
RUN apt-get upgrade -y git

# Ustaw strefę czasową na Europe/Warsaw
ENV TZ=Europe/Warsaw

# Ustaw strefę czasową na Europe/Warsaw
ENV TZ=Europe/Warsaw

COPY *.jar app.jar

ENV SERVER_PORT=6701

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://10.0.50.230:5432/analysis

ENTRYPOINT ["java", "-jar", "/app.jar", "--server.port=${SERVER_PORT}", \
                                        "--spring.datasource.url=${SPRING_DATASOURCE_URL}"]