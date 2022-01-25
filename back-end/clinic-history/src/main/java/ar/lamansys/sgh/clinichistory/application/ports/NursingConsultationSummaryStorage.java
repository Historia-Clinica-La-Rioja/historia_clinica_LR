package ar.lamansys.sgh.clinichistory.application.ports;

import ar.lamansys.sgh.clinichistory.domain.hce.summary.HealthConditionSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.NursingEvolutionSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.summary.ProcedureSummaryBo;

import java.util.List;

public interface NursingConsultationSummaryStorage {

    List<NursingEvolutionSummaryBo> getAllNursingEvolutionSummary(Integer patientId);

    List<HealthConditionSummaryBo> getHealthConditionsByPatient(Integer patientId, List<Integer> nursingConsultationIds);

    List<ProcedureSummaryBo> getProceduresByPatient(Integer patientId, List<Integer> nursingConsultationIds);

}
