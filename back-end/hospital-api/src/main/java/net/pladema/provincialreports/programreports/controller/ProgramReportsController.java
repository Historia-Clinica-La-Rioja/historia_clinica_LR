package net.pladema.provincialreports.programreports.controller;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import io.swagger.v3.oas.annotations.tags.Tag;

import net.pladema.provincialreports.programreports.repository.ProgramReportsQueryFactory;
import net.pladema.provincialreports.programreports.service.ProgramReportsExcelService;

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
@RequestMapping("programreports")
@Tag(name = "Reports - program", description = "Reportes de programa, program reports")
public class ProgramReportsController {
	private static final Logger logger = LoggerFactory.getLogger(ProgramReportsController.class);

	private final ProgramReportsExcelService excelService;
	private final ProgramReportsQueryFactory queryFactory;
	private final ReportExcelUtilsService excelUtilsService;

	public ProgramReportsController(ProgramReportsExcelService excelService, ProgramReportsQueryFactory queryFactory, ReportExcelUtilsService excelUtilsService) {
		this.excelService = excelService;
		this.queryFactory = queryFactory;
		this.excelUtilsService = excelUtilsService;
	}

	@GetMapping(value = "/{institutionId}/epidemiology-one")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<byte[]> getEpidemiologyOneExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
		logger.info("getEpidemiologyOneExcelReport started with institution id = {}, fromDate = {}, toDate = {}", institutionId, fromDate, toDate);

		try {
			String title = "Reporte de epidemiología - Vigilancia epidemiológica";
			String[] headers = {"Apellido y nombre", "Codificación", "Fecha de nacimiento", "Sexo", "Fecha", "Departamento", "Domicilio", "CIE10", "Documento", "Diagnóstico"};

			logger.debug("building epidemiology one excel report");
			IWorkbook wb = excelService.buildEpidemiologyOneExcel(title, headers, queryFactory.queryEpidemiologyOne(institutionId, fromDate, toDate), institutionId, fromDate, toDate);

			String filename = "Programas - Vigilancia epidemiológica - " + excelUtilsService.newGetPeriodForFilenameFromDates(fromDate, toDate) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating epidemiology one excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}

	@GetMapping(value = "/{institutionId}/epidemiology-two")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<byte[]> getEpidemiologyTwoExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
		logger.info("getEpidemiologyTwoExcelReport started with institution id = {}, fromDate = {}, toDate = {}", institutionId, fromDate, toDate);

		try {
			String title = "Reporte de epidemiología - Notificación colectiva";
			String[] headers = {"Diagnóstico/código", "Grupo de edad", "Total"};

			logger.debug("building epidemiology two excel report");
			IWorkbook wb = excelService.buildEpidemiologyTwoExcel(title, headers, queryFactory.queryEpidemiologyTwo(institutionId, fromDate, toDate), institutionId, fromDate, toDate);

			String filename = "Programas - Notificación colectiva de epidemiología - " + excelUtilsService.newGetPeriodForFilenameFromDates(fromDate, toDate) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating epidemiology two excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}

	@GetMapping(value = "/{institutionId}/recupero-general")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<byte[]> getRecuperoGeneralExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
		logger.info("getRecuperoGeneralExcelReport started with institution id = {}, fromDate = {}, toDate = {}", institutionId, fromDate, toDate);

		try {
			String title = "Reporte de Recupero - Obras sociales";
			String[] headers = {"Unidad Operativa", "Nombre de prestador", "DNI de prestador", "Fecha de atención", "Hora", "Cons. n°", "DNI de paciente", "Nombre de paciente", "Sexo", "Fecha de nacimiento", "Edad a fecha del turno", "Edad a hoy", "Obra/s social/es", "Domicilio", "Localidad", "Motivos", "Procedimientos", "Problemas", "Medicación", "Evolución"};

			logger.debug("building recupero general excel report");
			IWorkbook wb = excelService.buildRecuperoGeneralExcel(title, headers, queryFactory.queryRecuperoGeneral(institutionId, fromDate, toDate), institutionId, fromDate, toDate);

			String filename = "Programas - Recupero de obras sociales - " + excelUtilsService.newGetPeriodForFilenameFromDates(fromDate, toDate) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating recupero general excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}

