package net.pladema.clinichistory.outpatient.repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionSummaryVo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ProcedureSummaryBo;
import net.pladema.clinichistory.outpatient.repository.domain.NursingEvolutionSummaryVo;

import java.util.List;

public interface NursingConsultationSummaryRepository {

    List<NursingEvolutionSummaryVo> getAllNursingEvolutionSummary(Integer patientId);

    List<HealthConditionSummaryVo> getHealthConditionsByPatient(Integer patientId, List<Integer> nursingConsultationIds);

    List<ProcedureSummaryBo> getProceduresByPatient(Integer patientId, List<Integer> nursingConsultationIds);

}
