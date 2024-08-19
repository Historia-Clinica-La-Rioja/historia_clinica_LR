package net.pladema.hl7.dataexchange.procedures;

import ar.lamansys.sgh.shared.infrastructure.input.service.observation.FhirObservationGroupInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.observation.FhirObservationInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.observation.FhirQuantityInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.observation.SharedObservationPort;
import net.pladema.hl7.dataexchange.IResourceFhir;
import net.pladema.hl7.dataexchange.exceptions.DiagnosticReportException;
import net.pladema.hl7.dataexchange.exceptions.DiagnosticReportExceptionEnum;
import net.pladema.hl7.dataexchange.model.domain.CoverageVo;
import net.pladema.hl7.dataexchange.model.domain.DiagnosticReportVo;
import net.pladema.hl7.dataexchange.model.domain.ObservationVo;
import net.pladema.hl7.dataexchange.model.domain.PatientVo;
import net.pladema.hl7.dataexchange.model.domain.PractitionerVo;
import net.pladema.hl7.dataexchange.model.domain.ServiceRequestVo;
import net.pladema.hl7.supporting.conformance.InteroperabilityCondition;

import net.pladema.hl7.supporting.exchange.database.FhirPersistentStore;

import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Conditional(InteroperabilityCondition.class)
public class DiagnosticReportResource extends IResourceFhir {

	private final SharedObservationPort sharedObservationPort;

	protected DiagnosticReportResource(FhirPersistentStore store, SharedObservationPort sharedObservationPort) {
		super(store);
		this.sharedObservationPort = sharedObservationPort;
	}

	@Override
	public ResourceType getResourceType() {
		return null;
	}

	public void saveDiagnosticReport(DiagnosticReportVo diagnosticReportVo) {
		List<FhirObservationInfoDto> fhirObservationInfoDtoList = new ArrayList<>();
		for (ObservationVo observation : diagnosticReportVo.getObservations()) {
			FhirObservationInfoDto observationInfoDto = new FhirObservationInfoDto();
			observationInfoDto.setLoincCode(observation.getCode());
			if (observation.getValueString() != null)
				observationInfoDto.setValue(observation.getValueString());
			if (observation.hasQuantity()) {
				FhirQuantityInfoDto fhirQuantityInfoDto = new FhirQuantityInfoDto();
				fhirQuantityInfoDto.setValue(observation.getQuantityValue());
				fhirQuantityInfoDto.setUnit(observation.getQuantityUnit());
				observationInfoDto.setQuantity(fhirQuantityInfoDto);
			}
			fhirObservationInfoDtoList.add(observationInfoDto);
		}

		FhirObservationGroupInfoDto fhirObservationGroupInfoDto = new FhirObservationGroupInfoDto();
		fhirObservationGroupInfoDto.setPatientId(Integer.parseInt(diagnosticReportVo.getPatientId()));
		fhirObservationGroupInfoDto.setObservations(fhirObservationInfoDtoList);
		fhirObservationGroupInfoDto.setDiagnosticReportId(Integer.parseInt(diagnosticReportVo.getDiagnosticReportId()));

		sharedObservationPort.save(fhirObservationGroupInfoDto);
		store.setDiagnosticReportStatus(diagnosticReportVo.getDiagnosticReportUuid(), diagnosticReportVo.getStatusId());
	}

