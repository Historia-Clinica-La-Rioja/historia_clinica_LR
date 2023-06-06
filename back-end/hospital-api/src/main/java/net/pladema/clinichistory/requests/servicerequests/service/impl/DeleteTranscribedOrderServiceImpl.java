package net.pladema.clinichistory.requests.servicerequests.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.pladema.clinichistory.requests.servicerequests.repository.OrderImageFileRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.TranscribedServiceRequestRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.domain.FileVo;
import net.pladema.clinichistory.requests.servicerequests.service.DeleteTranscribedOrderService;

@Service
public class DeleteTranscribedOrderServiceImpl implements DeleteTranscribedOrderService {

	private final OrderImageFileRepository orderImageFileRepository;

	private final TranscribedServiceRequestRepository transcribedServiceRequestRepository;

    private static final Logger LOG = LoggerFactory.getLogger(DeleteTranscribedOrderServiceImpl.class);
    private final String OUTPUT = "Output -> {}";

    public DeleteTranscribedOrderServiceImpl(OrderImageFileRepository orderImageFileRepository, TranscribedServiceRequestRepository transcribedServiceRequestRepository){
		this.transcribedServiceRequestRepository = transcribedServiceRequestRepository;
		this.orderImageFileRepository = orderImageFileRepository;
    }

    @Override
    public Integer execute(Integer orderId) {
        LOG.debug("Input: orderId: {}", orderId);
		deleteImages(orderId);
		deleteTranscribedOrders(orderId);
        return null;
    }

	@Override
	public Integer getDiagnosticReportId(Integer orderId){
		LOG.debug("Input: orderId: {}", orderId);
		return transcribedServiceRequestRepository.getStudyIdByOrderId(orderId);
	}
    private void deleteImages(Integer orderId) {
        LOG.debug("Input parameters -> orderId {}", orderId);
		List<FileVo> images = orderImageFileRepository.getFilesByOrderId(orderId);
		for (FileVo image : images) {
			orderImageFileRepository.deleteById(image.getFileId());
		}
    }

	private void deleteTranscribedOrders(Integer orderId){
		LOG.debug("Input parameters -> orderId {}", orderId);
		transcribedServiceRequestRepository.deleteById(orderId);
	}
}
