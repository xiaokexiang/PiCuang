FROM openjdk:8-jdk-alpine
WORKDIR /opt
ENV PASSWORD=Aa123!@#
COPY target/picuang-0.0.1-SNAPSHOT.jar /opt/picuang.jar
COPY config.ini /opt/config.ini
COPY entrypoint.sh /opt/entrypoint.sh
RUN chmod +x /opt/entrypoint.sh
ENTRYPOINT ["/opt/entrypoint.sh"]


