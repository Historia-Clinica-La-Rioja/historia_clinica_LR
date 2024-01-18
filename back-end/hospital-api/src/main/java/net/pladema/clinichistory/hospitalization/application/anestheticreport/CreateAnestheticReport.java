package net.pladema.clinichistory.hospitalization.application.anestheticreport;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.port.AnestheticStorage;
import net.pladema.clinichistory.hospitalization.domain.AnestheticReportBo;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.documents.validation.AnestheticReportValidator;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.PatientNotFoundException;
import net.pladema.patient.controller.service.PatientExternalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class CreateAnestheticReport {

    private final PatientExternalService patientExternalService;
    private final InternmentEpisodeService internmentEpisodeService;
    private final AnestheticReportValidator anestheticReportValidator;
    private final DocumentFactory documentFactory;
    private final AnestheticStorage anestheticStorage;

    @Transactional
    public Integer run(AnestheticReportBo anestheticReport) {
        log.debug("Input parameter -> anestheticReport {}", anestheticReport);

        Integer encounterId = anestheticReport.getEncounterId();

        internmentEpisodeService.getPatient(encounterId)
                .map(patientExternalService::getBasicDataFromPatient)
                .map(patientDto -> new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()))
                .ifPresentOrElse(patientInfo -> {
                    anestheticReport.setPatientInfo(patientInfo);
                    anestheticReport.setPatientId(patientInfo.getId());
                }, PatientNotFoundException::new);

        LocalDate entryDate = internmentEpisodeService.getEntryDate(encounterId).toLocalDate();
        anestheticReport.setPatientInternmentAge(entryDate);

        anestheticReport.setPerformedDate(LocalDateTime.now());

        anestheticReportValidator.assertContextValid(anestheticReport);

        documentFactory.run(anestheticReport, false);

        Integer result = anestheticStorage.save(anestheticReport);

        log.debug("Output -> saved anestheticReport id {}", result);
        return result;
    }
}
