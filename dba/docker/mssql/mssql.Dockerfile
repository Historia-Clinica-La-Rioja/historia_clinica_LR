FROM microsoft/mssql-server-linux:latest

ENV PATH="/opt/mssql-tools/bin:${PATH}"

WORKDIR /usr/config

COPY . /usr/config

# Grant permissions
RUN chmod +x /usr/config/entrypoint.sh
RUN chmod +x /usr/config/init.sh

ENTRYPOINT ["./entrypoint.sh"]

# Tail the setup logs to trap the process
CMD ["tail -f /dev/null"]

HEALTHCHECK --interval=15s CMD /opt/mssql-tools/bin/sqlcmd -U sa -P Local123 -Q "select 1" && grep -q "MSSQL CONFIG COMPLETE" ./config.log