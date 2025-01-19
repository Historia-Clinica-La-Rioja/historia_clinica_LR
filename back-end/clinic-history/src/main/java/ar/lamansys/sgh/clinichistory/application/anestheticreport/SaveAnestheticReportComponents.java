package ar.lamansys.sgh.clinichistory.application.anestheticreport;

import ar.lamansys.sgh.clinichistory.domain.document.impl.AnestheticReportBo;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.ClinicalObservationService;
import ar.lamansys.sgh.clinichistory.domain.ips.services.HealthConditionService;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadAnalgesicTechniques;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadAnestheticHistory;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadAnestheticSubstances;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadAnestheticTechniques;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadMeasuringPoints;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadMedications;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadPostAnesthesiaStatus;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadProcedureDescription;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadProcedures;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class SaveAnestheticReportComponents {

    private final HealthConditionService healthConditionService;
    private final LoadMedications loadMedications;
    private final ClinicalObservationService clinicalObservationService;
    private final LoadProcedures loadProcedures;
    private final LoadAnestheticHistory loadAnestheticHistory;
    private final LoadAnestheticSubstances loadAnestheticSubstances;
    private final LoadProcedureDescription loadProcedureDescription;
    private final LoadAnalgesicTechniques loadAnalgesicTechniques;
    private final LoadAnestheticTechniques loadAnestheticTechniques;
    private final LoadMeasuringPoints loadMeasuringPoints;
    private final LoadPostAnesthesiaStatus loadPostAnesthesiaStatus;

    public void run(AnestheticReportBo documentBo) {
        PatientInfoBo patientInfo = documentBo.getPatientInfo();
        Integer patientId = documentBo.getPatientId();

        healthConditionService.loadMainDiagnosis(patientInfo, documentBo.getId(), Optional.ofNullable(documentBo.getMainDiagnosis()));
        healthConditionService.loadDiagnosis(patientInfo, documentBo.getId(), documentBo.getDiagnosis());
        healthConditionService.loadOtherHistories(patientInfo, documentBo.getId(), documentBo.getHistories());

        loadMedications.run(patientId, documentBo.getId(), documentBo.getMedications());
        loadProcedures.run(patientId,documentBo.getId(), documentBo.getSurgeryProcedures());

        clinicalObservationService.loadRiskFactors(patientId, documentBo.getId(), Optional.ofNullable(documentBo.getRiskFactors()));
        clinicalObservationService.loadAnthropometricData(patientId, documentBo.getId(), Optional.ofNullable(documentBo.getAnthropometricData()));

        loadAnestheticHistory.run(documentBo.getId(), Optional.ofNullable(documentBo.getAnestheticHistory()));
        loadAnestheticSubstances.run(documentBo.getId(), documentBo.getPreMedications());
        loadAnestheticSubstances.run(documentBo.getId(), documentBo.getAnestheticPlans());
        loadProcedureDescription.run(documentBo.getId(), Optional.ofNullable(documentBo.getProcedureDescription()));
        loadAnalgesicTechniques.run(documentBo.getId(), documentBo.getAnalgesicTechniques());
        loadAnestheticTechniques.run(documentBo.getId(), documentBo.getAnestheticTechniques());
        loadAnestheticSubstances.run(documentBo.getId(), documentBo.getFluidAdministrations());
        loadAnestheticSubstances.run(documentBo.getId(), documentBo.getAnestheticAgents());
        loadAnestheticSubstances.run(documentBo.getId(), documentBo.getNonAnestheticDrugs());
        loadAnestheticSubstances.run(documentBo.getId(), documentBo.getAntibioticProphylaxis());
        loadMeasuringPoints.run(documentBo.getId(), documentBo.getMeasuringPoints());
        loadPostAnesthesiaStatus.run(documentBo.getId(), Optional.ofNullable(documentBo.getPostAnesthesiaStatus()));
    }

}
