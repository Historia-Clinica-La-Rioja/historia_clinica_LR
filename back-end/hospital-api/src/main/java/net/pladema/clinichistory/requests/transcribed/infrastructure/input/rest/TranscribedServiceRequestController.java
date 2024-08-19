package net.pladema.clinichistory.requests.transcribed.infrastructure.input.rest;

import ar.lamansys.sgh.clinichistory.application.fetchorderimagefile.FetchOrderImageFileById;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.StudyTranscribedOrderReportInfoBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgx.shared.files.pdf.PDFDocumentException;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.controller.dto.TranscribedServiceRequestDto;
import net.pladema.clinichistory.requests.servicerequests.controller.mapper.StudyMapper;
import net.pladema.clinichistory.requests.transcribed.application.delete.DeleteTranscribedServiceRequest;
import net.pladema.clinichistory.requests.transcribed.application.generatepdf.CreateTranscribedServiceRequestPdf;
import net.pladema.clinichistory.requests.transcribed.application.getlistsummary.GetListTranscribedServiceRequestSummary;
import net.pladema.clinichistory.requests.transcribed.application.getliststudytranscribedservicerequest.GetListStudyTranscribedServiceRequest;
import net.pladema.clinichistory.requests.transcribed.application.update.CreateAndReplaceTranscribedOrder;
import net.pladema.clinichistory.requests.transcribed.application.uploadfile.UploadTranscribedOrderFile;
import net.pladema.clinichistory.requests.transcribed.domain.TranscribedServiceRequestBo;
import net.pladema.clinichistory.requests.transcribed.infrastructure.input.rest.dto.StudyTranscribedOrderReportInfoDto;
import net.pladema.clinichistory.requests.transcribed.infrastructure.input.rest.dto.TranscribedServiceRequestSummaryDto;
import net.pladema.clinichistory.requests.transcribed.infrastructure.input.rest.mapper.TranscribedDiagnosticReportInfoMapper;
import net.pladema.patient.controller.service.PatientExternalService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "Transcribed service request", description = "Transcribed service request")
@RequestMapping("/institutions/{institutionId}/patient/{patientId}/service-requests")
@Slf4j
@RequiredArgsConstructor
@RestController
public class TranscribedServiceRequestController {

    private static final String OUTPUT = "create result -> {}";

    private final PatientExternalService patientExternalService;
    private final StudyMapper studyMapper;
    private final UploadTranscribedOrderFile uploadTranscribedOrderFile;
    private final TranscribedDiagnosticReportInfoMapper transcribedDiagnosticReportInfoMapper;
    private final DeleteTranscribedServiceRequest deleteTranscribedServiceRequest;
    private final FetchOrderImageFileById fetchOrderImageFileById;
    private final CreateTranscribedServiceRequestPdf createTranscribedServiceRequestPdf;
    private final CreateAndReplaceTranscribedOrder createAndReplaceTranscribedOrder;
    private final GetListStudyTranscribedServiceRequest getListStudyTranscribedServiceRequest;
    private final GetListTranscribedServiceRequestSummary getListStudyTranscribedOrderReports;

    @PostMapping("/transcribed")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ADMINISTRATIVO_RED_DE_IMAGENES')")
    public Integer createTranscribed(@PathVariable(name = "institutionId") Integer institutionId,
                                     @PathVariable(name = "patientId") Integer patientId,
                                     @Valid @RequestBody TranscribedServiceRequestDto transcribedServiceRequest) {
        log.debug("Input parameters -> institutionId {}, patientId {}, transcribedServiceRequest {}", institutionId, patientId, transcribedServiceRequest);
        TranscribedServiceRequestBo transcribedServiceRequestBo = studyMapper.toTranscribedServiceRequestBo(transcribedServiceRequest);

        BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        PatientInfoBo patientInfoBo = new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge());
        transcribedServiceRequestBo.setPatientInfo(patientInfoBo);
        Integer serviceRequestId = createAndReplaceTranscribedOrder.run(transcribedServiceRequestBo, Optional.ofNullable(transcribedServiceRequest.getOldTranscribedOrderId()));