	@GetMapping(value = "/{institutionId}/sumar-general")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<byte[]> getSumarGeneralExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
			@RequestParam (required = false) Integer clinicalSpecialtyId,
			@RequestParam (required = false) Integer doctorId) {
		logger.info("getSumarGeneralExcelReport started with institution id = {}, fromDate = {}, toDate = {}", institutionId, fromDate, toDate);

		try {
			String title = "Reporte de Sumar - General";
			String[] headers = {"Unidad Operativa", "Nombre de prestador", "DNI de prestador", "Fecha de atención", "Hora", "DNI de paciente", "Nombre de paciente", "Sexo",
					"Fecha de nacimiento", "Edad a fecha del turno", "Etnia", "Obra/s social/es", "Domicilio", "Barrio", "Localidad",
					"Presión sistólica (mmHg)", "Presión diastólica (mmHg)", "Presión arterial media (mmHg)", "Temperatura (°C)", "Frecuencia cardíaca (lpm)", "Frecuencia respiratoria (rpm)", "Sat. de hemoglobina con oxígeno (%)",
					"Altura (cm)", "Peso (kg)", "Índice de masa corporal (kg/m^2)", "Perímetro cefálico (cm)", "Motivos", "Procedimientos", "Problemas", "Medicación", "Evolución"};

			logger.debug("building sumar general excel report");
			IWorkbook wb = excelService.buildSumarGeneralExcel(title, headers, queryFactory.querySumarGeneral(institutionId, fromDate, toDate, clinicalSpecialtyId, doctorId), institutionId, fromDate, toDate);

			String filename = "Programas - Sumar general - " + excelUtilsService.newGetPeriodForFilenameFromDates(fromDate, toDate) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating sumar general excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}

	@GetMapping(value = "/{institutionId}/recupero-odontologico")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<byte[]> getRecuperoOdontologicoExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
		logger.info("getRecuperoOdontologicoExcelReport started with institution id = {}, fromDate = {}, toDate = {}", institutionId, fromDate, toDate);

		try {
			String title = "Reporte de Recupero - Odontológico";
			String[] headers = {"Unidad Operativa", "Prestador", "DNI", "Fecha de atención", "Hora", "DNI de paciente", "Nombre de paciente", "Sexo",
					"Fecha de nacimiento", "Edad a fecha del turno", "Obra/s social/es", "Domicilio", "Barrio", "Localidad", "Índice CPO - Permanentes", "Índice CEO - Temporales",
					"Motivos", "Procedimientos", "Procedimientos de odontología", "Problemas", "Diagnósticos de odontología"};

			logger.debug("building recupero odontologico excel report");
			IWorkbook wb = excelService.buildRecuperoOdontologicoExcel(title, headers, queryFactory.queryRecuperoOdontologico(institutionId, fromDate, toDate), institutionId, fromDate, toDate);

			String filename = "Programas - Sumar odontológico - " + excelUtilsService.newGetPeriodForFilenameFromDates(fromDate, toDate) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating recupero odontologico excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}

	@GetMapping(value = "/{institutionId}/sumar-odontologico")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<byte[]> getSumarOdontologicoExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
		logger.info("getSumarOdontologicoExcelReport started with institution id = {}, fromDate = {}, toDate = {}", institutionId, fromDate, toDate);

		try {
			String title = "Reporte de Sumar - Odontológico";
			String[] headers = {"Unidad Operativa", "Prestador", "DNI", "Fecha de atención", "Hora", "DNI de paciente", "Nombre de paciente", "Sexo",
					"Fecha de nacimiento", "Edad a fecha del turno", "Obra/s social/es", "Domicilio", "Barrio", "Localidad", "Índice CPO - Permanentes", "Índice CEO - Temporales",
					"Motivos", "Procedimientos", "Procedimientos de odontología", "Problemas", "Diagnósticos de odontología"};

			logger.debug("building sumar odontologico excel report");
			IWorkbook wb = excelService.buildSumarOdontologicoExcel(title, headers, queryFactory.querySumarOdontologico(institutionId, fromDate, toDate), institutionId, fromDate, toDate);

			String filename = "Programas - Sumar odontológico - " + excelUtilsService.newGetPeriodForFilenameFromDates(fromDate, toDate) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating sumar odontologico excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}
}
