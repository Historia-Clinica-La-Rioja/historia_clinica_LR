package net.pladema.provincialreports.nursingreports.controller;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import io.swagger.v3.oas.annotations.tags.Tag;

import net.pladema.provincialreports.nursingreports.repository.NursingReportsQueryFactory;
import net.pladema.provincialreports.nursingreports.service.NursingReportsExcelService;

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
@RequestMapping("nursingreports")
@Tag(name = "Reports - nursing", description = "Reportes de enfermería, nursing reports")
public class NursingReportsController {
	private static final Logger logger = LoggerFactory.getLogger(NursingReportsController.class);

	private final NursingReportsExcelService excelService;
	private final ReportExcelUtilsService excelUtilsService;
	private final NursingReportsQueryFactory queryFactory;

	public NursingReportsController(NursingReportsExcelService excelService, ReportExcelUtilsService excelUtilsService, NursingReportsQueryFactory queryFactory) {
		this.excelService = excelService;
		this.excelUtilsService = excelUtilsService;
		this.queryFactory = queryFactory;
	}

	@GetMapping(value = "/{institutionId}/nursing-emergency")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<byte[]> getNursingEmergencyExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
		logger.info("getNursingEmergencyExcelReport started with institution id = {}, fromDate = {}, toDate = {}", institutionId, fromDate, toDate);

		try {
			String title = "Reporte de emergencias diarias de enfermería";
			String[] headers = {"Ambulancia", "Oficina", "Sector", "Intervención policial", "Fecha", "Hora", "Profesional que registró la atención", "Último profesional que lo atendió", "Identificación", "Apellido de paciente", "Nombre de paciente", "Sexo", "Género", "Fecha de nacimiento", "Edad a fecha del turno", "Edad a hoy", "Etnia", "Domicilio", "Localidad", "Obra social", "Medio de ingreso", "Estado", "Tipo", "Notas de triage", "Triage", "Fecha de alta", "Ambulancia de alta", "Tipo de alta", "Salida"};
			logger.debug("building nursing emergency excel report");
			// no filtering by date interval, date is mainly a decorative
			IWorkbook wb = excelService.buildNursingEmergencyExcel(title, headers, queryFactory.queryNursingEmergency(institutionId), institutionId, fromDate, toDate);

			String filename = "Enfermería - Emergencias - " + excelUtilsService.newGetPeriodForFilenameFromDates(fromDate, toDate) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating nursing emergency excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}

	@GetMapping(value = "/{institutionId}/nursing-outpatient")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<byte[]> getNursingOutpatientExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
		logger.info("getNursingOutpatientExcelReport started with institution id = {}, fromDate = {}, toDate = {}", institutionId, fromDate, toDate);

		try {
			String title = "Reporte de enfermería ambulatoria";
			String[] headers = {"Prestador", "DNI de prestador", "Fecha de atención", "Hora", "N° de consulta", "DNI de paciente", "Nombre de paciente", "Sexo", "Fecha de nacimiento", "Edad a fecha de turno", "Edad a hoy", "Etnia", "Obra(s) social(es)", "Domicilio", "Localidad", "Frecuencia cardíaca (lpm)", "Frecuencia respiratoria (rpm)", "Temperatura (°C)", "Saturación periférica de hemoglobina con oxígeno (SpO2)", "Presión sistólica (mmHg)", "Presión diastólica (mmHg)", "Presión arterial media (mmHg)", "Altura (cm)", "Peso (kg)", "Índice de masa corporal", "Procedimientos", "Evolución"};
			logger.debug("building nursing outpatient excel report");
			IWorkbook wb = excelService.buildNursingOutpatientExcel(title, headers, queryFactory.queryNursingOutpatient(institutionId, fromDate, toDate), institutionId, fromDate, toDate);

			String filename = "Enfermería - Ambulatoria - " + excelUtilsService.newGetPeriodForFilenameFromDates(fromDate, toDate) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating nursing outpatient excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}

	@GetMapping(value = "/{institutionId}/nursing-hospitalization")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<byte[]> getNursingHospitalizationExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
		logger.info("getNursingHospitalizationExcelReport started with institution id = {}, fromDate = {}, toDate = {}", institutionId, fromDate, toDate);

		try {
			String title = "Reporte de internaciones de enfermería";
			String[] headers = {"Apellido de paciente", "Nombre de paciente", "Género", "Identificación", "Profesional", "Matrícula", "Ingreso", "Alta probable", "Cama", "Categoría", "Habitación", "Sector", "Alta", "Procedimientos", "Signos vitales"};
			logger.debug("building nursing hospitalization excel report");
			IWorkbook wb = excelService.buildNursingHospitalizationExcel(title, headers, queryFactory.queryNursingHospitalization(institutionId, fromDate, toDate), institutionId, fromDate, toDate);

			String filename = "Enfermería - Internaciones - " + excelUtilsService.newGetPeriodForFilenameFromDates(fromDate, toDate) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating nursing hospitalization excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}

	@GetMapping(value = "/{institutionId}/nursing-procedures")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<byte[]> getNursingProceduresExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
		logger.info("getNursingProceduresExcelReport started with institution id = {}, fromDate = {}, toDate = {}", institutionId, fromDate, toDate);

		try {
			String title = "Reporte de procedimientos realizados de enfermería";
			String[] headers = {"Prestador", "DNI de prestador", "Fecha de atención", "Hora", "DNI de paciente", "Nombre de paciente", "Sexo", "Fecha de nacimiento", "Edad a fecha de turno", "Edad a hoy", "Etnia", "Obra social", "Domicilio", "Localidad", "Frecuencia cardíaca (lpm)", "Frecuencia respiratoria (rpm)", "Temperatura (°C)", "Saturación periférica de hemoglobina con oxígeno (SpO2)", "Presión sistólica (mmHg)", "Presión diastólica (mmHg)", "Presión arterial media (mmHg)", "Altura (cm)", "Peso (kg)", "Índice de masa corporal", "Procedimientos", "Problemas", "Medicación", "Evolución"};
			logger.debug("building nursing procedures excel report");
			IWorkbook wb = excelService.buildNursingProceduresExcel(title, headers, queryFactory.queryNursingProcedures(institutionId, fromDate, toDate), institutionId, fromDate, toDate);

			String filename = "Enfermería - Procedimientos realizados - " + excelUtilsService.newGetPeriodForFilenameFromDates(fromDate, toDate) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating nursing procedures excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}

	@GetMapping(value = "/{institutionId}/nursing-vaccine")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<byte[]> getNursingVaccinesExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
		logger.info("getNursingVaccinesExcelReport started with institution id = {}, fromDate = {}, toDate = {}", institutionId, fromDate, toDate);

		try {
			String title = "Reporte de vacunas aplicadas de enfermería";
			String[] headers = {"Prestador", "DNI de prestador", "Fecha de atención", "DNI de paciente", "Nombre de paciente", "Sexo", "Fecha de nacimiento", "Edad a fecha de turno", "Vacuna", "SCTID", "Estado", "Condición", "Esquema", "Dosis", "Lote"};
			logger.debug("building nursing vaccines excel report");
			IWorkbook wb = excelService.buildNursingVaccinesExcel(title, headers, queryFactory.queryNursingVaccines(institutionId, fromDate, toDate), institutionId, fromDate, toDate);

			String filename = "Enfermería - Vacunas aplicadas - " + excelUtilsService.newGetPeriodForFilenameFromDates(fromDate, toDate) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating nursing vaccines excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}

}
