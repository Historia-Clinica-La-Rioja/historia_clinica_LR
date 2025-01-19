package net.pladema.clinichistory.requests.transcribed.application.getbyid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.transcribed.application.port.TranscribedServiceRequestStorage;
import net.pladema.clinichistory.requests.transcribed.domain.TranscribedServiceRequestBo;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class GetTranscribedServiceRequest {

    private final TranscribedServiceRequestStorage transcribedServiceRequestStorage;

    public TranscribedServiceRequestBo run(Integer transcribedServiceRequestId) {
        log.debug("Input parameters -> transcribedServiceRequestId {}", transcribedServiceRequestId);
        TranscribedServiceRequestBo result = transcribedServiceRequestStorage.get(transcribedServiceRequestId);
        log.debug("Output -> {}", result);
        return result;
    }
}
