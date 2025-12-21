FROM eclipse-temurin:17.0.5_8-jre-focal AS builder
LABEL authors="paccy"
WORKDIR /extracted
ADD build/libs/Procure-to-Pay-0.0.1.jar precure.jar
RUN java -Djarmode=layertools -jar precure.jar extract

FROM eclipse-temurin:17.0.5_8-jre-focal
WORKDIR /application

COPY --from=builder extracted/dependencies/ ./
COPY --from=builder extracted/spring-boot-loader/ ./
COPY --from=builder extracted/snapshot-dependencies/ ./
COPY --from=builder extracted/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]