FROM openjdk:11-jre

LABEL org.opencontainers.image.authors="historia_clinica@pladema.exa.unicen.edu.ar"

# install glowroot
ENV GLOWROOT_VERSION=0.13.6
COPY entrypoint.sh /entrypoint.sh
RUN apt-get update && \
    apt-get -y install wget unzip && \
    cd /  && \
    wget -q https://github.com/glowroot/glowroot/releases/download/v$GLOWROOT_VERSION/glowroot-$GLOWROOT_VERSION-dist.zip && \
    unzip -o glowroot-$GLOWROOT_VERSION-dist.zip && \
    rm glowroot-$GLOWROOT_VERSION-dist.zip && \
    apt-get -y purge --auto-remove curl unzip && \
    rm -r /var/lib/apt/lists/* && \
    echo '{"transactions":{"captureThreadStats":false}}' > /glowroot/config.json && \
    chmod +x /entrypoint.sh

RUN apt-get update && apt-get install -y nginx
COPY nginx.conf /etc/nginx/sites-enabled/default 

WORKDIR /app

COPY app.jar        /app/app.jar
COPY front-end     /app/front-end/
COPY backoffice    /app/backoffice/
COPY storybook     /app/storybook/
COPY docker.properties /app/env.properties

EXPOSE 8280
EXPOSE 8080
ENTRYPOINT [ "/entrypoint.sh" ]

CMD ["java","-XX:+UseSerialGC","-verbose:gc","-javaagent:/glowroot/glowroot.jar","-jar","/app/app.jar","--spring.config.additional-location=file:./env.properties"]   
