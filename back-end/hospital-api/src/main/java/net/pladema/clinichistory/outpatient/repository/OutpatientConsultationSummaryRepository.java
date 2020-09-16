package net.pladema.clinichistory.outpatient.repository;

import net.pladema.clinichistory.outpatient.repository.domain.HealthConditionSummaryVo;
import net.pladema.clinichistory.outpatient.repository.domain.OutpatientEvolutionSummaryVo;
import net.pladema.clinichistory.outpatient.repository.domain.ProcedureSummaryVo;
import net.pladema.clinichistory.outpatient.repository.domain.ReasonSummaryVo;

import java.util.List;

public interface OutpatientConsultationSummaryRepository {

    List<OutpatientEvolutionSummaryVo> getAllOutpatientEvolutionSummary(Integer institutionId, Integer patientId);

    List<HealthConditionSummaryVo> getHealthConditionsByPatient(Integer patientId, List<Integer> outpatientIds);

    List<ReasonSummaryVo> getReasonsByPatient(Integer patientId, List<Integer> outpatientIds);

    List<ProcedureSummaryVo> getProceduresByPatient(Integer patientId, List<Integer> outpatientIds);
}
