FROM nginx:1.21.3

COPY nginx-template.conf /nginx-template.conf

WORKDIR /app

COPY apps/dist/hospital/ ./front-end
COPY backoffice/build/ ./backoffice
COPY entrypoint.sh /entrypoint.sh

RUN chmod +x /entrypoint.sh

ENTRYPOINT [ "/entrypoint.sh" ]

EXPOSE 8280

CMD ["nginx", "-g", "daemon off;"]