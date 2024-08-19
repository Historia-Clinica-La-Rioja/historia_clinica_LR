package net.pladema.clinichistory.requests.transcribed.infrastructure.output;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.application.ports.OrderImageFileStorage;
import ar.lamansys.sgh.clinichistory.domain.document.OrderImageFileBo;
import ar.lamansys.sgh.clinichistory.domain.document.OrderImageFileReducedBo;
import net.pladema.clinichistory.requests.transcribed.infrastructure.output.repository.OrderImageFileRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.domain.FileVo;
import net.pladema.clinichistory.requests.transcribed.infrastructure.output.repository.entity.OrderImageFile;

@Service
public class OrderImageFileStorageImpl implements OrderImageFileStorage {

    private final OrderImageFileRepository orderImageFileRepository;

    public OrderImageFileStorageImpl(OrderImageFileRepository orderImageFileRepository) {
        this.orderImageFileRepository = orderImageFileRepository;
    }

	@Override
	public List<OrderImageFileReducedBo> getOrderImageFileInfo(Integer transcribedOrderId) {
		return orderImageFileRepository.getFilesByOrderId(transcribedOrderId)
				.stream()
				.map(this::mapToOrderImageFileReducedBo)
				.collect(Collectors.toList());
	}

    @Override
    public Optional<OrderImageFileBo> findById(Integer id) {
        return orderImageFileRepository.findById(id)
                .map(this::mapToOrderImageFileBo);
    }

	private OrderImageFileBo mapToOrderImageFileBo(OrderImageFile orderImageFile) {
        return new OrderImageFileBo(
				orderImageFile.getId(),
				orderImageFile.getPath(),
				orderImageFile.getContentType(),
				orderImageFile.getSize(),
				orderImageFile.getOrderId(),
				orderImageFile.getName(),
				orderImageFile.getDeleteable().isDeleted());
    }

	private OrderImageFileReducedBo mapToOrderImageFileReducedBo(FileVo fileVo) {
		return new OrderImageFileReducedBo(
				fileVo.getFileId(),
				fileVo.getFileName());
	}
}
