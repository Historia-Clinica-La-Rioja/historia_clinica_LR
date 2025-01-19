package ar.lamansys.sgh.clinichistory.domain.ips.visitor;

import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticSubstanceBo;

import java.util.Optional;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SetNullIdsIpsVisitor implements IpsVisitor {

    @Override
    public void visitDiagnosis(DiagnosisBo diagnosisBo) {
        Optional.ofNullable(diagnosisBo)
                .ifPresent(e -> e.setId(null));
    }

    @Override
    public void visitHealthCondition(HealthConditionBo healthConditionBo) {
        Optional.ofNullable(healthConditionBo)
                .ifPresent(e -> e.setId(null));
    }

    @Override
    public void visitProcedure(ProcedureBo procedureBo) {
        Optional.ofNullable(procedureBo)
                .ifPresent(e -> e.setId(null));
    }

    @Override
    public void visitAnthropometricData(AnthropometricDataBo anthropometricDataBo) {
        Optional.ofNullable(anthropometricDataBo)
                .ifPresent(dataBo -> {
                    Optional.ofNullable(dataBo.getBloodType())
                            .ifPresent(e -> e.setId(null));
                    Optional.ofNullable(dataBo.getHeight())
                            .ifPresent(e -> e.setId(null));
                    Optional.ofNullable(dataBo.getWeight())
                            .ifPresent(e -> e.setId(null));
                });
    }

    @Override
    public void visitRiskFactor(RiskFactorBo riskFactorBo) {
        Optional.ofNullable(riskFactorBo)
                .ifPresent(dataBo -> {
                    Optional.ofNullable(dataBo.getDiastolicBloodPressure())
                            .ifPresent(e -> e.setId(null));
                    Optional.ofNullable(dataBo.getSystolicBloodPressure())
                            .ifPresent(e -> e.setId(null));
                    Optional.ofNullable(dataBo.getHematocrit())
                            .ifPresent(e -> e.setId(null));
                });
    }

    @Override
    public void visitAnestheticHistory(AnestheticHistoryBo anestheticHistoryBo) {
        Optional.ofNullable(anestheticHistoryBo)
                .ifPresent(e -> e.setId(null));
    }

    @Override
    public void visitMedication(MedicationBo medicationBo) {
        Optional.ofNullable(medicationBo)
                .ifPresent(e -> e.setId(null));
    }

    @Override
    public void visitAnestheticSubstance(AnestheticSubstanceBo anestheticSubstanceBo) {
        Optional.ofNullable(anestheticSubstanceBo)
                .ifPresent(e -> e.setId(null));
    }

    @Override
    public void visitProcedureDescription(ProcedureDescriptionBo procedureDescriptionBo) {
        Optional.ofNullable(procedureDescriptionBo)
                .ifPresent(e -> e.setId(null));
    }

    @Override
    public void visitAnestheticTechnique(AnestheticTechniqueBo anestheticTechniqueBo) {
        Optional.ofNullable(anestheticTechniqueBo)
                .ifPresent(e -> e.setId(null));
    }

    @Override
    public void visitMeasuringPoint(MeasuringPointBo measuringPointBo) {
        Optional.ofNullable(measuringPointBo)
                .ifPresent(e -> e.setId(null));
    }

    @Override
    public void visitPostAnesthesiaStatus(PostAnesthesiaStatusBo postAnesthesiaStatusBo) {
        Optional.ofNullable(postAnesthesiaStatusBo)
                .ifPresent(e -> e.setId(null));
    }
}
