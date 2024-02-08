package net.pladema.clinichistory.requests.servicerequests.infrastructure.output;

import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DiagnosticReportRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
import net.pladema.medicalconsultation.appointment.service.domain.EquipmentAppointmentBo;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class ServiceRequestStorageImpl implements ServiceRequestStorage {

	private final ServiceRequestRepository serviceRequestRepository;
	private final TranscribedServiceRequestRepository transcribedServiceRequestRepository;
	private final DeleteDiagnosticReportService deleteDiagnosticReportService;
	private final DiagnosticReportRepository diagnosticReportRepository;

	@Override
	public List<ServiceRequestProcedureInfoBo> getProceduresByServiceRequestIds(List<Integer> serviceRequestIds) {
		log.debug("Input parameter -> serviceRequestIds {} ", serviceRequestIds);
		return serviceRequestRepository.getServiceRequestsProcedures(serviceRequestIds);
	}

	@Override
	public TranscribedServiceRequestBo getTranscribedServiceRequest(Integer transcribedServiceRequestId) {
		log.debug("Input parameter -> transcribedServiceRequestId {} ", transcribedServiceRequestId);
		return transcribedServiceRequestRepository.getTranscribedServiceRequest(transcribedServiceRequestId)
				.map(transcribedServiceRequestBo -> {
					transcribedServiceRequestBo.setDiagnosticReports(transcribedServiceRequestRepository.getDiagnosticReports(transcribedServiceRequestId));
					return transcribedServiceRequestBo;
				})
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

	@Override
	public List<String> getDiagnosticReportsFrom(EquipmentAppointmentBo equipmentAppointmentBo) {
		log.debug("Input parameters -> equipmentAppointmentBo {} ", equipmentAppointmentBo);

		Integer diagnosticReportFromOrder = equipmentAppointmentBo.getDiagnosticReportId();
		if (diagnosticReportFromOrder != null)
			return diagnosticReportRepository.getDiagnosticReportById(diagnosticReportFromOrder)
					.map(DiagnosticReportBo::getDiagnosticReportSnomedPt)
					.map(Arrays::asList)
					.orElse(List.of());

		Integer transcribedServiceRequestId = equipmentAppointmentBo.getTranscribedServiceRequestId();
		if (transcribedServiceRequestId != null)
			return this.getTranscribedServiceRequest(transcribedServiceRequestId)
					.getStudies();

		return List.of();
	}

}
