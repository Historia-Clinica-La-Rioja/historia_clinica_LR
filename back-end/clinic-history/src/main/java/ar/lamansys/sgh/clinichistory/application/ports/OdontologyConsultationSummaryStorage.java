package ar.lamansys.sgh.clinichistory.application.ports;

import ar.lamansys.sgh.clinichistory.domain.hce.summary.HealthConditionSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.OdontologyEvolutionSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ProcedureSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ReasonSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ReferenceSummaryBo;

import java.util.List;

public interface OdontologyConsultationSummaryStorage {

    List<OdontologyEvolutionSummaryBo> getAllOdontologyEvolutionSummary(Integer patientId);

    List<HealthConditionSummaryBo> getHealthConditionsByPatient(Integer patientId, List<Integer> odontologyConsultationIds);

    List<ReasonSummaryBo> getReasonsByPatient(Integer patientId, List<Integer> odontologyConsultationIds);

    List<ProcedureSummaryBo> getProceduresByPatient(Integer patientId, List<Integer> odontologyConsultationIds);

    List<ReferenceSummaryBo> getReferencesByHealthCondition(Integer healthConditionId, Integer consultationId);
}
