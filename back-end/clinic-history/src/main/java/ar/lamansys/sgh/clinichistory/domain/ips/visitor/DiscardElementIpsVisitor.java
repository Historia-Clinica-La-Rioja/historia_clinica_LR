package ar.lamansys.sgh.clinichistory.domain.ips.visitor;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticSubstanceBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticTechniqueBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MeasuringPointBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.PostAnesthesiaStatusBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureDescriptionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.ClinicalObservationService;
import ar.lamansys.sgh.clinichistory.domain.ips.services.HealthConditionService;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadMedication;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadProcedure;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionClinicalStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.MedicationStatementStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ObservationStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProceduresStatus;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class DiscardElementIpsVisitor implements IpsVisitor {

    private final HealthConditionService healthConditionService;
    private final ClinicalObservationService clinicalObservationService;
    private final LoadMedication loadMedication;
    private final LoadProcedure loadProcedure;

    private final Long documentId;
    private final Integer patientId;
    private final PatientInfoBo patientInfoBo;

    @Override
    public void visitDiagnosis(DiagnosisBo diagnosisBo) {
        Optional.ofNullable(diagnosisBo)
                .ifPresent(dataBo -> {
                    dataBo.setId(null);
                    dataBo.setStatusId(ConditionClinicalStatus.DRAFT_DISCARDED);
                    dataBo.setVerificationId(ConditionVerificationStatus.DISCARDED);
                    healthConditionService.loadDiagnosis(patientInfoBo, documentId, dataBo);
                });
    }

    @Override
    public void visitHealthCondition(HealthConditionBo healthConditionBo) {
        Optional.ofNullable(healthConditionBo)
                .ifPresent(dataBo -> {
                    dataBo.setId(null);
                    dataBo.setStatusId(ConditionClinicalStatus.DRAFT_DISCARDED);
                    dataBo.setVerificationId(ConditionVerificationStatus.DISCARDED);
                    healthConditionService.loadOtherHistory(patientInfoBo, documentId, dataBo);
                });
    }

    @Override
    public void visitProcedure(ProcedureBo procedureBo) {
        Optional.ofNullable(procedureBo)
                .ifPresent(dataBo -> {
                    dataBo.setId(null);
                    dataBo.setStatusId(ProceduresStatus.DRAFT_DISCARDED);
                    loadProcedure.run(patientId, documentId, dataBo);
                });
    }

    @Override
    public void visitAnthropometricData(AnthropometricDataBo anthropometricDataBo) {
        Optional.ofNullable(anthropometricDataBo)
                .ifPresent(dataBo -> {
                    var previousBloodType = dataBo.getBloodType();
                    if (previousBloodType != null) {
                        previousBloodType.setId(null);
                        previousBloodType.setStatusId(ObservationStatus.DRAFT_DISCARDED);
                    }
                    var previousHeight = dataBo.getHeight();
                    if (previousHeight != null) {
                        previousHeight.setId(null);
                        previousHeight.setStatusId(ObservationStatus.DRAFT_DISCARDED);
                    }
                    var previousWeight = dataBo.getWeight();
                    if (previousWeight != null) {
                        previousWeight.setId(null);
                        previousWeight.setStatusId(ObservationStatus.DRAFT_DISCARDED);
                    }
                    clinicalObservationService.loadAnthropometricData(this.patientId, this.documentId, Optional.of(anthropometricDataBo));
                });
    }

    @Override
    public void visitMedication(MedicationBo medicationBo) {
        Optional.ofNullable(medicationBo)
                .ifPresent(dataBo -> {
                    dataBo.setId(null);
                    dataBo.setStatusId(MedicationStatementStatus.DRAFT_DISCARDED);
                    loadMedication.run(patientId, documentId, dataBo);
                });
    }

    @Override
    public void visitRiskFactor(RiskFactorBo riskFactorBo) {

    }

    @Override
    public void visitAnestheticHistory(AnestheticHistoryBo anestheticHistoryBo) {

    }

    @Override
    public void visitAnestheticSubstance(AnestheticSubstanceBo anestheticSubstanceBo) {

    }

    @Override
    public void visitProcedureDescription(ProcedureDescriptionBo procedureDescriptionBo) {

    }

    @Override
    public void visitAnestheticTechnique(AnestheticTechniqueBo anestheticTechniqueBo) {

    }

    @Override
    public void visitMeasuringPoint(MeasuringPointBo measuringPointBo) {

    }

    @Override
    public void visitPostAnesthesiaStatus(PostAnesthesiaStatusBo postAnesthesiaStatusBo) {

    }
}
