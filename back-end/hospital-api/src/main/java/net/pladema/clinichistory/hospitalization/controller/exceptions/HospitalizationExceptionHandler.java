package net.pladema.clinichistory.hospitalization.controller.exceptions;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.CreateInternmentEpisodeException;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.InternmentDocumentException;
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

}

