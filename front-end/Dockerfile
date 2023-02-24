FROM nginx:1.21.3

LABEL org.opencontainers.image.authors="historia_clinica@pladema.exa.unicen.edu.ar"

COPY nginx-template.conf /nginx-template.conf

WORKDIR /app

COPY .docker/front-end     /app/front-end/
COPY .docker/backoffice    /app/backoffice/

COPY entrypoint.sh /entrypoint.sh

RUN chmod +x /entrypoint.sh

ENTRYPOINT [ "/entrypoint.sh" ]

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
