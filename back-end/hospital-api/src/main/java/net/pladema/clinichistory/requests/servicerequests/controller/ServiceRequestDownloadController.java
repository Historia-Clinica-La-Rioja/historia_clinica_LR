package net.pladema.clinichistory.requests.servicerequests.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.service.ServeDiagnosticReportFileService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.StoredFileBo;

@Slf4j
@AllArgsConstructor
@Tag(name = "Service request", description = "Service request")
@RestController
@RequestMapping("/institutions/{institutionId}/patient/{patientId}/service-requests")
public class ServiceRequestDownloadController {
    private static final String OUTPUT = "create result -> {}";
    private final ServeDiagnosticReportFileService serveDiagnosticReportFileService;



    @GetMapping("/download/{fileId}")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_IMAGENES, PERSONAL_DE_LABORATORIO')")
    public ResponseEntity download(@PathVariable(name = "institutionId") Integer institutionId,
                                   @PathVariable(name = "patientId") Integer patientId,
                                   @PathVariable(name = "fileId") Integer fileId
    ) {
        log.debug("Input parameters -> institutionId {} patientId {}, fileId {}", institutionId, patientId, fileId);
        StoredFileBo result = serveDiagnosticReportFileService.run(fileId);
		log.debug(OUTPUT, result);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(result.getContentType()))
                .contentLength(result.getContentLenght())
                .body(result.getResource());
    }

}
