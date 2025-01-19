package net.pladema.provincialreports.epidemiologyreports.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.provincialreports.epidemiologyreports.repository.EpidemiologyReportsQueryFactory;
import net.pladema.provincialreports.epidemiologyreports.service.EpidemiologyReportsExcelService;
import net.pladema.provincialreports.reportformat.domain.service.ReportExcelUtilsService;

@RestController
@RequestMapping("epidemiologyreports")
@Tag(name = "Reports - epidemiology", description = "Reportes de epidemiología/epidemiológicos, epidemiology reports")
public class EpidemiologyReportsController {
	private static final Logger logger = LoggerFactory.getLogger(EpidemiologyReportsController.class);

	private final EpidemiologyReportsExcelService excelService;
	private final EpidemiologyReportsQueryFactory queryFactory;
	private final ReportExcelUtilsService excelUtilsService;

	public EpidemiologyReportsController(EpidemiologyReportsExcelService excelService, EpidemiologyReportsQueryFactory queryFactory, ReportExcelUtilsService excelUtilsService) {
		this.excelService = excelService;
		this.queryFactory = queryFactory;
		this.excelUtilsService = excelUtilsService;
	}

	@GetMapping(value = "/{institutionId}/dengue-patient-control")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<byte[]> getDenguePatientControlExcelReport(
			@PathVariable Integer institutionId) {
		logger.info("getDenguePatientControlExcelReport started with institution id = {}", institutionId);

		try {
			String title = "Reporte de atenciones relacionadas al dengue - Control de pacientes";
			String[] headers = {"Posible falso positivo", "DNI de paciente", "Apellido y nombre", "Sexo", "Fecha de nacimiento", "Edad", "Domicilio", "Teléfono", "Localidad"};

			logger.debug("building dengue patient control excel report");
			IWorkbook wb = excelService.buildDenguePatientControlExcel(title, headers, queryFactory.queryDenguePatientControl(institutionId), institutionId);

			String filename = "Epidemiología - Atenciones relacionadas al dengue - Control de pacientes - " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating dengue patient control excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}

	@GetMapping(value = "/{institutionId}/complete-dengue")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<byte[]> getCompleteDengueConsultationsExcelReport(
			@PathVariable Integer institutionId) {
		logger.info("getCompleteDengueConsultationsExcelReport started with institution id = {}", institutionId);

		try {
			String title = "Reporte de atenciones relacionadas al dengue - Consultas completas";
			String[] headers = {"Posible falso positivo", "Origen", "Unidad operativa", "DNI de paciente", "Apellido y nombre", "Sexo", "Fecha de nacimiento", "Edad", "Hora de atención", "Obra(s) social(es)", "Presión sistólica (mmHg)", "Presión diastólica (mmHg)", "Presión arterial media (mmHg)", "Temperatura (°C)", "Frecuencia cardíaca (lpm)", "Frecuencia respiratoria (rpm)", "Saturación de hemoglobina con oxígeno (%)", "Altura (cm)", "Peso (kg)", "Índice de masa corporal (kg/m²)", "Motivos de la atención", "Problemas", "Procedimientos", "Evolución"};

			logger.debug("building dengue complete consultations excel report");
			IWorkbook wb = excelService.buildCompleteDengueConsultationExcel(title, headers, queryFactory.queryCompleteDengue(institutionId), institutionId);

			String filename = "Epidemiología - Atenciones relacionadas al dengue - Consultas completas - " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "." + wb.getExtension();
			logger.debug("excel report generated successfully with filename = {}", filename);

			return excelUtilsService.createResponseEntity(wb, filename);
		} catch (Exception e) {
			logger.error("error generating dengue complete consultations excel report for institutionId {}", institutionId, e);
			throw new RuntimeException("error generating report", e);
		}
	}
}
