package net.pladema.clinichistory.requests.transcribed.application.update;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.transcribed.application.create.CreateTranscribedServiceRequest;
import net.pladema.clinichistory.requests.transcribed.application.delete.DeleteTranscribedServiceRequest;
import net.pladema.clinichistory.requests.transcribed.domain.TranscribedServiceRequestBo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CreateAndReplaceTranscribedOrder {

    private final DeleteTranscribedServiceRequest deleteTranscribedServiceRequest;
    private final CreateTranscribedServiceRequest createTranscribedServiceRequest;

    @Transactional
    public Integer run(TranscribedServiceRequestBo transcribedServiceRequestBo, Optional<Integer> oldTranscribedOrderId) {
        log.debug("Input parameters -> transcribedServiceRequestBo {}, oldTranscribedOrderId {}", transcribedServiceRequestBo, oldTranscribedOrderId);

        oldTranscribedOrderId.ifPresent(deleteTranscribedServiceRequest::run);

        Integer transcribedOrderId = createTranscribedServiceRequest.run(transcribedServiceRequestBo);

        log.debug("Output -> created transcribed order id {}", transcribedOrderId);
        return transcribedOrderId;
    }
}
