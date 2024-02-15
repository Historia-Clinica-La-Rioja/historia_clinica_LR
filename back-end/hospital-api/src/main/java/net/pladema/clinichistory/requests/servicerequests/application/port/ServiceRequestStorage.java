package net.pladema.clinichistory.requests.servicerequests.application.port;

import net.pladema.clinichistory.requests.servicerequests.domain.ServiceRequestProcedureInfoBo;

import java.util.List;
import net.pladema.clinichistory.requests.servicerequests.service.domain.TranscribedServiceRequestBo;

public interface ServiceRequestStorage {

	List<ServiceRequestProcedureInfoBo> getProceduresByServiceRequestIds(List<Integer> serviceRequestIds);

	TranscribedServiceRequestBo getTranscribedServiceRequest(Integer transcribedServiceRequestId);

	void cancelServiceRequest(Integer serviceRequestId);
}
