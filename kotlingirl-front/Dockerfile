FROM openjdk:8-jre-alpine
VOLUME /tmp
MAINTAINER Dev
COPY build/libs/kotlingirl-front-1.0-SNAPSHOT.jar /services/front.jar

#EXPOSE will not open port to outer world, just inside cloud
EXPOSE 8060

CMD ["java", "-jar", "/services/front.jar"]
