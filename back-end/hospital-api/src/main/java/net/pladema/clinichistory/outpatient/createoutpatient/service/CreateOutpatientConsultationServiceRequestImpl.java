package net.pladema.clinichistory.outpatient.createoutpatient.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import ar.lamansys.sgh.shared.domain.servicerequest.SharedAddObservationsCommandVo;
import ar.lamansys.sgh.shared.infrastructure.input.service.servicerequest.SharedCreateConsultationServiceRequest;

import net.pladema.clinichistory.requests.servicerequests.service.UpdateDiagnosticReportFileService;

import net.pladema.clinichistory.requests.servicerequests.service.UploadDiagnosticReportCompletedFileService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedHealthConditionPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.outpatient.createoutpatient.service.exceptions.CreateOutpatientConsultationServiceRequestException;
import net.pladema.clinichistory.requests.servicerequests.application.AddDiagnosticReportObservations;
import net.pladema.clinichistory.requests.servicerequests.application.port.ServiceRequestStorage;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.exceptions.DiagnosticReportObservationException;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.exceptions.InvalidProcedureTemplateChangeException;
import net.pladema.clinichistory.requests.servicerequests.service.CompleteDiagnosticReportService;
import net.pladema.clinichistory.requests.servicerequests.service.CreateServiceRequestService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.CompleteDiagnosticReportBo;
import net.pladema.clinichistory.requests.servicerequests.service.domain.ServiceRequestBo;
import net.pladema.events.EHospitalApiTopicDto;
import net.pladema.events.HospitalApiPublisher;

import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@Slf4j
public class CreateOutpatientConsultationServiceRequestImpl implements SharedCreateConsultationServiceRequest {

	private final CreateServiceRequestService createServiceRequestService;
	private final CompleteDiagnosticReportService completeDiagnosticReportService;
	private final ServiceRequestStorage serviceRequestStorage;
	private final HospitalApiPublisher hospitalApiPublisher;
	private final AddDiagnosticReportObservations addDiagnosticReportObservations;
	private final SharedHealthConditionPort sharedHealthConditionPort;
	private final UpdateDiagnosticReportFileService updateDiagnosticReportFileService;
	private final UploadDiagnosticReportCompletedFileService uploadDiagnosticReportCompletedFileService;

	/**
	 * Create an outpatient consultation service request
	 * ==============================================
	 *
	 * This use case creates a service request for the procedures of a new outpatient consultation. This permits
	 * the creation of service requests directly from the consultation form. The alternative is to create
	 * the service request from the "estudios" tab.
	 * Observations attached to the service request are also supported. See below.
	 *
	 * If the consultation has many procedures, this method must be called once for
	 * each of them.
	 *
	 * It works similarly to other callers of CreateServiceRequestService#execute: It creates a ServiceRequestBo
	 * and then calls the create service request use case.
	 * If createAsFinal is true, the new diagnostic reports (linked to the new service request)
	 * will be marked as complete (status=FINAL). Otherwise, its status will be
	 * pending (status=REGISTERED).
	 *
	 * Data model
	 * ===============================================
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
	 * Observations
	 * ===============================================
	 * The procedures of the outpatient consultation may have observations attached that must
	 * be stored by this use case. This is similar to the use case that's called from the "completar resultados" form
	 * of a service request. See ServiceRequestController.addObservations.
	 *
	 */
	@Override
	@Transactional
	public Integer createOutpatientServiceRequest(Integer doctorId, String categoryId, Integer institutionId, String healthConditionSctid,
												  String healthConditionPt, Integer medicalCoverageId, Integer outpatientConsultationId, String snomedSctid,
												  String snomedPt, Boolean createAsFinal, Optional<SharedAddObservationsCommandVo> addObservationsCommand,
												  Integer patientId, Short patientGenderId, Short patientAge, List<MultipartFile> files, String textObservation)
	{
		return execute(doctorId, categoryId, institutionId, healthConditionSctid, healthConditionPt, medicalCoverageId,
		outpatientConsultationId, snomedSctid, snomedPt, createAsFinal, addObservationsCommand, patientId, patientGenderId,
		patientAge, SourceType.OUTPATIENT, textObservation, files);
	}

	@Override
	@Transactional
	public Integer createOdontologyServiceRequest(Integer doctorId, String categoryId, Integer institutionId, String healthConditionSctid,
												  String healthConditionPt, Integer medicalCoverageId, Integer outpatientConsultationId, String snomedSctid,
												  String snomedPt, Boolean createAsFinal, Optional<SharedAddObservationsCommandVo> addObservationsCommand,
												  Integer patientId, Short patientGenderId, Short patientAge)
	{
		return execute(doctorId, categoryId, institutionId, healthConditionSctid, healthConditionPt, medicalCoverageId,
				outpatientConsultationId, snomedSctid, snomedPt, createAsFinal, addObservationsCommand, patientId, patientGenderId,
				patientAge, SourceType.ODONTOLOGY, null, Collections.emptyList());
	}

