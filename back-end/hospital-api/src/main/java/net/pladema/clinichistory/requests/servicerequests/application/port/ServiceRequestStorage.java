package net.pladema.clinichistory.requests.servicerequests.application.port;

import java.util.List;
import net.pladema.clinichistory.requests.servicerequests.domain.ServiceRequestProcedureInfoBo;

public interface ServiceRequestStorage {

	List<ServiceRequestProcedureInfoBo> getProceduresByServiceRequestIds(List<Integer> serviceRequestIds);

	void cancelServiceRequest(Integer serviceRequestId);

    List<String> getDiagnosticReportsFrom(Integer diagnosticReportId, Integer transcribedServiceRequestId);
}
