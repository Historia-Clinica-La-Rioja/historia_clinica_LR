FROM cubejs/cube:v0.26

LABEL org.opencontainers.image.authors="historia_clinica@pladema.exa.unicen.edu.ar"

ENV CUBEJS_DB_TYPE=postgres
ENV CUBEJS_WEB_SOCKETS=true
ENV CUBEJS_DEV_MODE=false
ENV CUBEJS_CACHE_AND_QUEUE_DRIVER=memory
ENV CUBEJS_JWT_ALGS=HS512

COPY schema/ /cube/conf/schema/
COPY cube.js /cube/conf/