	@Override
	@Transactional
	public Integer createNursingServiceRequest(Integer doctorId, String categoryId, Integer institutionId, String healthConditionSctid,
												  String healthConditionPt, Integer medicalCoverageId, Integer outpatientConsultationId, String snomedSctid,
												  String snomedPt, Boolean createAsFinal, Optional<SharedAddObservationsCommandVo> addObservationsCommand,
												  Integer patientId, Short patientGenderId, Short patientAge)
	{
		return execute(doctorId, categoryId, institutionId, healthConditionSctid, healthConditionPt, medicalCoverageId,
				outpatientConsultationId, snomedSctid, snomedPt, createAsFinal, addObservationsCommand, patientId, patientGenderId,
				patientAge, SourceType.NURSING, null, Collections.emptyList());
	}

	private Integer execute(Integer doctorId, String categoryId, Integer institutionId, String healthConditionSctid,
		String healthConditionPt, Integer medicalCoverageId, Integer outpatientConsultationId, String snomedSctid,
		String snomedPt, Boolean createAsFinal, Optional<SharedAddObservationsCommandVo> addObservationsCommand,
		Integer patientId, Short patientGenderId, Short patientAge, Short sourceTypeId, String textObservations,
		List<MultipartFile> files)
	{
		log.debug("execute -> institutionId {}, doctorId {}, categoryId {}, " +
						"medicalCoverageId {}, outpatientConsultationId {}, snomedSctid {}, snomedPt {}, " +
						 "healthConditionSctid {}, healthConditionPt {}, patientId {}, patientGenderId {}, patientAge {}", institutionId, doctorId, categoryId, medicalCoverageId, outpatientConsultationId, snomedSctid, snomedPt, healthConditionSctid, healthConditionPt, patientId, patientGenderId, patientAge
		);

		ServiceRequestBo serviceRequestBo = buildServiceRequestBo(institutionId, doctorId, categoryId, medicalCoverageId,
			outpatientConsultationId, snomedSctid, snomedPt, healthConditionSctid, healthConditionPt, patientId,
			patientGenderId, patientAge, sourceTypeId
		);

		Integer newServiceRequestId = createServiceRequestService.execute(serviceRequestBo);
		hospitalApiPublisher.publish(serviceRequestBo.getPatientId(), institutionId, getTopicToPublish(categoryId));

		//Fetch the diagnostic report created as part of the service request
		Integer newDiagnosticReportId = getCreatedDiagnosticReportId(newServiceRequestId, outpatientConsultationId);

		//If the procedures come with observations add them to the diagnostic report
		if (addObservationsCommand.isPresent()) {
			var command = addObservationsCommand.get();
			addObservations(newDiagnosticReportId, command, outpatientConsultationId, institutionId, patientId);
		}

		//Advance the diagnostic report's status if necessary
		if (createAsFinal) {
			transitionToFinal(newDiagnosticReportId, patientId, institutionId, textObservations, files);
		}

		log.debug("Output -> {}", newServiceRequestId);
		return newServiceRequestId;
	}

	/**
	 * Add observations to the newly created diagnostic report
	 */
	private void addObservations(Integer diagnosticReportId, SharedAddObservationsCommandVo addObservationsCommand,
		Integer outpatientConsultationId, Integer institutionId, Integer patientId) throws CreateOutpatientConsultationServiceRequestException {
		try {
			addDiagnosticReportObservations.run(diagnosticReportId, addObservationsCommand, institutionId, patientId);
		}
		catch (InvalidProcedureTemplateChangeException e) {
			throw translateException(e, outpatientConsultationId, diagnosticReportId);
		}
		catch (DiagnosticReportObservationException e) {
			throw translateException(e, outpatientConsultationId, diagnosticReportId);
		}
	}

	/**
	 * Advance the new diagnostic report to status=FINAL
	 *
	 * Same operation as ServiceRequestController#uploadFile followed by ServiceRequestController.complete.
	 * This two methods are called by the frontend:
	 *  1 First to upload the files of an already created diagnostic report. This operation returns the ids
	 *  of the stored files.
	 *  2 Then to mark the diagnostic report as final. This method also attaches the file ids of the
	 *  previous step.
	 */
	private void transitionToFinal(Integer diagnosticReportId, Integer patientId, Integer institutionId,
		String textObservation, List<MultipartFile> files) {

		var fileIds = uploadDiagnosticReportCompletedFileService.execute(files, diagnosticReportId, patientId);

		CompleteDiagnosticReportBo completeDiagnosticReportBo = CompleteDiagnosticReportBo
			.onlyObservations(textObservation);
		Integer result = completeDiagnosticReportService.run(patientId, diagnosticReportId,
			completeDiagnosticReportBo, institutionId);
		updateDiagnosticReportFileService.run(result, fileIds);
	}

