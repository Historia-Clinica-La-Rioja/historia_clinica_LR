package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.exceptions;

import ar.lamansys.sgh.clinichistory.application.anestheticreport.exceptions.AnestheticReportException;
import ar.lamansys.sgh.clinichistory.application.fetchdocumentfile.exceptions.FetchDocumentFileException;
import ar.lamansys.sgh.clinichistory.application.getanthropometricgraphicdata.exceptions.GetAnthropometricGraphicDataException;
import ar.lamansys.sgh.clinichistory.application.isolationalerts.exceptions.IsolationAlertException;
import ar.lamansys.sgh.clinichistory.application.rebuildFile.exceptions.RebuildFileException;
import ar.lamansys.sgh.clinichistory.application.saveMedicationStatementInstitutionalSupply.exception.SaveMedicationStatementInstitutionalSupplyException;
import ar.lamansys.sgh.clinichistory.application.signDocumentFile.exceptions.SignDocumentFileException;
import ar.lamansys.sgh.clinichistory.domain.document.exceptions.DocumentException;
import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import ar.lamansys.sgh.clinichistory.application.calculatecie10.exceptions.HCICIE10Exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "ar.lamansys.sgh.clinichistory")
public class HciExceptionHandler {

	@ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
	@ExceptionHandler({ HCICIE10Exception.class })
	protected ApiErrorMessageDto handleHCICIE10Exception(HCICIE10Exception ex) {
		log.error("HCICIE10Exception exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
	@ExceptionHandler({ FetchDocumentFileException.class })
	protected ApiErrorMessageDto handleFetchDocumentFileException(FetchDocumentFileException ex) {
		log.error("FetchDocumentFileException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ SignDocumentFileException.class })
	protected ApiErrorMessageDto handleSignDocumentFileException(SignDocumentFileException ex) {
		log.error("SignDocumentFileException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ RebuildFileException.class })
	protected ApiErrorMessageDto handleRebuildFileException(RebuildFileException ex) {
		log.error("RebuildFileException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ DocumentException.class })
	protected ApiErrorMessageDto handleDocumentException(DocumentException ex) {
		log.error("DocumentException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ AnestheticReportException.class })
	protected ApiErrorMessageDto handleAnestheticReportException(AnestheticReportException ex) {
		log.error("AnestheticReportException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ GetAnthropometricGraphicDataException.class })
	protected ApiErrorMessageDto handleGetAnthropometricGraphicDataException(GetAnthropometricGraphicDataException ex) {
		log.error("GetAnthropometricGraphicDataException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ SaveMedicationStatementInstitutionalSupplyException.class })
	protected ApiErrorMessageDto handleSaveMedicationStatementInstitutionalSupplyException(SaveMedicationStatementInstitutionalSupplyException ex) {
		log.error("SaveMedicationStatementInstitutionalSupplyException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ IsolationAlertException.class })
	protected ApiErrorMessageDto handleIsolationAlertException(IsolationAlertException ex) {
		log.error("IsolationAlertException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

}

