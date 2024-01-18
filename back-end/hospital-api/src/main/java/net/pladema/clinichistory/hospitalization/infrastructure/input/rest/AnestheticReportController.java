package net.pladema.clinichistory.hospitalization.infrastructure.input.rest;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.CreateAnestheticReport;
import net.pladema.clinichistory.hospitalization.domain.AnestheticReportBo;
import net.pladema.clinichistory.hospitalization.infrastructure.input.rest.dto.AnestheticReportDto;
import net.pladema.clinichistory.hospitalization.infrastructure.input.rest.mapper.AnestheticReportMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/anesthetic-report")
@RequiredArgsConstructor
@Validated
@Slf4j
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA')")
@RestController
public class AnestheticReportController {

    private final AnestheticReportMapper anestheticReportMapper;
    private final CreateAnestheticReport createAnestheticReport;

    @PostMapping
    public ResponseEntity<Integer> create(@PathVariable(name = "institutionId") Integer institutionId,
                                          @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
                                          @Valid @RequestBody AnestheticReportDto anestheticReportDto) {
        log.trace("Input parameters -> institutionId {}, internmentEpisodeId {}, anestheticReport {}", institutionId, internmentEpisodeId, anestheticReportDto);
        AnestheticReportBo anestheticReport = anestheticReportMapper.fromAnestheticReportDto(anestheticReportDto);
        anestheticReport.setInstitutionId(institutionId);
        anestheticReport.setEncounterId(internmentEpisodeId);
        Integer documentId = createAnestheticReport.run(anestheticReport);
        log.trace("Output -> {}", documentId);
        return ResponseEntity.ok().body(documentId);
    }
}
