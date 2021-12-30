package net.pladema.clinichistory.outpatient.repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionSummaryVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ReferenceSummaryVo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ProcedureSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ReasonSummaryBo;
import net.pladema.clinichistory.outpatient.repository.domain.OdontologyEvolutionSummaryVo;

import java.util.List;

public interface OdontologyConsultationSummaryRepository {

    List<OdontologyEvolutionSummaryVo> getAllOdontologyEvolutionSummary(Integer patientId);

    List<HealthConditionSummaryVo> getHealthConditionsByPatient(Integer patientId, List<Integer> odontologyConsultationIds);

    List<ReasonSummaryBo> getReasonsByPatient(Integer patientId, List<Integer> odontologyConsultationIds);

    List<ProcedureSummaryBo> getProceduresByPatient(Integer patientId, List<Integer> odontologyConsultationIds);

    List<ReferenceSummaryVo> getReferenceByHealthCondition(Integer healthConditionId, Integer outpatientId);

}
