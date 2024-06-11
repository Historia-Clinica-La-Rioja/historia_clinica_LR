package net.pladema.clinichistory.outpatient.createoutpatient.service;

import java.time.LocalDateTime;
import java.util.List;

import net.pladema.clinichistory.outpatient.createoutpatient.service.exceptions.CreateOutpatientDocumentException;
import net.pladema.clinichistory.outpatient.createoutpatient.service.exceptions.CreateOutpatientDocumentExceptionEnum;
import net.pladema.clinichistory.requests.servicerequests.application.port.ServiceRequestStorage;
import net.pladema.clinichistory.requests.servicerequests.service.CompleteDiagnosticReportService;

import net.pladema.clinichistory.requests.servicerequests.service.domain.CompleteDiagnosticReportBo;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.service.CreateServiceRequestService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.ServiceRequestBo;
import net.pladema.events.EHospitalApiTopicDto;
import net.pladema.events.HospitalApiPublisher;

@RequiredArgsConstructor
@Service
@Slf4j
public class CreateOutpatientConsultationServiceRequestImpl implements CreateOutpatientConsultationServiceRequest {

	private final CreateServiceRequestService createServiceRequestService;
	private final CompleteDiagnosticReportService completeDiagnosticReportService;
	private final ServiceRequestStorage serviceRequestStorage;

	private final HospitalApiPublisher hospitalApiPublisher;

	/**
	 * Creates a service request for the procedures of a new outpatient consultation.
	 * If the consultation has many procedures, this method must be called once for
	 * each of them.
	 *
	 * Very similar to other callers of CreateServiceRequestService#execute.
	 * It creates a ServiceRequestBo and then calls the create service request use case.
	 * If createAsFinal is true, the new diagnostic reports (linked to the new service request)
	 * will be marked as complete (status=FINAL). Otherwise, its status will be
	 * pending (status=REGISTERED).
	 *
	 * Example of what the db looks like after this use case is executed (this consultation has only one procedure):
	 *
	 *	outpatient_consultation
	 * 		(id=3222, ...)
	 *
	 * 	service_request
	 * 		(id=832, source_type_id=(1, outpatient), source_id=3222)
	 *
	 * 	document
	 * 		(id=9285, source_id=832, type_id=(6, order))
	 * 		(id=9284, source_id=3222, type_id=(4, outpatient))
	 *
	 * 	diagnostic_report
	 * 		(id=1994, ...)
	 *
	 * 	document_diagnostic_report
	 * 		(document_id=9285, diagnostic_report_id=1994)
	 *
	 * 	The relations between these entities are as follows:
	 * 		service_request -> outpatient_consultation (via (source_id,source_type))
	 * 		service_request <- document <- document_diagnostic_report -> diagnostic_report
	 *
	 */
	@Override
	public Integer execute(Integer doctorId, String categoryId, BasicPatientDto patientDto, Integer institutionId,
		Integer healthConditionId, Integer medicalCoverageId, Integer outpatientConsultationId,
		String snomedSctid, String snomedPt, Boolean createAsFinal
	)
	{

		log.debug("execute -> institutionId {}, doctorId {}, patientDto {}, categoryId {}, " +
						"medicalCoverageId {}, outpatientConsultationId {}, snomedSctid {}, snomedPt {}, healthConditionId {}",
				institutionId,
				doctorId,
				patientDto,
				categoryId,
				medicalCoverageId,
				outpatientConsultationId,
				snomedSctid,
				snomedPt,
				healthConditionId
		);
		ServiceRequestBo serviceRequestBo = buildServiceRequestBo(
			institutionId,
			doctorId,
			patientDto,
			categoryId,
			medicalCoverageId,
			outpatientConsultationId,
			snomedSctid,
			snomedPt,
			healthConditionId
		);
		Integer newServiceRequestId = createServiceRequestService.execute(serviceRequestBo);
		hospitalApiPublisher.publish(serviceRequestBo.getPatientId(), institutionId, getTopicToPublish(categoryId));
		if (createAsFinal) {
			transitionToFinal(newServiceRequestId, patientDto.getId(), institutionId, outpatientConsultationId);
		}
		log.debug("Output -> {}", newServiceRequestId);
		return newServiceRequestId;
	}

	/**
	 * Advance the new diagnostic report to status=FINAL
	 * @param serviceRequestId
	 */
	private void transitionToFinal(Integer serviceRequestId, Integer patientId, Integer institutionId, Integer outpatientConsultationId) {
		var createdDiagnosticReports = serviceRequestStorage.getProceduresByServiceRequestIds(List.of(serviceRequestId));
		Integer diagnosticReportId = createdDiagnosticReports.stream()
		.map(diagnosticReport -> diagnosticReport.getDiagnosticReportId())
		.findFirst()
		.orElseThrow(() -> creationError(outpatientConsultationId));

		//There are no observations, reference or link for this diagnostic report
		CompleteDiagnosticReportBo completeDiagnosticReportBo = new CompleteDiagnosticReportBo();
		completeDiagnosticReportService.run(patientId, diagnosticReportId, completeDiagnosticReportBo, institutionId);
	}

	private CreateOutpatientDocumentException creationError(Integer outpatientConsultationId) {
		var error = new CreateOutpatientDocumentException(CreateOutpatientDocumentExceptionEnum.SERVICE_REQUEST_CREATION_FAILED);
		error.addError(String.format("Error al crear la orden. Id consulta: %s", outpatientConsultationId));
		return error;
	}

	private ServiceRequestBo buildServiceRequestBo(
			Integer institutionId,
			Integer doctorId,
			BasicPatientDto patientDto,
			String categoryId,
			Integer medicalCoverageId,
			Integer outpatientConsultationId,
			String snomedSctid,
			String snomedPt,
			Integer healthConditionId)
	{
		log.debug("buildServiceRequestBo -> institutionId {}, doctorId {}, patientDto {}, categoryId {}, " +
		 "medicalCoverageId {}, outpatientConsultationId {}, snomedSctid {}, snomedPt {}, healthConditionId {}",
				institutionId,
				doctorId,
				patientDto,
				categoryId,
				medicalCoverageId,
				outpatientConsultationId,
				snomedSctid,
				snomedPt,
				healthConditionId
		 );

		/**
		 * A single diagnostic report created from the outpatient consultation procedures
		 */
		var diagnosticReport = new DiagnosticReportBo();
		diagnosticReport.setSnomed(new SnomedBo(snomedSctid, snomedPt));
		diagnosticReport.setHealthConditionId(healthConditionId);

		var result = ServiceRequestBo.builder()
				.patientInfo(new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()))
				.categoryId(categoryId)
				.institutionId(institutionId)
				.doctorId(doctorId)
				.diagnosticReports(List.of(diagnosticReport))
				.requestDate(LocalDateTime.now())
				.associatedSourceTypeId(SourceType.OUTPATIENT)
				.associatedSourceId(outpatientConsultationId)
				.medicalCoverageId(medicalCoverageId)
				.observations(null)
				.build();

		log.debug("Output -> {}", result);
		return result;
	}

	private EHospitalApiTopicDto getTopicToPublish(String categoryId) {
		if (categoryId.equals("108252007"))
			return EHospitalApiTopicDto.CLINIC_HISTORY__HOSPITALIZATION__SERVICE_RESQUEST__LABORATORY;
		if (categoryId.equals("363679005"))
			return EHospitalApiTopicDto.CLINIC_HISTORY__HOSPITALIZATION__SERVICE_RESQUEST__IMAGE;
		return EHospitalApiTopicDto.CLINIC_HISTORY__HOSPITALIZATION__SERVICE_RESQUEST;
	}

}
