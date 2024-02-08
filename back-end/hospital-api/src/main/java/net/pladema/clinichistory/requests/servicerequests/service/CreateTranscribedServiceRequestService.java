package net.pladema.clinichistory.requests.servicerequests.service;

import net.pladema.clinichistory.requests.servicerequests.service.domain.TranscribedServiceRequestBo;

public interface CreateTranscribedServiceRequestService {

    Integer execute(TranscribedServiceRequestBo transcribedServiceRequest);
}
