package net.pladema.clinichistory.hospitalization.infrastructure.input.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ar.lamansys.sgh.clinichistory.application.document.draft.CreateDocumentWithDraftSupport;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.GetAnestheticReport;
import net.pladema.clinichistory.hospitalization.domain.AnestheticReportBo;
import net.pladema.clinichistory.hospitalization.infrastructure.input.rest.dto.AnestheticReportDto;
import net.pladema.clinichistory.hospitalization.infrastructure.input.rest.mapper.AnestheticReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Anesthetic Report", description = "Anesthetic Report")
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/anesthetic-report")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Validated
@Slf4j
@RestController
public class AnestheticReportController {

    private final AnestheticReportMapper anestheticReportMapper;
    @Qualifier("anesthetic_report")
    private final CreateDocumentWithDraftSupport createDocumentWithDraftSupport;
    private final GetAnestheticReport getAnestheticReport;

    @PostMapping("/close")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA')")
    public ResponseEntity<Integer> close(@PathVariable(name = "institutionId") Integer institutionId,
                                         @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
                                         @Valid @RequestBody AnestheticReportDto anestheticReportDto) {
        log.trace("Input parameters -> institutionId {}, internmentEpisodeId {}, anestheticReport {}", institutionId, internmentEpisodeId, anestheticReportDto);

        AnestheticReportBo anestheticReport = anestheticReportMapper.fromAnestheticReportDto(anestheticReportDto);
        anestheticReport.setInstitutionId(institutionId);
        anestheticReport.setEncounterId(internmentEpisodeId);
        anestheticReport.setConfirmed(true);
        Integer anestheticReportId = createDocumentWithDraftSupport.run(anestheticReport);

        log.trace("Output -> {}", anestheticReportId);
        return ResponseEntity.ok().body(anestheticReportId);
    }

    @PostMapping("/draft")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA')")
    public ResponseEntity<Integer> createDraft(@PathVariable(name = "institutionId") Integer institutionId,
                                               @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
                                               @Valid @RequestBody AnestheticReportDto anestheticReportDto) {
        log.trace("Input parameters -> institutionId {}, internmentEpisodeId {}, anestheticReport {}", institutionId, internmentEpisodeId, anestheticReportDto);

        AnestheticReportBo anestheticReport = anestheticReportMapper.fromAnestheticReportDto(anestheticReportDto);
        anestheticReport.setInstitutionId(institutionId);
        anestheticReport.setEncounterId(internmentEpisodeId);
        anestheticReport.setConfirmed(false);
        Integer anestheticReportId = createDocumentWithDraftSupport.run(anestheticReport);

        log.trace("Output -> {}", anestheticReportId);
        return ResponseEntity.ok().body(anestheticReportId);
    }

    @GetMapping("/{documentId}")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA,ENFERMERO, PROFESIONAL_DE_SALUD')")
    public ResponseEntity<AnestheticReportDto> getById(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "documentId") Long documentId) {
        log.trace("Input parameters -> institutionId {}, internmentEpisodeId {}, documentId {}",
                institutionId, internmentEpisodeId, documentId);
        AnestheticReportBo anestheticReport = getAnestheticReport.run(documentId);
        AnestheticReportDto result = anestheticReportMapper.fromAnestheticReportBo(anestheticReport);
        log.trace("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }
}