	public void validateDiagnosticReport(DiagnosticReportVo diagnosticReport) {
		ServiceRequestVo serviceRequest = store.getServiceRequestDataForValidation(diagnosticReport.getServiceRequestVo().getServiceRequestUuid(),
				diagnosticReport.getDiagnosticReportUuid());

		if (serviceRequest == null)
			throw new DiagnosticReportException(DiagnosticReportExceptionEnum.REQUEST_NOT_MATCH, HttpStatus.BAD_REQUEST, "No existe, o no coincide el pedido con el procedimiento o diagnostico.");

		diagnosticReport.setDiagnosticReportId(serviceRequest.getDiagnosticReportId().toString());
		diagnosticReport.setServiceRequestId(serviceRequest.getSrId().toString());

		PatientVo patient = diagnosticReport.getPatientVo();
		if (/*!serviceRequest.getPatientId().equals(patient.getId()) ||*/ !serviceRequest.getPatientIdentificationNumber().equals(patient.getIdentificationNumber()))
			throw new DiagnosticReportException(DiagnosticReportExceptionEnum.PATIENT_NOT_MATCH, HttpStatus.BAD_REQUEST, "No coincide el paciente para la dispensa con el de la receta.");
		CoverageVo coverage = diagnosticReport.getCoverageVo();
		if (coverage != null && serviceRequest.getMedicalCoverageId() != null) {
			if (!serviceRequest.getMedicalCoverageId().equals(coverage.getMedicalCoverageId()))
				throw new DiagnosticReportException(DiagnosticReportExceptionEnum.COVERAGE_NOT_MATCH, HttpStatus.BAD_REQUEST, "No coincide la cobertura del pedido con la del procedimiento o diagnostico.");
			if ((serviceRequest.getCoverageAffiliateNumber() != null) && (!serviceRequest.getCoverageAffiliateNumber().equals(coverage.getAffiliateNumber())))
				throw new DiagnosticReportException(DiagnosticReportExceptionEnum.COVERAGE_AFFILIATE_NUMBER_NOT_MATCH, HttpStatus.BAD_REQUEST, "No coincide el numero de afiliado de la cobertura del pedido con la del procedimiento o diagnostico.");
		}
		/*PractitionerVo practitioner = diagnosticReport.getPractitionerVo();
		if (practitioner == null || serviceRequest.getDoctorId() == null)
			throw new DiagnosticReportException(DiagnosticReportExceptionEnum.PRACTITIONER_NOT_NULL, HttpStatus.BAD_REQUEST, "El profesional tiene que estar tanto en el pedido como en el procedimiento o diagnostico.");
		if (!practitioner.getId().equals(serviceRequest.getDoctorId().toString()))
			throw new DiagnosticReportException(DiagnosticReportExceptionEnum.PRACTITIONER_NOT_MATCH, HttpStatus.BAD_REQUEST, "No coincide el profesional del pedido con el del procedimiento o diagnostico.");*/

		validateStateTransition(serviceRequest.getDiagnosticReportStatus(),serviceRequest.getServiceRequestStatus());
	}

	private void validateStateTransition(String diagnosticReportStatus, String serviceRequestStatus) {
		switch (serviceRequestStatus) {
			case ("255594003"):
				throw new DiagnosticReportException(DiagnosticReportExceptionEnum.TRANSITION_STATE_INVALID, HttpStatus.BAD_REQUEST, "El pedido ya fue completado.");
			case ("723510000"):
				throw new DiagnosticReportException(DiagnosticReportExceptionEnum.TRANSITION_STATE_INVALID, HttpStatus.BAD_REQUEST, "El pedido fue ingresado por error.");
		}
		switch (diagnosticReportStatus) {
			case ("89925002"):
				throw new DiagnosticReportException(DiagnosticReportExceptionEnum.TRANSITION_STATE_INVALID, HttpStatus.BAD_REQUEST, "El procedimiento o diagnostico fue cancelado.");
			case ("723510000"):
				throw new DiagnosticReportException(DiagnosticReportExceptionEnum.TRANSITION_STATE_INVALID, HttpStatus.BAD_REQUEST, "El procedimiento o diagnostico fue ingresado por error.");
			case ("261782000"):
				throw new DiagnosticReportException(DiagnosticReportExceptionEnum.TRANSITION_STATE_INVALID, HttpStatus.BAD_REQUEST, "El procedimiento o diagnostico ya tiene un registro final y completo.");
		}
	}

	public static DiagnosticReportVo encode(Resource baseResource) {
		DiagnosticReport resource = (DiagnosticReport) baseResource;
		DiagnosticReportVo data = new DiagnosticReportVo();

		data.setId(resource.getIdElement().getIdPart());

		String statusDisplay = resource.getStatus().getDisplay();
		data.setStatus(statusDisplay);
		data.setStatusId(getStatusId(statusDisplay));

		data.setCategoryCode(resource.getCategoryFirstRep().getText());
		data.setCode(resource.getCode().getCodingFirstRep().getCode());
		data.setPatientId(resource.getSubject().getReferenceElement().getIdPart());
		data.setDoctorId(resource.getPerformerFirstRep().getReferenceElement().getIdPart());
		data.setDiagnosticReportUuid(UUID.fromString(resource.getBasedOnFirstRep().getReferenceElement().getIdPart()));
		
		data.setConclusion(resource.getConclusion());
		data.setConclusionCode(resource.getConclusionCodeFirstRep().getCodingFirstRep().getCode());

		return data;
	}

	private static String getStatusId(String statusDisplay) {
		switch (statusDisplay) {
			case ("Registered"):
				return "1";
			case ("Partial"):
				return "255609007";
			case ("Final"):
				return "261782000";
			case ("Corrected"):
				return "33714007";
			case ("Appended"):
				return "18403000";
			case ("Cancelled"):
				return "89925002";
			case ("Entered in Error"):
				return "723510000";
			default:
				return null;
		}
	}
}
