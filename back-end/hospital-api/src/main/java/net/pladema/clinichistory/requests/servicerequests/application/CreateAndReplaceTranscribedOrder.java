package net.pladema.clinichistory.requests.servicerequests.application;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.service.CreateTranscribedServiceRequestService;
import net.pladema.clinichistory.requests.servicerequests.service.DeleteTranscribedOrderService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.TranscribedServiceRequestBo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CreateAndReplaceTranscribedOrder {

    private final DeleteTranscribedOrderService deleteTranscribedOrderService;
    private final CreateTranscribedServiceRequestService createTranscribedServiceRequestService;

    @Transactional
    public Integer run(TranscribedServiceRequestBo transcribedServiceRequestBo, Optional<Integer> oldTranscribedOrderId) {
        log.debug("Input parameters -> transcribedServiceRequestBo {}, oldTranscribedOrderId {}", transcribedServiceRequestBo, oldTranscribedOrderId);

        oldTranscribedOrderId.ifPresent(deleteTranscribedOrderService::execute);

        Integer transcribedOrderId = createTranscribedServiceRequestService.execute(transcribedServiceRequestBo);

        log.debug("Output -> created transcribed order id {}", transcribedOrderId);
        return transcribedOrderId;
    }
}
