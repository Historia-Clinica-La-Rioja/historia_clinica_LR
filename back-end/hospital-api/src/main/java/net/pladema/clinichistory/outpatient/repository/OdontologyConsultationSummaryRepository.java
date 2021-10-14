package net.pladema.clinichistory.outpatient.repository;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionSummaryVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ProcedureSummaryVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ReasonSummaryVo;
import net.pladema.clinichistory.outpatient.repository.domain.OdontologyEvolutionSummaryVo;

import java.util.List;

public interface OdontologyConsultationSummaryRepository {

    List<OdontologyEvolutionSummaryVo> getAllOdontologyEvolutionSummary(Integer patientId);

    List<HealthConditionSummaryVo> getHealthConditionsByPatient(Integer patientId, List<Integer> odontologyConsultationIds);

    List<ReasonSummaryVo> getReasonsByPatient(Integer patientId, List<Integer> odontologyConsultationIds);

    List<ProcedureSummaryVo> getProceduresByPatient(Integer patientId, List<Integer> odontologyConsultationIds);

}
