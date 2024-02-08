package net.pladema.clinichistory.requests.servicerequests.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.repository.OrderImageFileRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.TranscribedServiceRequestRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.domain.FileVo;
import net.pladema.clinichistory.requests.servicerequests.service.DeleteTranscribedOrderService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeleteTranscribedOrderServiceImpl implements DeleteTranscribedOrderService {

	private final OrderImageFileRepository orderImageFileRepository;

	private final TranscribedServiceRequestRepository transcribedServiceRequestRepository;

    @Override
    public Integer execute(Integer orderId) {
        log.debug("Input: orderId: {}", orderId);
		deleteImages(orderId);
		deleteTranscribedOrders(orderId);
        return null;
    }
	
    private void deleteImages(Integer orderId) {
        log.debug("Input parameters -> orderId {}", orderId);
		List<FileVo> images = orderImageFileRepository.getFilesByOrderId(orderId);
		for (FileVo image : images) {
			orderImageFileRepository.deleteById(image.getFileId());
		}
    }

	private void deleteTranscribedOrders(Integer orderId){
		log.debug("Input parameters -> orderId {}", orderId);
		transcribedServiceRequestRepository.deleteById(orderId);
	}
}
