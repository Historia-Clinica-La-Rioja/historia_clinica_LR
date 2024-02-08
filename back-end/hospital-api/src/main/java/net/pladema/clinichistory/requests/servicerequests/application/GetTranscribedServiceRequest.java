package net.pladema.clinichistory.requests.servicerequests.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.application.port.ServiceRequestStorage;
import net.pladema.clinichistory.requests.servicerequests.service.domain.TranscribedServiceRequestBo;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class GetTranscribedServiceRequest {

    private final ServiceRequestStorage serviceRequestStorage;

    public TranscribedServiceRequestBo run(Integer transcribedServiceRequestId) {
        log.debug("Input parameters -> transcribedServiceRequestId {}", transcribedServiceRequestId);
        TranscribedServiceRequestBo result = serviceRequestStorage.getTranscribedServiceRequest(transcribedServiceRequestId);
        log.debug("Output -> {}", result);
        return result;
    }
}
