import csv
import json
import requests
import pandas as pd
import os

if 'SNOWSTORM_SERVICE' not in os.environ:
    # Si no existe, lanzamos una excepción con un mensaje descriptivo
    print(f"La variable de entorno SNOWSTORM_SERVICE no está definida.")
    exit(1)

snowstorm_base_url = os.environ.get('SNOWSTORM_SERVICE')

auth_headers_required = False

if 'API_ID' in os.environ:
    auth_headers_required = True
    app_id = os.environ.get('API_ID')
    app_key = os.environ.get('API_KEY')
    print("Usando credenciales para:", snowstorm_base_url)

snomed_synonyms_search = False

if 'DESCRIPTION_FULL_FILE' in os.environ:
    snomed_synonyms_search = True
    snomed_file_path = os.environ.get('DESCRIPTION_FULL_FILE')
    print("Se incluyen sinonimos usando:", snomed_file_path)

def write_concepts_synonyms_to_file(sf, data, items):
    for concept in items:
        conceptId = concept.get('conceptId')
        sf.write(data.query("conceptId == " + conceptId)
                 .drop_duplicates("term", keep=False)
                 .to_csv(index=False, columns=["conceptId", "term"], header=False, quotechar='"',
                         quoting=csv.QUOTE_NONNUMERIC))


def write_concepts_to_file(file, items):
    for concept in items:
        conceptId = concept.get('conceptId')
        term = concept.get('pt').get('term').replace('\"', '\'')
        term = '\"' + term + '\"'
        line = conceptId + ',' + term + '\n'
        file.write(line)


def do_request(ecl, searchAfter=None):
    url = snowstorm_base_url + '/MAIN/concepts'
    url += '?activeFilter=true'
    url += '&termActive=true'
    url += '&offset=0'
    url += '&limit=2000'
    url += '&ecl=' + ecl
    url += ("&searchAfter=" + searchAfter) if searchAfter else ''
    headers = {"Accept": "application/json", "Accept-Language": "es", "app_id": app_id,
               "app_key": app_key} if auth_headers_required else {"Accept": "application/json", "Accept-Language": "es"}
    response = requests.get(url=url, headers=headers)
    # Imprimir la respuesta
    # print("Respuesta de la solicitud:")
    # print(response.text)
    # print("URL de la solicitud:", url)
    return response.json()


def generate_csv_file(groupName, ecl, data):
    global sf
    print('Generating csv file for ' + groupName)
    response = do_request(ecl)
    items = response.get('items')
    searchAfter = response.get('searchAfter')
    f = open(groupName + '.csv', "w", encoding="utf-8")
    f.write('conceptId,term' + '\n')
    if snomed_synonyms_search:
        sf = open(groupName + '_SYNONYMS.csv', "w", encoding="utf-8")
        sf.write('conceptId,term' + '\n')
    while (items):
        write_concepts_to_file(f, items)
        if snomed_synonyms_search:
            write_concepts_synonyms_to_file(sf, data, items)
        response = do_request(ecl, searchAfter)
        items = response.get('items')
        searchAfter = response.get('searchAfter')

    f.close()


f = open('/exporter/ecls.json')
groupsData = json.load(f)
data = pd.DataFrame
if snomed_synonyms_search:
    names = ["id", "effectiveTime", "active", "moduleId", "conceptId", "languageCode", "typeId", "term",
             "caseSignificanceId"]
    usecols = ["conceptId", "id", "active", "languageCode", "typeId", "term"]
    snomedFile = pd.read_csv(snomed_file_path, sep='\t', skiprows=1, names=names, usecols=usecols)
    data = snomedFile.query("active == 1 and languageCode == 'es' and typeId == 900000000000013009")

for group in groupsData:
    name = group.get('key')
    ecl = group.get('value')
    generate_csv_file(name, ecl, data)

f.close()