package net.pladema.clinichistory.requests.servicerequests.infrastructure.output;

import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DiagnosticReportRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.application.port.ServiceRequestStorage;
import net.pladema.clinichistory.requests.servicerequests.domain.ServiceRequestProcedureInfoBo;
import net.pladema.clinichistory.requests.servicerequests.repository.ServiceRequestRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequest;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequestStatus;
import net.pladema.clinichistory.requests.servicerequests.service.DeleteDiagnosticReportService;
import net.pladema.clinichistory.requests.transcribed.application.port.TranscribedServiceRequestStorage;
import net.pladema.medicalconsultation.appointment.service.domain.EquipmentAppointmentBo;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ServiceRequestStorageImpl implements ServiceRequestStorage {

	private final ServiceRequestRepository serviceRequestRepository;
	private final DeleteDiagnosticReportService deleteDiagnosticReportService;
	private final DiagnosticReportRepository diagnosticReportRepository;
	private final TranscribedServiceRequestStorage transcribedServiceRequestStorage;

	@Override
	public List<ServiceRequestProcedureInfoBo> getProceduresByServiceRequestIds(List<Integer> serviceRequestIds) {
		log.debug("Input parameter -> serviceRequestIds {} ", serviceRequestIds);
		return serviceRequestRepository.getServiceRequestsProcedures(serviceRequestIds);
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

	@Override
	public List<String> getDiagnosticReportsFrom(Integer diagnosticReportId, Integer transcribedServiceRequestId) {
		log.debug("Input parameters -> diagnosticReportId {}, transcribedServiceRequestId {} ",
				diagnosticReportId, transcribedServiceRequestId);

		if (diagnosticReportId != null)
			return diagnosticReportRepository.getDiagnosticReportById(diagnosticReportId)
					.map(DiagnosticReportBo::getDiagnosticReportSnomedPt)
					.map(Arrays::asList)
					.orElse(List.of());

		if (transcribedServiceRequestId != null)
			return transcribedServiceRequestStorage.get(transcribedServiceRequestId)
					.getStudies();

		return List.of();
	}


}
