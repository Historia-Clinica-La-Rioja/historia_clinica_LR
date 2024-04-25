package net.pladema.clinichistory.hospitalization.application.anestheticreport;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticSubstanceBo;
import ar.lamansys.sgh.clinichistory.domain.ips.GeneralHealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EAnestheticSubstanceType;
import ar.lamansys.sgh.shared.infrastructure.input.service.ProcedureTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.anestheticreport.exceptions.AnestheticReportException;
import net.pladema.clinichistory.hospitalization.application.port.AnestheticStorage;
import net.pladema.clinichistory.hospitalization.domain.AnestheticReportBo;
import net.pladema.clinichistory.hospitalization.domain.exceptions.AnestheticReportEnumException;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetAnestheticReport {

    private final AnestheticStorage anestheticStorage;
    private final DocumentService documentService;

    public AnestheticReportBo run(Long documentId) {
        log.debug("Input parameters -> documentId {}", documentId);

        AnestheticReportBo result = anestheticStorage.get(documentId)
                .orElseThrow(() -> new AnestheticReportException(
                        AnestheticReportEnumException.ANESTHETIC_REPORT_NOT_FOUND,
                        "anesthetic-report.not-found"));

       this.completeValuesAnestheticReportFromDocument(documentId, result);

        log.debug("Output -> anestheticReport {}", result);
        return result;
    }

    private void completeValuesAnestheticReportFromDocument(Long documentId, AnestheticReportBo result) {
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

        List<AnestheticSubstanceBo> substances = documentService.getAnestheticSubstancesStateFromDocument(documentId);

        result.setPreMedications(this.filterSubstanceBy(substances, EAnestheticSubstanceType.PRE_MEDICATION));

        result.setHistories(generalHealthConditionBo.getOtherHistories());
        result.setProcedureDescription(documentService.getProcedureDescriptionStateFromDocument(documentId));

        result.setAnestheticPlans(this.filterSubstanceBy(substances, EAnestheticSubstanceType.ANESTHETIC_PLAN));

        result.setAnalgesicTechniques(documentService.getAnalgesicTechniquesStateFromDocument(documentId));

        result.setAnestheticTechniques(documentService.getAnestheticTechniquesStateFromDocument(documentId));

        result.setFluidAdministrations(this.filterSubstanceBy(substances, EAnestheticSubstanceType.FLUID_ADMINISTRATION));

        result.setAnestheticAgents(this.filterSubstanceBy(substances, EAnestheticSubstanceType.ANESTHETIC_AGENT));

        result.setNonAnestheticDrugs(this.filterSubstanceBy(substances, EAnestheticSubstanceType.NON_ANESTHETIC_DRUG));

        result.setAntibioticProphylaxis(this.filterSubstanceBy(substances, EAnestheticSubstanceType.ANTIBIOTIC_PROPHYLAXIS));

        result.setMeasuringPoints(documentService.getMeasuringPointStateFromDocument(documentId));

        result.setPostAnesthesiaStatus(documentService.getPostAnesthesiaStatusStateFromDocument(documentId));
    }

    private List<AnestheticSubstanceBo> filterSubstanceBy(List<AnestheticSubstanceBo> substances, EAnestheticSubstanceType type) {
        return substances.stream()
                .filter(substance -> substance.isOfType(type))
                .collect(Collectors.toList());
    }
}
