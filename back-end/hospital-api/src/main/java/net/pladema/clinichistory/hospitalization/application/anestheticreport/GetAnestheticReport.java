package net.pladema.clinichistory.hospitalization.application.anestheticreport;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.ips.GeneralHealthConditionBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.exceptions.AnestheticReportException;
import net.pladema.clinichistory.hospitalization.application.port.AnestheticStorage;
import net.pladema.clinichistory.hospitalization.domain.AnestheticReportBo;
import net.pladema.clinichistory.hospitalization.domain.exceptions.AnestheticReportEnumException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetAnestheticReport {

    private final AnestheticStorage anestheticStorage;
    private final DocumentService documentService;

    public AnestheticReportBo run(Long documentId, Integer internmentEpisodeId) {
        log.debug("Input parameters -> documentId {} internmentEpisodeId {}", documentId, internmentEpisodeId);

        AnestheticReportBo result = anestheticStorage.get(documentId)
                .orElseThrow(() -> new AnestheticReportException(
                        AnestheticReportEnumException.ANESTHETIC_REPORT_NOT_FOUND,
                        "anesthetic-report.not-found"));

        GeneralHealthConditionBo generalHealthConditionBo = documentService.getHealthConditionFromDocument(documentId);
        result.setMainDiagnosis(generalHealthConditionBo.getMainDiagnosis());
        result.setDiagnosis(generalHealthConditionBo.getDiagnosis());

        result.setAnthropometricData(documentService.getAnthropometricDataStateFromDocument(documentId));
        result.setMedications(documentService.getMedicationStateFromDocument(documentId));
        result.setPreMedications(documentService.getPreMedicationStateFromDocument(documentId));
        result.setFoodIntake(documentService.getFoodIntakeStateFromDocument(documentId));

        log.debug("Output -> anestheticReport {}", result);
        return result;
    }
}
