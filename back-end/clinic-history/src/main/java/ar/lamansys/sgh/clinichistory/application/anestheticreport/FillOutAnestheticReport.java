package ar.lamansys.sgh.clinichistory.application.anestheticreport;

import ar.lamansys.sgh.clinichistory.application.anestheticreport.ports.AnestheticReportStorage;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.document.impl.AnestheticReportBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticSubstanceBo;
import ar.lamansys.sgh.clinichistory.domain.ips.GeneralHealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EAnestheticSubstanceType;
import ar.lamansys.sgh.shared.infrastructure.input.service.ProcedureTypeEnum;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedHospitalizationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FillOutAnestheticReport {

    private final DocumentService documentService;
    private final AnestheticReportStorage anestheticReportStorage;
    private final SharedHospitalizationPort sharedHospitalizationPort;

    public AnestheticReportBo run(AnestheticReportBo document) {
        log.debug("Input parameters -> document {}", document);
        Long documentId = document.getId();

        GeneralHealthConditionBo generalHealthConditionBo = documentService.getHealthConditionFromDocument(documentId);
        document.setMainDiagnosis(generalHealthConditionBo.getMainDiagnosis());
        document.setDiagnosis(generalHealthConditionBo.getDiagnosis());

        List<ProcedureBo> procedures = documentService.getProcedureStateFromDocument(documentId).stream()
                .filter(p -> ProcedureTypeEnum.SURGICAL_PROCEDURE.equals(p.getType()))
                .collect(Collectors.toList());
        document.setSurgeryProcedures(procedures);

        document.setAnthropometricData(documentService.getAnthropometricDataStateFromDocument(documentId));

        document.setRiskFactors(documentService.getRiskFactorStateFromDocument(documentId));

        document.setAnestheticHistory(documentService.getAnestheticHistoryStateFromDocument(documentId));

        document.setMedications(documentService.getMedicationStateFromDocument(documentId));

        List<AnestheticSubstanceBo> substances = documentService.getAnestheticSubstancesStateFromDocument(documentId);

        document.setPreMedications(this.filterSubstanceBy(substances, EAnestheticSubstanceType.PRE_MEDICATION));

        document.setHistories(generalHealthConditionBo.getOtherHistories());

        document.setProcedureDescription(documentService.getProcedureDescriptionStateFromDocument(documentId));

        document.setAnestheticPlans(this.filterSubstanceBy(substances, EAnestheticSubstanceType.ANESTHETIC_PLAN));

        document.setAnalgesicTechniques(documentService.getAnalgesicTechniquesStateFromDocument(documentId));

        document.setAnestheticTechniques(documentService.getAnestheticTechniquesStateFromDocument(documentId));

        document.setFluidAdministrations(this.filterSubstanceBy(substances, EAnestheticSubstanceType.FLUID_ADMINISTRATION));

        document.setAnestheticAgents(this.filterSubstanceBy(substances, EAnestheticSubstanceType.ANESTHETIC_AGENT));

        document.setNonAnestheticDrugs(this.filterSubstanceBy(substances, EAnestheticSubstanceType.NON_ANESTHETIC_DRUG));

        document.setAntibioticProphylaxis(this.filterSubstanceBy(substances, EAnestheticSubstanceType.ANTIBIOTIC_PROPHYLAXIS));

        document.setMeasuringPoints(documentService.getMeasuringPointStateFromDocument(documentId));

        document.setPostAnesthesiaStatus(documentService.getPostAnesthesiaStatusStateFromDocument(documentId));

        this.setAnestheticChart(document);

        this.setPatientMedicalCoverageId(document);

        log.debug("Output -> anestheticReport {}", document);
        return document;
    }
    
    private List<AnestheticSubstanceBo> filterSubstanceBy(List<AnestheticSubstanceBo> substances, EAnestheticSubstanceType type) {
        return substances.stream()
                .filter(substance -> substance.isOfType(type))
                .collect(Collectors.toList());
    }

    private void setAnestheticChart(AnestheticReportBo document) {
        if (document.getAnestheticChart() == null && document.isConfirmed()) {
            String chart = anestheticReportStorage.getAntestheticChart(document.getId());
            document.setAnestheticChart(chart);
        }
    }

    private void setPatientMedicalCoverageId(AnestheticReportBo document) {
        sharedHospitalizationPort.getPatientMedicalCoverageId(document.getEncounterId())
                .ifPresent(document::setPatientMedicalCoverageId);
    }
}
