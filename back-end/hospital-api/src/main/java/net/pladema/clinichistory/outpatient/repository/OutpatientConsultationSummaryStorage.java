package net.pladema.clinichistory.outpatient.repository;

import ar.lamansys.sgh.clinichistory.domain.hce.summary.ProcedureSummaryBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionSummaryVo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.OutpatientBasicDataBo;

import java.util.List;

public interface OutpatientConsultationSummaryStorage {

	List<HealthConditionSummaryVo> getHealthConditionsByPatient(Integer patientId, List<Integer> outpatientIds);

	List<ProcedureSummaryBo> getProceduresByPatient(Integer patientId, List<Integer> outpatientIds);

	List<OutpatientBasicDataBo> getOutpatientConsultationsToCipres(Integer limit);

}
