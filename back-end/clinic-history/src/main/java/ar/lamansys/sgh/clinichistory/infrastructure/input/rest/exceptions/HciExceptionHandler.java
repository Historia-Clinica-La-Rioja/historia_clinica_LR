package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.exceptions;

import ar.lamansys.sgh.clinichistory.application.fetchdocumentfile.exceptions.FetchDocumentFileException;
import ar.lamansys.sgh.clinichistory.application.getanthropometricgraphicdata.exceptions.GetAnthropometricGraphicDataException;
import ar.lamansys.sgh.clinichistory.application.rebuildFile.exceptions.RebuildFileException;
import ar.lamansys.sgh.clinichistory.application.signDocumentFile.exceptions.SignDocumentFileException;
import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import ar.lamansys.sgh.clinichistory.application.calculatecie10.exceptions.HCICIE10Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "ar.lamansys.sgh.clinichistory")
public class HciExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(HciExceptionHandler.class);

	@ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
	@ExceptionHandler({ HCICIE10Exception.class })
	protected ApiErrorMessageDto handleHCICIE10Exception(HCICIE10Exception ex) {
		LOG.error("HCICIE10Exception exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
	@ExceptionHandler({ FetchDocumentFileException.class })
	protected ApiErrorMessageDto handleFetchDocumentFileException(FetchDocumentFileException ex) {
		LOG.error("FetchDocumentFileException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ SignDocumentFileException.class })
	protected ApiErrorMessageDto handleSignDocumentFileException(SignDocumentFileException ex) {
		LOG.error("SignDocumentFileException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ RebuildFileException.class })
	protected ApiErrorMessageDto handleRebuildFileException(RebuildFileException ex) {
		LOG.error("RebuildFileException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ GetAnthropometricGraphicDataException.class })
	protected ApiErrorMessageDto handleGetAnthropometricGraphicDataException(GetAnthropometricGraphicDataException ex) {
		LOG.error("RebuildFileException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}
}

