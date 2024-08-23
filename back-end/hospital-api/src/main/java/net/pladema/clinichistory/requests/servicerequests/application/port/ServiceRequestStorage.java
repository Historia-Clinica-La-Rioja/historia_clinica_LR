package net.pladema.clinichistory.requests.servicerequests.application.port;

import java.util.List;

import net.pladema.clinichistory.requests.servicerequests.domain.ServiceRequestProcedureInfoBo;
import net.pladema.clinichistory.requests.servicerequests.domain.SnomedItemBo;

public interface ServiceRequestStorage {

	List<ServiceRequestProcedureInfoBo> getProceduresByServiceRequestIds(List<Integer> serviceRequestIds);

	void cancelServiceRequest(Integer serviceRequestId);

    List<String> getDiagnosticReportsFrom(Integer diagnosticReportId, Integer transcribedServiceRequestId);

	List<SnomedItemBo> getMostFrequentStudies(Integer professionalId, Integer institutionId, Integer limit);
}
