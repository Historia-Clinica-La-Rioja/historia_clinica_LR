package net.pladema.clinichistory.outpatient.repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionSummaryVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ProcedureSummaryVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ReasonSummaryVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ReferenceSummaryVo;
import net.pladema.clinichistory.outpatient.repository.domain.OutpatientEvolutionSummaryVo;

import java.util.List;
import java.util.Optional;

public interface OutpatientConsultationSummaryRepository {

    List<OutpatientEvolutionSummaryVo> getAllOutpatientEvolutionSummary(Integer patientId);

    List<HealthConditionSummaryVo> getHealthConditionsByPatient(Integer patientId, List<Integer> outpatientIds);

    List<ReasonSummaryVo> getReasonsByPatient(Integer patientId, List<Integer> outpatientIds);

    List<ProcedureSummaryVo> getProceduresByPatient(Integer patientId, List<Integer> outpatientIds);

    Optional<ReferenceSummaryVo> getReferenceByHealthCondition(Integer healthConditionId, Integer outpatientId);

}
