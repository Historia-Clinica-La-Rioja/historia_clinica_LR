package net.pladema.provincialreports.generalreports.controller;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import io.swagger.v3.oas.annotations.tags.Tag;

import net.pladema.provincialreports.generalreports.repository.GeneralReportsQueryFactory;
import net.pladema.provincialreports.generalreports.service.GeneralReportsExcelService;

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
@RequestMapping("generalreports")
@Tag(name = "Reports - general", description = "Reportes generales, general reports")
public class GeneralReportsController {
	private static final Logger logger = LoggerFactory.getLogger(GeneralReportsController.class);

	private final GeneralReportsExcelService excelService;
	private final ReportExcelUtilsService excelUtilsService;
	private final GeneralReportsQueryFactory queryFactory;

	public GeneralReportsController(GeneralReportsExcelService excelService, ReportExcelUtilsService excelUtilsService, GeneralReportsQueryFactory queryFactory) {
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
		logger.info("getEmergencyExcelReport started with institution id = {}, fromDate = {}, toDate = {}", institutionId, fromDate, toDate);

		try {
			String title = "Reporte de emergencias diarias";
			String[] headers = {"DNI de paciente", "Apellido", "Nombre", "Obra social", "Fecha de atención", "Hora de atención", "Medio de ingreso", "Ambulancia", "Oficina", "Sector", "Estado", "Tipo", "Notas de triage", "Triage", "Fecha de alta", "Ambulancia de alta", "Tipo de alta", "Salida", "Intervención policial"};

			logger.debug("building emergency excel report");
			IWorkbook wb = excelService.buildEmergencyExcel(title, headers, queryFactory.queryEmergency(institutionId, fromDate, toDate), institutionId, fromDate, toDate);

			String filename = "Generales - Emergencias diarias - " + excelUtilsService.newGetPeriodForFilenameFromDates(fromDate, toDate) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating emergency excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}

	@GetMapping(value = "/{institutionId}/diabetic")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<byte[]> getDiabeticsExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
		logger.info("getDiabeticsExcelReport started with institution id = {}, fromDate = {}, toDate = {}", institutionId, fromDate, toDate);

		try {
			String title = "Reporte de diabéticos confirmados";
			String[] headers = {"Fecha", "Apellido de prestador", "Nombre de prestador", "DNI de prestador", "Apellido de paciente", "Nombre de paciente", "DNI de paciente", "Sexo", "Fecha de nacimiento", "Edad a fecha del turno", "Motivos", "Problema", "Hemoglobina glicosilada (mg/dL)", "Medicación"};

			logger.debug("building diabetics excel report");
			IWorkbook wb = excelService.buildDiabeticsOrHypertensivesExcel(title, headers, queryFactory.queryDiabetics(institutionId, fromDate, toDate), institutionId, fromDate, toDate);

			String filename = "Generales - Diabéticos confirmados - " + excelUtilsService.newGetPeriodForFilenameFromDates(fromDate, toDate) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating diabetics excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}

	@GetMapping(value = "/{institutionId}/hypertensive")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<byte[]> getHypertensivesExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
		logger.info("getHypertensivesExcelReport started with institution id = {}, fromDate = {}, toDate = {}", institutionId, fromDate, toDate);

		try {
			String title = "Reporte de hipertensos confirmados";
			String[] headers = {"Fecha", "Apellido de prestador", "Nombre de prestador", "DNI de prestador", "Apellido de paciente", "Nombre de paciente", "DNI de paciente", "Sexo", "Fecha de nacimiento", "Edad a fecha del turno", "Motivos", "Problema", "Presión arterial (mmHg)", "Medicación"};

			logger.debug("building hypertensives excel report");
			IWorkbook wb = excelService.buildDiabeticsOrHypertensivesExcel(title, headers, queryFactory.queryHypertensives(institutionId, fromDate, toDate), institutionId, fromDate, toDate);

			String filename = "Generales - Hipertensos confirmados - " + excelUtilsService.newGetPeriodForFilenameFromDates(fromDate, toDate) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating hypertensives excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}

	@GetMapping(value = "/{institutionId}/complementary-studies")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<byte[]> getComplementaryStudiesExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
		logger.info("getComplementaryStudiesExcelReport started with institution id = {}, fromDate = {}, toDate = {}", institutionId, fromDate, toDate);

		try {
			String title = "Reporte de estudios complementarios";
			String[] headers = {"Fecha", "Categoría", "Estado de orden", "Tipo de solicitud", "Origen de solicitud", "Nombre de paciente", "Tipo de documento", "DNI de paciente", "Obra social", "Número de afiliado", "Nombre de profesional", "Tipo de documento", "DNI de profesional", "Licencia", "Nota", "Fecha de emisión", "Nombre de estudio", "Notas adicionales", "Problema asociado"};

			logger.debug("building complementary studies excel report");
			IWorkbook wb = excelService.buildComplementaryStudiesExcel(title, headers, queryFactory.queryComplementaryStudies(institutionId, fromDate, toDate), institutionId, fromDate, toDate);

			String filename = "Generales - Estudios complementarios - " + excelUtilsService.newGetPeriodForFilenameFromDates(fromDate, toDate) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating complementary studies excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}

	@GetMapping(value = "/{institutionId}/medicines-prescription")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA, PERSONAL_DE_FARMACIA')")
	public ResponseEntity<byte[]> getMedicinesPrescriptionExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
		logger.info("getMedicinesPrescriptionExcelReport started with institution id = {}, fromDate = {}, toDate = {}", institutionId, fromDate, toDate);

		try {
			String title = "Reporte de prescripción de medicamentos";
			String[] headers = {"Nombre de prescriptor", "Licencia", "Licencia provincial", "Licencia nacional", "Fecha", "Nombre de paciente", "DNI de paciente", "Obra social", "Diagnóstico asociado", "¿Es crónico?", "Evento", "Nombre de medicamento", "Duración", "Frecuencia", "Fecha de inicio", "Fecha de fin", "Inicio de suspensión", "Fin de suspensión", "Dosificación", "Dosis diaria", "Dosis por unidad", "Observaciones", "Estado de receta"};

			logger.debug("building medicines prescription excel report");
			IWorkbook wb = excelService.buildMedicinesPrescriptionExcel(title, headers, queryFactory.queryMedicinesPrescription(institutionId, fromDate, toDate), institutionId, fromDate, toDate);

			String filename = "Generales - Prescripción de medicamentos - " + excelUtilsService.newGetPeriodForFilenameFromDates(fromDate, toDate) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating medicines prescription excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}
}
