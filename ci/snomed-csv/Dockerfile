FROM python:3.9 

WORKDIR /exporter/csv

ADD main.py /exporter/
ADD ecls.json /exporter/

RUN pip install --upgrade pip
RUN pip install requests pandas

CMD ["python", "/exporter/main.py"]
