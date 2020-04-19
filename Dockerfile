#STAGE1 multistage 2stagebuild
FROM maven:3.6.3-jdk-11 as backend-build
WORKDIR /fullstack/backend
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src src
RUN mvn install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

#STAGE2
FROM openjdk:11-jdk-slim
VOLUME /tmp
ARG DEPENDENCY=/fullstack/backend/target/dependency
COPY --from=backend-build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=backend-build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=backend-build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*", \
"ru.serj.currencyconversionservice.CurrencyConversionServiceApplication"]