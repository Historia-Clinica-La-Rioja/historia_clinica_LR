package ar.lamansys.sgh.clinichistory.application.ports;

import ar.lamansys.sgh.clinichistory.domain.hce.summary.HealthConditionSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.OutpatientEvolutionSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ProcedureSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ReasonSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ReferenceSummaryBo;

import java.util.List;

public interface HCEOutpatientConsultationSummaryStorage {

    List<OutpatientEvolutionSummaryBo> getAllOutpatientEvolutionSummary(Integer patientId);

    List<HealthConditionSummaryBo> getHealthConditionsByPatient(Integer patientId, List<Integer> outpatientIds);

    List<ReasonSummaryBo> getReasonsByPatient(Integer patientId, List<Integer> outpatientIds);

    List<ProcedureSummaryBo> getProceduresByPatient(Integer patientId, List<Integer> outpatientIds);

    List<ReferenceSummaryBo> getReferencesByHealthCondition(Integer healthConditionId, Integer outpatientId);

}
