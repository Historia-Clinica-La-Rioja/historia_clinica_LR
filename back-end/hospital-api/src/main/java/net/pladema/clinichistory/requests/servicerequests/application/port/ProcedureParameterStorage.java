package net.pladema.clinichistory.requests.servicerequests.application.port;

import net.pladema.clinichistory.requests.servicerequests.domain.observations.ProcedureParameterBo;

import java.util.List;

public interface ProcedureParameterStorage {
	List<ProcedureParameterBo> findProcedureParametersById(List<Integer> parameterIds);
}
