package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.document;

import ar.lamansys.sgh.clinichistory.application.anestheticreport.CreateAnestheticReportWithEditionSupportConcreteMethod;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.document.dto.PostCloseAnestheticReportDto;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.GetAnestheticReport;
import ar.lamansys.sgh.clinichistory.domain.document.impl.AnestheticReportBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.document.dto.AnestheticReportDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.service.mapper.AnestheticReportMapper;

import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Anesthetic Report", description = "Anesthetic Report")
@RequestMapping("/institutions/{institutionId}/anesthetic-report")
@RequiredArgsConstructor
@Validated
@Slf4j
@RestController
public class AnestheticReportController {

    private final AnestheticReportMapper anestheticReportMapper;
    private final CreateAnestheticReportWithEditionSupportConcreteMethod supportAnestheticReport;
    private final GetAnestheticReport getAnestheticReport;

    @PostMapping("/close")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA')")
    public Integer close(@PathVariable(name = "institutionId") Integer institutionId,
                                         @Valid @RequestBody AnestheticReportDto anestheticReportDto) {
        log.trace("Input parameters -> institutionId {}, anestheticReport {}", institutionId, anestheticReportDto);

        AnestheticReportBo anestheticReport = anestheticReportMapper.fromAnestheticReportDto(anestheticReportDto);
        anestheticReport.setInstitutionId(institutionId);
        anestheticReport.setConfirmed(true);
        Integer anestheticReportId = supportAnestheticReport.run(anestheticReport);

        log.trace("Output -> {}", anestheticReportId);
        return anestheticReportId;
    }

    @PostMapping("/draft")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA')")
    public Integer createDraft(@PathVariable(name = "institutionId") Integer institutionId,
                                               @Valid @RequestBody AnestheticReportDto anestheticReportDto) {
        log.trace("Input parameters -> institutionId {}, anestheticReport {}", institutionId, anestheticReportDto);

        AnestheticReportBo anestheticReport = anestheticReportMapper.fromAnestheticReportDto(anestheticReportDto);
        anestheticReport.setInstitutionId(institutionId);
        anestheticReport.setConfirmed(false);
        Integer anestheticReportId = supportAnestheticReport.run(anestheticReport);

        log.trace("Output -> {}", anestheticReportId);
        return anestheticReportId;
    }

    @GetMapping("/by-document/{documentId}")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PROFESIONAL_DE_SALUD')")
    public AnestheticReportDto getById(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "documentId") Long documentId) {
        log.trace("Input parameters -> institutionId {}, documentId {}",
                institutionId, documentId);
        AnestheticReportBo anestheticReport = getAnestheticReport.run(documentId);
        AnestheticReportDto result = anestheticReportMapper.fromAnestheticReportBo(anestheticReport);
        log.trace("Output -> {}", result);
        return result;
    }

    @PutMapping("/close")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA')")
    public Integer edit(@PathVariable(name = "institutionId") Integer institutionId,
                        @Valid @RequestBody PostCloseAnestheticReportDto anestheticReportDto) {
        log.trace("Input parameters -> institutionId {}, anestheticReport {}", institutionId, anestheticReportDto);

        AnestheticReportBo anestheticReport = anestheticReportMapper.fromPostCloseAnestheticReportDto(anestheticReportDto);
        anestheticReport.setInstitutionId(institutionId);
        anestheticReport.setConfirmed(true);
        Integer anestheticReportId = supportAnestheticReport.run(anestheticReport);

        log.trace("Output -> {}", anestheticReportId);
        return anestheticReportId;
    }
}
