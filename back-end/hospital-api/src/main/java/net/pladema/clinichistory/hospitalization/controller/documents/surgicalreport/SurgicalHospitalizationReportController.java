package net.pladema.clinichistory.hospitalization.controller.documents.surgicalreport;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.setpatientfrominternmenteposiode.SetPatientFromInternmentEpisode;
import net.pladema.clinichistory.hospitalization.controller.constraints.DocumentValid;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentValid;
import net.pladema.clinichistory.hospitalization.controller.documents.surgicalreport.dto.SurgicalReportDto;
import net.pladema.clinichistory.hospitalization.controller.documents.surgicalreport.mapper.SurgicalReportMapper;
import net.pladema.clinichistory.hospitalization.service.domain.SurgicalReportBo;
import net.pladema.clinichistory.hospitalization.service.surgicalreport.CreateSurgicalReport;
import net.pladema.clinichistory.hospitalization.service.surgicalreport.DeleteSurgicalReport;
import net.pladema.clinichistory.hospitalization.service.surgicalreport.GetSurgicalReport;
import net.pladema.clinichistory.hospitalization.service.surgicalreport.UpdateSurgicalReport;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/surgical-report")
@Slf4j
@Validated
@RequiredArgsConstructor
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
@RestController
public class SurgicalHospitalizationReportController {

    public static final String OUTPUT = "Output -> {}";

    private final SurgicalReportMapper surgicalReportMapper;

    private final CreateSurgicalReport createSurgicalReport;

    private final GetSurgicalReport getSurgicalReport;

    private final DateTimeProvider dateTimeProvider;

    private final DeleteSurgicalReport deleteSurgicalReport;

    private final UpdateSurgicalReport updateSurgicalReport;

    private final SetPatientFromInternmentEpisode setPatientFromInternmentEpisode;

    @PostMapping
    public ResponseEntity<Boolean> create(@PathVariable(name = "institutionId") Integer institutionId,
                                          @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
                                          @RequestBody SurgicalReportDto surgicalReportDto) {
        log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, surgicalReport {}", institutionId, internmentEpisodeId, surgicalReportDto);
        SurgicalReportBo surgicalReport = surgicalReportMapper.fromSurgicalReportDto(surgicalReportDto);
        this.setEncounterInformation(internmentEpisodeId, institutionId, surgicalReport);
        createSurgicalReport.run(surgicalReport);
        log.debug(OUTPUT, Boolean.TRUE);
        return ResponseEntity.ok().body(Boolean.TRUE);
    }

    @GetMapping("/{documentId}")
    @InternmentValid
    @DocumentValid(isConfirmed = true, documentType = DocumentType.SURGICAL_HOSPITALIZATION_REPORT)
    public ResponseEntity<SurgicalReportDto> getById(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "documentId") Long documentId) {
        log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, documentId {}",
                institutionId, internmentEpisodeId, documentId);
        SurgicalReportBo surgicalReport = getSurgicalReport.run(documentId);
        SurgicalReportDto result = surgicalReportMapper.fromSurgicalReportBo(surgicalReport);
        log.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Boolean> delete(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "documentId") Long documentId,
            @RequestBody String reason) {
        log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, documentId {}, reason {}",
                institutionId, internmentEpisodeId, documentId, reason);
        deleteSurgicalReport.run(internmentEpisodeId, documentId, reason);
        log.debug(OUTPUT, Boolean.TRUE);
        return ResponseEntity.ok().body(Boolean.TRUE);
    }

    @PutMapping("/{documentId}")
    public ResponseEntity<Long> update(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "documentId") Long documentId,
            @RequestBody SurgicalReportDto surgicalReportDto) {
        log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, documentId {}, surgicalReportDto {}",
                institutionId, internmentEpisodeId, documentId, surgicalReportDto);
        SurgicalReportBo newReport = surgicalReportMapper.fromSurgicalReportDto(surgicalReportDto);
        newReport.setInstitutionId(institutionId);
        newReport.setEncounterId(internmentEpisodeId);
        setPatientFromInternmentEpisode.run(newReport);
        Long result = updateSurgicalReport.execute(internmentEpisodeId, documentId, newReport);
        log.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    private void setEncounterInformation(Integer encounterId, Integer institutionId, SurgicalReportBo surgicalReport) {
        surgicalReport.setEncounterId(encounterId);
        surgicalReport.setInstitutionId(institutionId);
        LocalDateTime now = dateTimeProvider.nowDateTime();
        surgicalReport.setPerformedDate(now);
        setPatientFromInternmentEpisode.run(surgicalReport);
    }
}