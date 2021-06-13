FROM openjdk:11-jre

MAINTAINER Pladema

# install glowroot
ENV GLOWROOT_VERSION=0.13.4
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
COPY  *.jar .  

COPY front-end-dist/hospital/ ./front-end
COPY backoffice-dist/ ./backoffice


COPY  docker.properties ./env.properties  
EXPOSE 8280
EXPOSE 8080
ENTRYPOINT [ "/entrypoint.sh" ]
CMD ["java","-javaagent:/glowroot/glowroot.jar","-jar","./hospital.jar", "--spring.config.additional-location=file:./env.properties"]  
