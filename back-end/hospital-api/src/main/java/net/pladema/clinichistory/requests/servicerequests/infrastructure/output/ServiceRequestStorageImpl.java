package net.pladema.clinichistory.requests.servicerequests.infrastructure.output;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.application.port.ServiceRequestStorage;

import net.pladema.clinichistory.requests.servicerequests.domain.ServiceRequestProcedureInfoBo;

import net.pladema.clinichistory.requests.servicerequests.repository.ServiceRequestRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class ServiceRequestStorageImpl implements ServiceRequestStorage {

	private final ServiceRequestRepository serviceRequestRepository;

	@Override
	public List<ServiceRequestProcedureInfoBo> getProceduresByServiceRequestIds(List<Integer> serviceRequestIds) {
		log.debug("Input parameter -> serviceRequestIds {} ", serviceRequestIds);
		return serviceRequestRepository.getServiceRequestsProcedures(serviceRequestIds);
	}

}
