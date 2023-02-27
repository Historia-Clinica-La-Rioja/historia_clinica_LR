### ![logo](../hospital-api/src/main/resources/assets/webapp/icons/icon-72x72.png) Back-end | Almacenamiento

El paquete [ar.lamansys.sgx.shared.filtestorage](../sgx-shared/src/main/java/ar/lamansys/sgx/shared/filestorage/) del módulo [sgx-shared](../sgx-shared/) contiene la abstracción que permite configurar el almacenamiento final a utilizar, esta abstracción es materializada en la interfaz [BlobStorage](../sgx-shared/src/main/java/ar/lamansys/sgx/shared/filestorage/infrastructure/output/repository/BlobStorage.java). 

Las implementación existente es:

1. NFS [infrastructure.output.repository.nfs.NFSBlobStorage](../sgx-shared/src/main/java/ar/lamansys/sgx/shared/filtestorage/infrastructure/output/repository/nfs/NFSBlobStorage.java)

La interfaz define un funcionamiento similar a un Map donde por cada clave (la ruta del archivo) se obtiene el contenido binario.

## API Rest

Los endpoints que responden archivos son todos aquellos que arman la respuesta usando [infrastructure.input.rest.StoredFileResponse](../sgx-shared/src/main/java/ar/lamansys/sgx/shared/filestorage/infrastructure/input/rest/StoredFileResponse.java).

En el front-end se utilizan dos servicios para procesar las respuestas:
1. [ViewPdfService](../../front-end/apps/projects/hospital/src/app/modules/presentation/dialogs/view-pdf/view-pdf.service.ts): para visualizar los PDF generados por HSI
1. [DownloadService](../../front-end/apps/projects/hospital/src/app/modules/core/services/download.service.ts): para descargar los archivos que se han adjuntado.

En el backoffice se utiliza el botón [DownloadButton](../../front-end/backoffice/src/libs/sgx/components/DownloadButton.js) para descargar los PDFs.

En todos los casos se requiere la `url` del endpoint. Además, aunque el backend defina el `filename`, el frontend debe definir uno por defecto.

## Endpoints de archivos

Todos los endpoints que responden archivos hacen uso de [StoredFileResponse](../sgx-shared/src/main/java/ar/lamansys/sgx/shared/filestorage/infrastructure/input/rest/StoredFileResponse.java) para enviar el archivo en la respuesta. Una división manual permite clasificarlos en dos:

### Archivos adjuntos
* [AssetsController](../hospital-api/src/main/java/net/pladema/assets/controller/AssetsController.java): Logo sponsor, footers.
* [ServiceRequestDownloadController](../hospital-api/src/main/java/net/pladema/clinichistory/requests/servicerequests/controller/ServiceRequestDownloadController.java)
* [InternmentEpisodeDownloadController](../hospital-api/src/main/java/net/pladema/clinichistory/hospitalization/controller/InternmentEpisodeDownloadController.java): Adjunto PDF
* [PersonFileDownloadController](../hospital-api/src/main/java/net/pladema/person/controller/PersonFileDownloadController.java)
* [CounterFileDownloadController](../reference-counter-ref/src/main/java/ar/lamansys/refcounterref/infraestructure/input/rest/CounterFileDownloadController.java)
* [ReferenceFileDownloadController](../reference-counter-ref/src/main/java/ar/lamansys/refcounterref/infraestructure/input/rest/ReferenceFileDownloadController.java)
* [BackofficeFileDownloadController](../sgx-shared/src/main/java/ar/lamansys/sgx/shared/files/infrastructure/input/rest/backoffice/BackofficeFileDownloadController.java)
### Documentos PDF generados
* [MedicationRequestController](../hospital-api/src/main/java/net/pladema/clinichistory/requests/medicationrequests/controller/MedicationRequestController.java): se permite descargar y compartir
* [ServiceRequestController](../hospital-api/src/main/java/net/pladema/clinichistory/requests/servicerequests/controller/ServiceRequestController.java)
* [AppointmentTicketController](../hospital-api/src/main/java/net/pladema/medicalconsultation/appointment/controller/AppointmentTicketController.java)
* [DailyAppointmentController](../hospital-api/src/main/java/net/pladema/medicalconsultation/appointment/controller/DailyAppointmentController.java)
* [ReportsController](../hospital-api/src/main/java/net/pladema/reports/controller/ReportsController.java)
* [DocumentFileDownloadController](../clinic-history/src/main/java/ar/lamansys/sgh/clinichistory/infrastructure/input/rest/document/DocumentFileDownloadController.java)
* [BackofficeDocumentFileDownloadController](../clinic-history/src/main/java/ar/lamansys/sgh/clinichistory/infrastructure/input/rest/backoffice/BackofficeDocumentFileDownloadController.java)



