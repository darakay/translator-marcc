FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG DEP=target/dependency
COPY ${DEP}/BOOT-INF/lib /marcc/lib
COPY ${DEP}/META-INF /marcc/META-INF
COPY ${DEP}/BOOT-INF/classes /marcc
ENTRYPOINT ["java", "-cp", "marcc:marcc/lib/*", "com.darakay.marcc.TranslatorMarccApplication"]