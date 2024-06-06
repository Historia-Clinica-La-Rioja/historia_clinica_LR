package net.pladema.reports.controller.exceptions;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import lombok.extern.slf4j.Slf4j;

import net.pladema.hsi.addons.billing.infrastructure.input.exception.BillProceduresExternalServiceException;
import net.pladema.medicalconsultation.diary.service.exception.DiaryNotFoundException;

import net.pladema.reports.service.exception.AnnexReportException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice(basePackages = "net.pladema.reports")
@Slf4j
public class ReportsExceptionHandler {

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({AnnexReportException.class})
	protected ApiErrorMessageDto handleAnnexReportException(AnnexReportException ex, Locale locale) {
		log.debug("AnnexReportException exception -> {}#{}#{}", ex.getCode(), ex.getReason(), ex.getErrorDetails());
		return new ApiErrorMessageDto(ex.getCode(), ex.getReason());
	}
}
