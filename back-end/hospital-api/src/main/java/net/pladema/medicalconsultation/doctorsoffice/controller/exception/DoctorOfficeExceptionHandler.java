package net.pladema.medicalconsultation.doctorsoffice.controller.exception;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import net.pladema.medicalconsultation.doctorsoffice.service.exception.DoctorOfficeDescriptionException;
import net.pladema.medicalconsultation.doctorsoffice.service.exception.DoctorOfficeInstitutionIdException;
import net.pladema.medicalconsultation.doctorsoffice.service.exception.DoctorOfficeSectorIdException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "net.pladema.medicalconsultation.doctorsoffice")
public class DoctorOfficeExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(DoctorOfficeExceptionHandler.class);

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ DoctorOfficeSectorIdException.class })
	protected ApiErrorMessageDto handleDoctorOfficeSectorIdException(DoctorOfficeSectorIdException ex) {
		LOG.debug("DoctorOfficeSectorIdException exception -> {}", ex.getMessage());
		//return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ DoctorOfficeDescriptionException.class })
	protected ApiErrorMessageDto handleDoctorOfficeDescriptionException(DoctorOfficeDescriptionException ex) {
		LOG.debug("DoctorOfficeDescriptionException exception -> {}", ex.getMessage());
		//return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ DoctorOfficeInstitutionIdException.class })
	protected ApiErrorMessageDto handleDoctorOfficeInstitutionIdException(DoctorOfficeInstitutionIdException ex) {
		LOG.debug("DoctorOfficeInstitutionIdException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}
}
