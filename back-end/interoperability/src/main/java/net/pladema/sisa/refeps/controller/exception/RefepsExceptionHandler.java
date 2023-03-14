package net.pladema.sisa.refeps.controller.exception;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;

import net.pladema.sisa.refeps.services.exceptions.RefepsLicenseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "net.pladema.staff.infrastructure")
public class RefepsExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(RefepsExceptionHandler.class);

	@ExceptionHandler({ RefepsLicenseException.class })
	protected ResponseEntity<ApiErrorMessageDto> handleRefepsLicenseException(RefepsLicenseException ex) {
		LOG.debug("FefepsLicenseException exception -> {}", ex.getMessage());
		return new ResponseEntity<>(new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage()), HttpStatus.NOT_FOUND);
	}

}
