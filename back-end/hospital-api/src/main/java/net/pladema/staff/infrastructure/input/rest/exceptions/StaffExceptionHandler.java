package net.pladema.staff.infrastructure.input.rest.exceptions;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import net.pladema.staff.application.saveprofessional.exceptions.CreateHealthcareProfessionalSpecialtyException;
import net.pladema.staff.application.saveprofessionallicensesnumber.exceptions.SaveProfessionalLicensesNumberException;
import net.pladema.staff.service.exceptions.HealthcareProfessionalException;
import net.pladema.staff.service.exceptions.HealthcareProfessionalSpecialtyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "net.pladema.staff")
public class StaffExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(StaffExceptionHandler.class);

    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler({ HealthcareProfessionalSpecialtyException.class })
    protected ApiErrorMessageDto handleHealthcareProfessionalSpecailtyException(HealthcareProfessionalSpecialtyException ex, Locale locale) {
        LOG.debug("InputValidationException exception -> {}", ex.getMessage());
        return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler({ HealthcareProfessionalException.class })
    protected ApiErrorMessageDto handleHealthcareProfessionalException(HealthcareProfessionalException ex, Locale locale) {
        LOG.debug("InputValidationException exception -> {}", ex.getMessage());
        return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler({CreateHealthcareProfessionalSpecialtyException.class })
    protected ApiErrorMessageDto handleCreateHealthcareProfessionalException(CreateHealthcareProfessionalSpecialtyException ex, Locale locale) {
        LOG.debug("InputValidationException exception -> {}", ex.getMessage());
        return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
    }

	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({SaveProfessionalLicensesNumberException.class })
	protected ApiErrorMessageDto handleSaveProfessionalLicensesNumberException(SaveProfessionalLicensesNumberException ex, Locale locale) {
		LOG.debug("InputValidationException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
	}

}

