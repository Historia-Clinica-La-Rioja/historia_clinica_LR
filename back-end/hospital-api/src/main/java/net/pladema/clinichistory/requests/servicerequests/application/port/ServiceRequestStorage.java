package net.pladema.clinichistory.requests.servicerequests.application.port;

import net.pladema.clinichistory.requests.servicerequests.domain.ServiceRequestProcedureInfoBo;

import java.util.List;

public interface ServiceRequestStorage {

	List<ServiceRequestProcedureInfoBo> getProceduresByServiceRequestIds(List<Integer> serviceRequestIds);
}
