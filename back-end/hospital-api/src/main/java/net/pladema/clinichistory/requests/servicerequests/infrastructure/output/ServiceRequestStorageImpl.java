package net.pladema.clinichistory.requests.servicerequests.infrastructure.output;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.application.port.ServiceRequestStorage;

import net.pladema.clinichistory.requests.servicerequests.domain.ServiceRequestProcedureInfoBo;

import net.pladema.clinichistory.requests.servicerequests.repository.ServiceRequestRepository;

import net.pladema.clinichistory.requests.servicerequests.repository.TranscribedServiceRequestRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequest;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequestStatus;
import net.pladema.clinichistory.requests.servicerequests.service.DeleteDiagnosticReportService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.TranscribedServiceRequestBo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class ServiceRequestStorageImpl implements ServiceRequestStorage {

	private final ServiceRequestRepository serviceRequestRepository;
	private final TranscribedServiceRequestRepository transcribedServiceRequestRepository;
	private final DeleteDiagnosticReportService deleteDiagnosticReportService;

	@Override
	public List<ServiceRequestProcedureInfoBo> getProceduresByServiceRequestIds(List<Integer> serviceRequestIds) {
		log.debug("Input parameter -> serviceRequestIds {} ", serviceRequestIds);
		return serviceRequestRepository.getServiceRequestsProcedures(serviceRequestIds);
	}

	@Override
	public TranscribedServiceRequestBo getTranscribedServiceRequest(Integer transcribedServiceRequestId) {
		return transcribedServiceRequestRepository.getTranscribedServiceRequest(transcribedServiceRequestId)
				.orElse(null);
	}

	@Override
	public void cancelServiceRequest(Integer serviceRequestId){
		log.debug("Input parameter -> serviceRequestId {} ", serviceRequestId);
		Optional<ServiceRequest> optServiceRequest = serviceRequestRepository.findById(serviceRequestId);
		if (optServiceRequest.isPresent()) {
			List<Integer> diagnosticReportIds = serviceRequestRepository.getActiveServiceRequestProcedures(serviceRequestId).stream().map(ServiceRequestProcedureInfoBo::getDiagnosticReportId).collect(Collectors.toList());
			diagnosticReportIds.forEach(drId -> {deleteDiagnosticReportService.execute(optServiceRequest.get().getPatientId(), drId);});
			optServiceRequest.get().setStatusId(ServiceRequestStatus.CANCELLED);
			serviceRequestRepository.save(optServiceRequest.get());
		}
	}

}
