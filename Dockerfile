FROM gradle:4.7.0-jdk8-alpine AS build
ENV APP_HOME=/opt/
COPY  . $APP_HOME
WORKDIR $APP_HOME
RUN gradle build --no-daemon

FROM adoptopenjdk/openjdk11:jdk-11.0.2.9-slim
ENV APP_HOME=/opt
ENV ARTIFACT_NAME=your-application.jar
WORKDIR $APP_HOME
ENV PORT 8080
EXPOSE 8080
COPY --from=builder $APP_HOME/ build/libs/$ARTIFACT_NAME
ENTRYPOINT exec java $JAVA_OPTS -jar $ARTIFACwe T_NAME