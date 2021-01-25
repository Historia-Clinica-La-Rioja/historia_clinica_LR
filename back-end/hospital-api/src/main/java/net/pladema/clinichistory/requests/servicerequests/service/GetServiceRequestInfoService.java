package net.pladema.clinichistory.requests.servicerequests.service;

import net.pladema.clinichistory.requests.servicerequests.service.domain.ServiceRequestBo;

public interface GetServiceRequestInfoService {
    ServiceRequestBo run(Integer serviceRequestId);
}
