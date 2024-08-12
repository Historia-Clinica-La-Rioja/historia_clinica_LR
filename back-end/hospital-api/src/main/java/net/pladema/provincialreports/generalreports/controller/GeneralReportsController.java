package net.pladema.provincialreports.generalreports.controller;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import io.swagger.v3.oas.annotations.tags.Tag;

import net.pladema.provincialreports.generalreports.repository.NewGeneralReportsQueryFactory;
import net.pladema.provincialreports.generalreports.service.NewGeneralReportsExcelService;

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
import java.time.LocalTime;

@RestController
@RequestMapping("newgeneralreports")
@Tag(name = "General reports", description = "Reportes generales")
public class GeneralReportsController {
	private static final Logger logger = LoggerFactory.getLogger(GeneralReportsController.class);

	private final NewGeneralReportsExcelService excelService;
	private final ReportExcelUtilsService excelUtilsService;
	private final NewGeneralReportsQueryFactory queryFactory;

	public GeneralReportsController(NewGeneralReportsExcelService excelService, ReportExcelUtilsService excelUtilsService, NewGeneralReportsQueryFactory queryFactory) {
		this.excelService = excelService;
		this.excelUtilsService = excelUtilsService;
		this.queryFactory = queryFactory;
	}

	@GetMapping(value = "/{institutionId}/emergency")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<byte[]> getEmergencyExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
		logger.info("getEmergencyExcelReport started with institution id {}, fromDate {}, toDate {}", institutionId, fromDate, toDate);

		try {
			String title = "Reporte de emergencias diarias";
			String[] headers = {"DNI de paciente", "Apellido(s)", "Nombre(s)", "Obra social", "Fecha de atención", "Hora de atención", "Medio de ingreso", "Ambulancia", "Oficina", "Sector", "Estado", "Tipo", "Notas de triage", "Triage", "Fecha de alta", "Ambulancia de alta", "Tipo de alta", "Salida", "Intervención policial"};

			IWorkbook wb = excelService.buildEmergencyExcel(title, headers, queryFactory.queryEmergency(institutionId, fromDate, toDate), institutionId, fromDate, toDate);

			String filename = "Emergencias diarias - " + excelUtilsService.newGetPeriodForFilenameFromDates(fromDate, toDate) + "." + wb.getExtension();

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("Error generating emergency excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("Error generating report", e);
		}
	}
}
