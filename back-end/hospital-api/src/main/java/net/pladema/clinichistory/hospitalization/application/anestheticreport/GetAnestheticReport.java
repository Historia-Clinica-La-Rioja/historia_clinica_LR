package net.pladema.clinichistory.hospitalization.application.anestheticreport;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.ips.GeneralHealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.ProcedureTypeEnum;
import java.util.List;
import java.util.stream.Collectors;
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

        List<ProcedureBo> procedures = documentService.getProcedureStateFromDocument(documentId).stream()
                .filter(p -> ProcedureTypeEnum.SURGICAL_PROCEDURE.equals(p.getType()))
                .collect(Collectors.toList());
        result.setSurgeryProcedures(procedures);

        result.setAnthropometricData(documentService.getAnthropometricDataStateFromDocument(documentId));
        result.setRiskFactors(documentService.getRiskFactorStateFromDocument(documentId));
        result.setAnestheticHistory(documentService.getAnestheticHistoryStateFromDocument(documentId));
        result.setMedications(documentService.getMedicationStateFromDocument(documentId));
        result.setPreMedications(documentService.getPreMedicationStateFromDocument(documentId));
        result.setFoodIntake(documentService.getFoodIntakeStateFromDocument(documentId));

        result.setHistories(generalHealthConditionBo.getOtherHistories());
        result.setProcedureDescription(documentService.getProcedureDescriptionStateFromDocument(documentId));

        log.debug("Output -> anestheticReport {}", result);
        return result;
    }
}
