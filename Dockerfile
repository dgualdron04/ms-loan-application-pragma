FROM eclipse-temurin:17-jdk AS build
LABEL authors="diego gualdron"

WORKDIR /ms-loan

COPY . .

RUN chmod +x gradlew || true

ARG BOOT_MODULE=":app-service"
RUN ./gradlew clean ${BOOT_MODULE}:bootJar -x test --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /ms-loan

ARG BOOT_JAR_PATH="applications/app-service/build/libs/*.jar"
COPY --from=build /ms-loan/${BOOT_JAR_PATH} app.jar

EXPOSE 8081
ENV JAVA_OPTS=""

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]


