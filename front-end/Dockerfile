FROM nginx:1.21.3

LABEL org.opencontainers.image.authors="desarrollo@lamansys.com.ar"

COPY nginx-template.conf /nginx-template.conf

WORKDIR /app

COPY apps/dist/hospital/ /app/front-end/
COPY backoffice/build/   /app/backoffice/

COPY entrypoint.sh /entrypoint.sh

RUN chmod +x /entrypoint.sh

ENTRYPOINT [ "/entrypoint.sh" ]

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
