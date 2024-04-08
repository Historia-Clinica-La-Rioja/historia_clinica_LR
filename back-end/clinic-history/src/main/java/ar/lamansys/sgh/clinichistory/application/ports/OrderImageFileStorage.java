package ar.lamansys.sgh.clinichistory.application.ports;

import java.util.List;
import java.util.Optional;

import ar.lamansys.sgh.clinichistory.domain.document.OrderImageFileBo;
import ar.lamansys.sgh.clinichistory.domain.document.OrderImageFileReducedBo;

public interface OrderImageFileStorage {

	List<OrderImageFileReducedBo> getOrderImageFileInfo(Integer transcribedOrderId);

	Optional<OrderImageFileBo> findById(Integer id);
}