	private Integer getCreatedDiagnosticReportId(Integer serviceRequestId, Integer outpatientConsultationId) throws CreateOutpatientConsultationServiceRequestException {
		var createdDiagnosticReports = serviceRequestStorage.getProceduresByServiceRequestIds(List.of(serviceRequestId));
		return createdDiagnosticReports
		.stream()
		.map(diagnosticReport -> diagnosticReport.getDiagnosticReportId())
		.findFirst()
		.orElseThrow(() ->
			CreateOutpatientConsultationServiceRequestException
			.diagnosticReportCreationFailed(serviceRequestId, outpatientConsultationId));
	}

	private ServiceRequestBo buildServiceRequestBo(
			Integer institutionId,
			Integer doctorId,
			String categoryId,
			Integer medicalCoverageId,
			Integer outpatientConsultationId,
			String snomedSctid,
			String snomedPt,
			String healthConditionSctid,
			String healthConditionPt,
			Integer patientId,
			Short patientGenderId,
			Short patientAge,
			Short sourceTypeId)
	{

		/**
		 * A single diagnostic report created from the consultation procedures
		 * The source type indicates the kind of consultation that led to
		 * the creation of the service request (outpatient , odontology, etc)
		 */
		var diagnosticReport = new DiagnosticReportBo();
		diagnosticReport.setSnomed(new SnomedBo(snomedSctid, snomedPt));
		diagnosticReport.setHealthConditionId(
			findHealthCondition(institutionId, patientId, healthConditionSctid, healthConditionPt)
		);

		var result = ServiceRequestBo.builder()
				.patientInfo(new PatientInfoBo(patientId, patientGenderId, patientAge))
				.categoryId(categoryId)
				.institutionId(institutionId)
				.doctorId(doctorId)
				.diagnosticReports(List.of(diagnosticReport))
				.requestDate(LocalDateTime.now())
				.associatedSourceTypeId(sourceTypeId)
				.associatedSourceId(outpatientConsultationId)
				.medicalCoverageId(medicalCoverageId)
				.observations(null)
				.build();

		log.debug("Output -> {}", result);
		return result;
	}

	/**
	 * Looks up the patient's health condition that matches the snomed term.
	 * The selected health condition must already exist.
	 * The possible health conditions are those assigned to the patient via outpatient consultations (the one being
	 * created during this use case, or others created before).
	 * The frontend sends the sctid, pt pair to indicate the health condition. It can't send the health condition
	 * id directly because the condition could be a new one assigned in the consultation being created.
	 *
	 */
	private Integer findHealthCondition(Integer institutionId, Integer patientId, String healthConditionSctid, String healthConditionPt) throws CreateOutpatientConsultationServiceRequestException {
		return sharedHealthConditionPort
		.getLatestHealthConditionByPatientIdAndInstitutionIdAndSnomedConcept(
			institutionId, patientId, healthConditionSctid, healthConditionPt
		).orElseThrow(() ->
			CreateOutpatientConsultationServiceRequestException
				.healthConditionNotFound(institutionId, patientId, healthConditionSctid, healthConditionPt)
		);
	}

	private EHospitalApiTopicDto getTopicToPublish(String categoryId) {
		if (categoryId.equals("108252007"))
			return EHospitalApiTopicDto.CLINIC_HISTORY__HOSPITALIZATION__SERVICE_RESQUEST__LABORATORY;
		if (categoryId.equals("363679005"))
			return EHospitalApiTopicDto.CLINIC_HISTORY__HOSPITALIZATION__SERVICE_RESQUEST__IMAGE;
		return EHospitalApiTopicDto.CLINIC_HISTORY__HOSPITALIZATION__SERVICE_RESQUEST;
	}

	private CreateOutpatientConsultationServiceRequestException translateException(InvalidProcedureTemplateChangeException e, Integer outpatientConsultationId, Integer diagnosticReportId) {
		return CreateOutpatientConsultationServiceRequestException
			.invalidDiagnosticReportTemplateChange(outpatientConsultationId, diagnosticReportId);
	}

	private CreateOutpatientConsultationServiceRequestException translateException(DiagnosticReportObservationException e, Integer outpatientConsultationId, Integer diagnosticReportId) {
		return CreateOutpatientConsultationServiceRequestException
			.diagnosticReportObservationException(e.getCode(), e.getDomainObjectName(), e.getDomainObjectId(), outpatientConsultationId);
	}


}
