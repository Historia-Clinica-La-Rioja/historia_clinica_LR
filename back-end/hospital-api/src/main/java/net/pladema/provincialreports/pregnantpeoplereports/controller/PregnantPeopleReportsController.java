package net.pladema.provincialreports.pregnantpeoplereports.controller;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import io.swagger.v3.oas.annotations.tags.Tag;

import net.pladema.provincialreports.pregnantpeoplereports.repository.PregnantPeopleReportsQueryFactory;
import net.pladema.provincialreports.pregnantpeoplereports.service.PregnantPeopleReportsExcelService;

import net.pladema.provincialreports.reportformat.domain.service.ReportExcelUtilsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("newpregnantpeoplereports")
@Tag(name = "Reports - pregnant people", description = "Reportes de persona gestante, pregnant people reports")
public class PregnantPeopleReportsController {
	private static final Logger logger = LoggerFactory.getLogger(PregnantPeopleReportsController.class);

	private final PregnantPeopleReportsExcelService excelService;
	private final PregnantPeopleReportsQueryFactory queryFactory;
	private final ReportExcelUtilsService excelUtilsService;

	public PregnantPeopleReportsController(PregnantPeopleReportsExcelService excelService, PregnantPeopleReportsQueryFactory queryFactory, ReportExcelUtilsService excelUtilsService) {
		this.excelService = excelService;
		this.queryFactory = queryFactory;
		this.excelUtilsService = excelUtilsService;
	}

	@GetMapping(value = "/{institutionId}/pregnant-attentions")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<byte[]> getPregnantAttentionsExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
		logger.info("getOlderAdultOutpatientExcelReport started with institution id = {}, fromDate = {}, toDate = {}", institutionId, fromDate, toDate);

		try {
			String title = "Reporte de atenciones a personas gestantes";
			String[] headers = {"Unidad operativa", "DNI de paciente", "Apellido y nombre", "Fecha de nacimiento", "Edad al momento del turno", "Domicilio", "Teléfono", "Localidad", "Fecha de atención", "Obra social", "Motivo", "Problemas", "Procedimientos", "Evolución"};

			logger.debug("building pregnant people attentions excel report");
			IWorkbook wb = excelService.buildPregnantAttentionsExcel(title, headers, queryFactory.queryPregnantAttentions(institutionId, fromDate, toDate), institutionId, fromDate, toDate);

			String filename = "Personas gestantes - Atenciones - " + excelUtilsService.newGetPeriodForFilenameFromDates(fromDate, toDate) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating pregnant people attentions excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}

	@GetMapping(value = "/{institutionId}/pregnant-controls")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<byte[]> getPregnantControlsExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
		logger.info("getPregnantControlsExcelReport started with institution id = {}, fromDate = {}, toDate = {}", institutionId, fromDate, toDate);

		try {
			String title = "Reporte de control de personas gestantes";
			String[] headers = {"DNI de paciente", "Apellido y nombre", "Fecha de nacimiento", "Edad a la fecha del turno", "Domicilio", "Teléfono", "Localidad", "N° de consultas", "Consultas"};

			logger.debug("building pregnant people controls excel report");
			IWorkbook wb = excelService.buildPregnantControlsExcel(title, headers, queryFactory.queryPregnantControls(institutionId, fromDate, toDate), institutionId, fromDate, toDate);

			String filename = "Personas gestantes - Controles - " + excelUtilsService.newGetPeriodForFilenameFromDates(fromDate, toDate) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating pregnant people controls excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}
}
