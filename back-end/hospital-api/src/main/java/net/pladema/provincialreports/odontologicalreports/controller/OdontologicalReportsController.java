package net.pladema.provincialreports.odontologicalreports.controller;


import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import io.swagger.v3.oas.annotations.tags.Tag;

import net.pladema.provincialreports.odontologicalreports.repository.OdontologicalReportsQueryFactory;

import net.pladema.provincialreports.odontologicalreports.service.OdontologicalReportsExcelService;
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
@RequestMapping("odontologicalreports")
@Tag(name = "Reports - odontological", description = "Reportes odontológicos, odontological reports")
public class OdontologicalReportsController {
	private static final Logger logger = LoggerFactory.getLogger(OdontologicalReportsController.class);

	private final OdontologicalReportsExcelService excelService;
	private final OdontologicalReportsQueryFactory queryFactory;
	private final ReportExcelUtilsService excelUtilsService;

	public OdontologicalReportsController(OdontologicalReportsExcelService excelService, OdontologicalReportsQueryFactory queryFactory, ReportExcelUtilsService excelUtilsService) {
		this.excelService = excelService;
		this.queryFactory = queryFactory;
		this.excelUtilsService = excelUtilsService;
	}

	@GetMapping(value = "/{institutionId}/promocion-primer-nivel")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<byte[]> getFirstLevelPromotionExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
		logger.info("getFirstLevelPromotionExcelReport started with institution id = {}, fromDate = {}, toDate = {}", institutionId, fromDate, toDate);

		try {
			String title = "Reporte de odontología - Promoción de primer nivel";
			String[] headers = {"Profesional", "Procedimiento", "Total"};

			logger.debug("building first level promotion excel report");
			IWorkbook wb = excelService.buildOdontologyExcel(title, headers, queryFactory.queryFirstLevelPromotion(institutionId, fromDate, toDate), institutionId, fromDate, toDate);

			String filename = "Odontología - Promoción de primer nivel - " + excelUtilsService.newGetPeriodForFilenameFromDates(fromDate, toDate) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating first level promotion excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}

	@GetMapping(value = "/{institutionId}/prevencion-primer-nivel")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<byte[]> getFirstLevelPreventionExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
		logger.info("getFirstLevelPreventionExcelReport started with institution id = {}, fromDate = {}, toDate = {}", institutionId, fromDate, toDate);

		try {
			String title = "Reporte de odontología - Prevención de primer nivel";
			String[] headers = {"Profesional", "Procedimiento", "Total"};

			logger.debug("building first level prevention excel report");
			IWorkbook wb = excelService.buildOdontologyExcel(title, headers, queryFactory.queryFirstLevelPrevention(institutionId, fromDate, toDate), institutionId, fromDate, toDate);

			String filename = "Odontología - Prevención de primer nivel - " + excelUtilsService.newGetPeriodForFilenameFromDates(fromDate, toDate) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating first level prevention excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}

	@GetMapping(value = "/{institutionId}/prevencion-grupal-primer-nivel")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<byte[]> getFirstLevelGroupPreventionExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
		logger.info("getFirstLevelGroupPreventionExcelReport started with institution id = {}, fromDate = {}, toDate = {}", institutionId, fromDate, toDate);

		try {
			String title = "Reporte de odontología - Prevención de primer nivel";
			String[] headers = {"Profesional", "Procedimiento", "Total"};

			logger.debug("building first level group prevention excel report");
			IWorkbook wb = excelService.buildOdontologyExcel(title, headers, queryFactory.queryFirstLevelGroupPrevention(institutionId, fromDate, toDate), institutionId, fromDate, toDate);

			String filename = "Odontología - Prevención grupal de primer nivel - " + excelUtilsService.newGetPeriodForFilenameFromDates(fromDate, toDate) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating first level group prevention excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}

	@GetMapping(value = "/{institutionId}/operatoria-segundo-nivel")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<byte[]> getSecondLevelOperationExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
		logger.info("getSecondLevelOperationExcelReport started with institution id = {}, fromDate = {}, toDate = {}", institutionId, fromDate, toDate);

		try {
			String title = "Reporte de odontología - Operatoria de segundo nivel";
			String[] headers = {"Profesional", "Procedimiento", "Total"};

			logger.debug("building second level operation excel report");
			IWorkbook wb = excelService.buildOdontologyExcel(title, headers, queryFactory.querySecondLevelOperation(institutionId, fromDate, toDate), institutionId, fromDate, toDate);

			String filename = "Odontología - Operatoria de segundo nivel - " + excelUtilsService.newGetPeriodForFilenameFromDates(fromDate, toDate) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating second level operation excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}

	@GetMapping(value = "/{institutionId}/endodoncia-segundo-nivel")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<byte[]> getSecondLevelEndodonticsExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
		logger.info("getSecondLevelEndodonticsExcelReport started with institution id = {}, fromDate = {}, toDate = {}", institutionId, fromDate, toDate);

		try {
			String title = "Reporte de odontología - Endodoncia segundo nivel";
			String[] headers = {"Profesional", "Procedimiento", "Total"};

			logger.debug("building second level endodontics excel report");
			IWorkbook wb = excelService.buildOdontologyExcel(title, headers, queryFactory.querySecondLevelEndodontics(institutionId, fromDate, toDate), institutionId, fromDate, toDate);

			String filename = "Odontología - Endodoncia de segundo nivel - " + excelUtilsService.newGetPeriodForFilenameFromDates(fromDate, toDate) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating second level operation excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}

	@GetMapping(value = "/{institutionId}/odontological-procedures")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<byte[]> getOdontologicalProceduresExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
		logger.info("getOdontologicalProceduresExcelReport started with institution id = {}, fromDate = {}, toDate = {}", institutionId, fromDate, toDate);

		try {
			String title = "Reporte de odontología - Procedimientos odontológicos";
			String[] headers = {"Nombre de profesional", "DNI", "Matrícula", "Fecha de atención", "Hora", "Nombre de paciente", "DNI", "Sexo", "Fecha de nacimiento", "Edad a fecha de turno", "Obra social", "Domicilio", "Localidad", "CPO permanentes", "CEO permanentes", "Motivos", "Otros diagnósticos", "Otros procedimientos", "Alergias e intolerancias", "Medicación habitual", "Diagnósticos dentales", "Procedimientos dentales", "Evolución"};

			logger.debug("building odontological procedures excel report");
			IWorkbook wb = excelService.buildOdontologicalProceduresExcel(title, headers, queryFactory.queryOdontologicalProcedures(institutionId, fromDate, toDate), institutionId, fromDate, toDate);

			String filename = "Odontología - Procedimientos odontológicos - " + excelUtilsService.newGetPeriodForFilenameFromDates(fromDate, toDate) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating second level operation excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}
}
