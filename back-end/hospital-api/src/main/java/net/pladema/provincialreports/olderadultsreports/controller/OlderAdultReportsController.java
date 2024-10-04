package net.pladema.provincialreports.olderadultsreports.controller;

import java.time.LocalDate;

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

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.provincialreports.olderadultsreports.repository.OlderAdultReportsQueryFactory;
import net.pladema.provincialreports.olderadultsreports.service.OlderAdultReportsExcelService;
import net.pladema.provincialreports.reportformat.domain.service.ReportExcelUtilsService;

@RestController
@RequestMapping("olderadultsreports")
@Tag(name = "Reports - older adult", description = "Reportes de adulto mayor, older adult reports")
public class OlderAdultReportsController {
	private static final Logger logger = LoggerFactory.getLogger(OlderAdultReportsController.class);

	private final OlderAdultReportsExcelService excelService;
	private final OlderAdultReportsQueryFactory queryFactory;
	private final ReportExcelUtilsService excelUtilsService;

	public OlderAdultReportsController(OlderAdultReportsExcelService excelService, OlderAdultReportsQueryFactory queryFactory, ReportExcelUtilsService excelUtilsService) {
		this.excelService = excelService;
		this.queryFactory = queryFactory;
		this.excelUtilsService = excelUtilsService;
	}

	@GetMapping(value = "/{institutionId}/older-adults-outpatient")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<byte[]> getOlderAdultOutpatientExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
		logger.info("getOlderAdultOutpatientExcelReport started with institution id = {}, fromDate = {}, toDate = {}", institutionId, fromDate, toDate);

		try {
			String title = "Reporte de adulto mayor - Ambulatoria";
			String[] headers = {"Nombre de prestador", "DNI de prestador", "Fecha de atención", "Hora", "Número de consulta", "Nombre de paciente", "DNI de paciente", "Sexo", "Fecha de nacimiento", "Edad a fecha del turno", "Obra(s) social(es)", "Domicilio", "Localidad", "Teléfono", "Problemas"};

			logger.debug("building older adult outpatient excel report");
			IWorkbook wb = excelService.buildOlderAdultOutpatientExcel(title, headers, queryFactory.queryOlderAdultOutpatient(institutionId, fromDate, toDate), institutionId, fromDate, toDate);

			String filename = "Adulto mayor - Ambulatoria - " + excelUtilsService.getPeriodForFilenameFromDates(fromDate, toDate) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating older adult outpatient excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}

	@GetMapping(value = "/{institutionId}/older-adults-hospitalization")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<byte[]> getOlderAdultHospitalizationExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
		logger.info("getOlderAdultHospitalizationExcelReport started with institution id = {}, fromDate = {}, toDate = {}", institutionId, fromDate, toDate);

		try {
			String title = "Reporte de adulto mayor - Internación";
			String[] headers = {"Nombre de paciente", "DNI de paciente", "Género", "Fecha de nacimiento", "Edad a fecha del turno", "Teléfono", "Ingreso", "Alta probable", "Cama", "Categoría", "Habitación", "Sector", "Alta", "Problemas"};

			logger.debug("building older adult hospitalization excel report");
			IWorkbook wb = excelService.buildOlderAdultHospitalizationExcel(title, headers, queryFactory.queryOlderAdultHospitalization(institutionId, fromDate, toDate), institutionId, fromDate, toDate);

			String filename = "Adulto mayor - Internación - " + excelUtilsService.getPeriodForFilenameFromDates(fromDate, toDate) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating older adult hospitalization excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}

	@GetMapping(value = "/{institutionId}/polypharmacy")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<byte[]> getPolypharmacyExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
		logger.info("getPolypharmacyExcelReport started with institution id = {}, fromDate = {}, toDate = {}", institutionId, fromDate, toDate);

		try {
			String title = "Reporte de adulto mayor - Polifarmacia";
			String[] headers = {"Nombre de paciente", "DNI de paciente", "Sexo", "Edad a fecha de atención", "Nombre de prestador", "DNI de prestador", "Especialidad de prestador", "Fecha de atención", "Problema asociado", "¿Crónico?", "Tipo de origen", "SNOMED", "Medicación", "Obra social"};

			logger.debug("building polypharmacy excel report");
			IWorkbook wb = excelService.buildPolypharmacyExcel(title, headers, queryFactory.queryPolypharmacy(institutionId, fromDate, toDate), institutionId, fromDate, toDate);

			String filename = "Adulto mayor - Polifarmacia - " + excelUtilsService.getPeriodForFilenameFromDates(fromDate, toDate) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating polypharmacy excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}
}
