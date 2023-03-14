# ![logo](front-end/apps/projects/hospital/src/assets/custom/icons/icon-72x72.png) Eventos generados en el sistema

Este documento detalla los eventos que el sistema HSI reporta. Todos los eventos generados envían en el mensaje el id del paciente asociado. En caso de no ser así se detalla en el propio evento.

### Ejemplo de formato estandar de mensaje

```json

{"description":"{\"patientId\":1,\"topic\":\"HSI/INSTITUTION/1/ODONTOLOGY/NUEVA_CONSULTA\"}"}
```

### Odontologia


| Evento                                                    | Descripción                                                                         | Desde   |
|-----------------------------------------------------------|-------------------------------------------------------------------------------------|---------|
| HSI/INSTITUTION/{institutionId}/ODONTOLOGY/NUEVA_CONSULTA | Indica que un paciente termino una consulta odontológica en una institución específica  | v1.46.0 |


## Internación

| Evento                                                                                        | Descripción                                                                                                | Desde   |
|-----------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------|---------|
| HSI/INSTITUTION/{institutionId}/HOSPITAL_API/CLINIC_HISTORY/HOSPITALIZATION/DISCHARGE/MEDICAL | Indica que un paciente  tuvo el alta médica en una institución específica                                  | v1.46.0 |
| HSI/INSTITUTION/{institutionId}/HOSPITAL_API/CLINIC_HISTORY/HOSPITALIZATION/DISCHARGE/PHYSIC  | Indica que un paciente  tuvo el alta física en una institución específica                                  | v1.46.0 |
| HSI/INSTITUTION/{institutionId}/HOSPITAL_API/CLINIC_HISTORY/HOSPITALIZATION/SERVICE_RESQUEST  | Indica que un paciente se emitió una orden para un estudio médico en una institución específica            | v1.46.0 |
| HSI/INSTITUTION/{institutionId}/HOSPITAL_API/CLINIC_HISTORY/HOSPITALIZATION/SERVICE_RESQUEST/IMAGE  | Indica que un paciente  se emitió una orden para una estudio de imagen médica en una institución específica | v1.46.0 |
| HSI/INSTITUTION/{institutionId}/HOSPITAL_API/CLINIC_HISTORY/HOSPITALIZATION/SERVICE_RESQUEST/LABORATORY  | Indica que un paciente se emitió una orden para un estudio de laboratorio en una institución específica    | v1.46.0 |

