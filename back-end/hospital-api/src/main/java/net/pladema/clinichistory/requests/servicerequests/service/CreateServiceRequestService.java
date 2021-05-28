package net.pladema.clinichistory.requests.servicerequests.service;

import net.pladema.clinichistory.requests.servicerequests.service.domain.ServiceRequestBo;

public interface CreateServiceRequestService {

    Integer execute(ServiceRequestBo serviceRequestBo);
}
