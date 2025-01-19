package ar.lamansys.sgh.clinichistory.domain.ips.visitor;

import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticSubstanceBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnestheticTechniqueBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.IpsBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MeasuringPointBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.PostAnesthesiaStatusBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureDescriptionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;

public interface IpsVisitor {

    default void visit(IpsBo ipsBo) {}

    void visitDiagnosis(DiagnosisBo diagnosisBo);
    void visitHealthCondition(HealthConditionBo healthConditionBo);
    void visitProcedure(ProcedureBo procedureBo);
    void visitAnthropometricData(AnthropometricDataBo anthropometricDataBo);
    void visitRiskFactor(RiskFactorBo riskFactorBo);
    void visitAnestheticHistory(AnestheticHistoryBo anestheticHistoryBo);
    void visitMedication(MedicationBo medicationBo);
    void visitAnestheticSubstance(AnestheticSubstanceBo anestheticSubstanceBo);
    void visitProcedureDescription(ProcedureDescriptionBo procedureDescriptionBo);
    void visitAnestheticTechnique(AnestheticTechniqueBo anestheticTechniqueBo);
    void visitMeasuringPoint(MeasuringPointBo measuringPointBo);
    void visitPostAnesthesiaStatus(PostAnesthesiaStatusBo postAnesthesiaStatusBo);
}
