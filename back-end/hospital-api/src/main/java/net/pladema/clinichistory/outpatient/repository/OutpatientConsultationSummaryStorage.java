package net.pladema.clinichistory.outpatient.repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionSummaryVo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ProcedureSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ReasonSummaryBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ReferenceSummaryVo;
import net.pladema.clinichistory.outpatient.repository.domain.OutpatientEvolutionSummaryVo;

import java.util.List;

public interface OutpatientConsultationSummaryStorage {

    List<OutpatientEvolutionSummaryVo> getAllOutpatientEvolutionSummary(Integer patientId);

    List<HealthConditionSummaryVo> getHealthConditionsByPatient(Integer patientId, List<Integer> outpatientIds);

    List<ReasonSummaryBo> getReasonsByPatient(Integer patientId, List<Integer> outpatientIds);

    List<ProcedureSummaryBo> getProceduresByPatient(Integer patientId, List<Integer> outpatientIds);

    List<ReferenceSummaryVo> getReferencesByHealthCondition(Integer healthConditionId, Integer outpatientId);

}