        log.debug(OUTPUT, serviceRequestId);
        return serviceRequestId;
    }

    @DeleteMapping("/{orderId}/delete-transcribed")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ADMINISTRATIVO_RED_DE_IMAGENES')")
    public void deleteTranscribedOrder(@PathVariable(name = "institutionId") Integer institutionId,
                                       @PathVariable(name = "patientId") Integer patientId,
                                       @PathVariable(name = "orderId") Integer orderId) {
        log.debug("Input parameters -> institutionId {}, patientId {}, orderId {}", institutionId, patientId, orderId);
        deleteTranscribedServiceRequest.run(orderId);
        log.debug(OUTPUT, orderId);
    }

    @PostMapping(value = "/{orderId}/uploadFiles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ADMINISTRATIVO_RED_DE_IMAGENES')")
    public List<Integer> uploadFiles(@PathVariable(name = "institutionId") Integer institutionId,
                                     @PathVariable(name = "patientId") Integer patientId,
                                     @PathVariable(name = "orderId") Integer orderId,
                                     @RequestPart("files") MultipartFile[] files) {
        log.debug("Input parameters -> institutionId {}, patientId {}, orderId {}", institutionId, patientId, orderId);
        var result = uploadTranscribedOrderFile.execute(files, orderId, patientId);
        log.debug(OUTPUT, result);
        return result;
    }

    @GetMapping(value = "/{id}/downloadTranscribedFile")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PRESCRIPTOR, TECNICO, INFORMADOR')")
    public ResponseEntity<Resource> downloadImg(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "id") Integer id) {
        var storedFileBo = fetchOrderImageFileById.run(id);

        return StoredFileResponse.sendFile(storedFileBo);
    }

    @GetMapping("/studyTranscribedOrder")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO, ADMINISTRATIVO_RED_DE_IMAGENES')")
    public List<StudyTranscribedOrderReportInfoDto> getListStudyTranscribedOrder(@PathVariable(name = "institutionId") Integer institutionId,
                                                                                 @PathVariable(name = "patientId") Integer patientId) {
        log.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);

        List<StudyTranscribedOrderReportInfoBo> resultService = getListStudyTranscribedServiceRequest.run(patientId);

        List<StudyTranscribedOrderReportInfoDto> result = resultService.stream()
                .map(transcribedDiagnosticReportInfoMapper::parseToDto)
                .collect(Collectors.toList());

        log.trace(OUTPUT, result);
        return result;
    }

    @GetMapping("/transcribedOrders")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO, ADMINISTRATIVO_RED_DE_IMAGENES, ADMINISTRADOR_AGENDA')")
    public List<TranscribedServiceRequestSummaryDto> getList(@PathVariable(name = "institutionId") Integer institutionId,
                                                             @PathVariable(name = "patientId") Integer patientId,
                                                             @RequestParam(value = "orderId", required = false) String orderId) {
        log.debug("Input parameters -> institutionId {}, patientId {}, orderId {}", institutionId, patientId, orderId);

        List<TranscribedServiceRequestBo> resultService = getListStudyTranscribedOrderReports.run(patientId);

        List<TranscribedServiceRequestSummaryDto> result = resultService.stream()
                .map(studyMapper::toTranscribedServiceRequestSummaryDto)
                .collect(Collectors.toList());

        log.trace(OUTPUT, result);
        return result;
    }

    @GetMapping(value = "/transcribed/{transcribedServiceRequestId}/download-pdf")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, TECNICO, ADMINISTRATIVO_RED_DE_IMAGENES')")
    public ResponseEntity<Resource> downloadTranscribedOrderPdf(@PathVariable(name = "institutionId") Integer institutionId,
                                                                @PathVariable(name = "patientId") Integer patientId,
                                                                @PathVariable(name = "transcribedServiceRequestId") Integer transcribedServiceRequestId,
                                                                @RequestParam(name = "appointmentId") String appointmentId) throws PDFDocumentException {
        log.trace("Input parameters -> institutionId {}, patientId {}, transcribedServiceRequestId {}, appointmentId {}", institutionId, patientId, transcribedServiceRequestId, appointmentId);
        var result = createTranscribedServiceRequestPdf.run(institutionId, patientId, transcribedServiceRequestId, Integer.valueOf(appointmentId));
        log.trace(OUTPUT, result);

        return StoredFileResponse.sendGeneratedBlob(//ServiceRequestService.downloadTranscribedOrderPdf
                result
        );
    }

}
