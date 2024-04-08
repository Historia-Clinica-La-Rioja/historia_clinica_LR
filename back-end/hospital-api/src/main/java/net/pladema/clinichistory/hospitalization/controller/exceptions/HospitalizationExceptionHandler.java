package net.pladema.clinichistory.hospitalization.controller.exceptions;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.CreateInternmentEpisodeException;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.GeneratePdfException;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.InternmentDocumentException;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.InternmentEpisodeNotFoundException;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.MoreThanOneConsentDocumentException;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.PatientNotFoundException;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.PersonNotFoundException;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.SaveMedicalDischargeException;
import net.pladema.sgx.exceptions.PermissionDeniedException;
import net.pladema.clinichistory.hospitalization.service.servicerequest.exception.CreateInternmentServiceRequestException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = {"net.pladema.clinichistory.hospitalization"})
public class HospitalizationExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(HospitalizationExceptionHandler.class);

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({CreateInternmentEpisodeException.class})
	protected ApiErrorMessageDto handleCreateInternmentEpisodeException(CreateInternmentEpisodeException ex) {
		LOG.debug("CreateInternmentEpisodeException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({CreateInternmentServiceRequestException.class})
	protected ApiErrorMessageDto handleCreateInternmentEpisodeException(CreateInternmentServiceRequestException ex) {
		LOG.debug("CreateInternmentServiceRequestException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({SaveMedicalDischargeException.class})
	protected ApiErrorMessageDto handleSaveMedicalDischargeException(SaveMedicalDischargeException ex) {
		LOG.debug("SaveMedicalDischargeException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
	}
	
	@ExceptionHandler({PermissionDeniedException.class})
	public ApiErrorMessageDto permissionDenied(PermissionDeniedException ex) {
		LOG.debug("PermissionDeniedException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(null , ex.getMessage());
	}

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({InternmentDocumentException.class})
	protected ApiErrorMessageDto handleInternmentDocumentException(InternmentDocumentException ex) {
		LOG.debug("InternmentDocumentException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({GeneratePdfException.class})
	protected ApiErrorMessageDto handleGeneratePdfException(GeneratePdfException ex) {
		LOG.debug("GeneratePdfException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto("generate-pdf-fail", ex.getMessage());
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({PatientNotFoundException.class})
	protected ApiErrorMessageDto handlePatientNotFoundException(PatientNotFoundException ex) {
		LOG.debug("PatientNotFoundException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto("patient-not-found", "Paciente no encontrado");
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({PersonNotFoundException.class})
	protected ApiErrorMessageDto handlePersonNotFoundException(PersonNotFoundException ex) {
		LOG.debug("PersonNotFoundException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto("person-not-found", "Persona no encontrado");
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({InternmentEpisodeNotFoundException.class})
	protected ApiErrorMessageDto handleInternmentNotFoundException(InternmentEpisodeNotFoundException ex) {
		LOG.debug("InternmentNotFoundException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto("internment-episode-not-found", "Episodio de internación no encontrado");
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({MoreThanOneConsentDocumentException.class})
	protected ApiErrorMessageDto handleMoreThanOneConsentDocumentException(MoreThanOneConsentDocumentException ex) {
		LOG.debug("MoreThanOneConsentDocumentException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto("more-than-one-consent-document", "El paciente ya tiene un documento de ese tipo");
	}

}

