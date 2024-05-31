package net.pladema.provincialreports.programreports.controller;


import java.io.OutputStream;
import java.time.LocalDate;

import javax.servlet.http.HttpServletResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.provincialreports.reportformat.domain.service.ReportExcelUtilsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.provincialreports.programreports.repository.ProgramReportQueryFactory;
import net.pladema.provincialreports.programreports.service.NewProgramReportsExcelService;

@RestController
@RequestMapping("program-reports")
@Tag(name = "Program reports", description = "Reportes de programa")
public class NewProgramReportsController {

	private static final Logger logger = LoggerFactory.getLogger(NewProgramReportsController.class);

	private final NewProgramReportsExcelService excelService;

	private final ReportExcelUtilsService excelUtilsService;

	private final ProgramReportQueryFactory queryFactory;

	public NewProgramReportsController(NewProgramReportsExcelService excelService, ReportExcelUtilsService excelUtilsService, ProgramReportQueryFactory queryFactory) {
		this.excelService = excelService;
		this.excelUtilsService = excelUtilsService;
		this.queryFactory = queryFactory;
	}

	@GetMapping(value = "/{institutionId}/epidemiology-one")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody void getEpidemiologyOneExcelReport(@PathVariable Integer institutionId, @RequestParam(value = "fromDate") String fromDate, @RequestParam(value = "toDate") String toDate, HttpServletResponse response) throws Exception {

		logger.info("getEpidemiologyOneExcelReport start with institutionId: {}, fromDate: {}, toDate: {}", institutionId, fromDate, toDate);

		String title = "Reporte de epidemiología - Vigilancia epidemiológica";

		String[] headers = new String[]{"Apellido y nombre", "Codificación", "Fecha de nacimiento", "Sexo", "Fecha", "Departamento", "Domicilio", "CIE10", "Documento", "Diagnóstico"};

		try {
			LocalDate startDate = LocalDate.parse(fromDate);
			LocalDate endDate = LocalDate.parse(toDate);

			logger.debug("Parsed dates - startDate: {}, endDate: {}", startDate, endDate);

			IWorkbook wb = this.excelService.buildEpidemiologyOneExcel(title, headers, this.queryFactory.queryEpidemiologyOne(institutionId, startDate, endDate), institutionId, startDate, endDate);

			String filename = "Epidemiología - Vigilancia epidemiológica - " + excelUtilsService.getPeriodForFilenameFromDates(startDate, endDate) + "." + wb.getExtension();

			response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
			response.setContentType(wb.getContentType());

			logger.debug("Writing epidemiology one excel report to response with filename: {}", filename);

			OutputStream outputStream = response.getOutputStream();
			wb.write(outputStream);
			outputStream.close();
			outputStream.flush();
			response.flushBuffer();

			logger.info("Successfully wrote epidemiology one report for institutionId: {}", institutionId);
		} catch (Exception e) {
			logger.error("Error generating epidemiology one report for institutionId: {}", institutionId, e);
			throw e;
		}

	}

	@GetMapping(value = "/{institutionId}/epidemiology-two")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody void getEpidemiologyTwoExcelReport(@PathVariable Integer institutionId, @RequestParam(value = "fromDate") String fromDate, @RequestParam(value = "toDate") String toDate, HttpServletResponse response) throws Exception {

		logger.info("getEpidemiologyTwoExcelReport start with institutionId: {}, fromDate: {}, toDate: {}", institutionId, fromDate, toDate);

		String title = "Reporte de epidemiología - Notificación colectiva";

		String[] headers = new String[]{"Diagnóstico/código", "Grupo", "Total"};

		try {
			LocalDate startDate = LocalDate.parse(fromDate);
			LocalDate endDate = LocalDate.parse(toDate);

			logger.debug("Parsed dates - startDate: {}, endDate: {}", startDate, endDate);

			IWorkbook wb = this.excelService.buildEpidemiologyTwoExcel(title, headers, this.queryFactory.queryEpidemiologyTwo(institutionId, startDate, endDate), institutionId, startDate, endDate);

			String filename = "Epidemiología - Notificación colectiva - " + excelUtilsService.getPeriodForFilenameFromDates(startDate, endDate) + "." + wb.getExtension();

			response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
			response.setContentType(wb.getContentType());

			logger.debug("Writing epidemiology two excel report to response with filename: {}", filename);

			OutputStream outputStream = response.getOutputStream();
			wb.write(outputStream);
			outputStream.close();
			outputStream.flush();
			response.flushBuffer();

			logger.info("Successfully wrote epidemiology report two for institutionId: {}", institutionId);
		} catch (Exception e) {
			logger.error("Error generating epidemiology report two for institutionId: {}", institutionId, e);
			throw e;
		}

	}

	@GetMapping(value = "/{institutionId}/recupero-general")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody void getRecuperoGeneralExcelReport(@PathVariable Integer institutionId, @RequestParam(value = "fromDate") String fromDate, @RequestParam(value = "toDate") String toDate, HttpServletResponse response) throws Exception {

		logger.info("getRecuperoGeneralExcelReport start with institutionId: {}, fromDate: {}, toDate: {}", institutionId, fromDate, toDate);

		String title = "Reporte de recupero - obras sociales";

		String[] headers = new String[]{"Unidad Operativa", "Nombre de prestador", "DNI de prestador", "Fecha de atención", "Hora", "Cons. n°", "DNI de paciente", "Nombre de paciente", "Sexo", "Fecha de nacimiento", "Edad a fecha del turno", "Edad a hoy", "Obra/s social/es", "Domicilio", "Localidad", "Motivos", "Procedimientos", "Problemas", "Medicación", "Evolución"};
		try {
			LocalDate startDate = LocalDate.parse(fromDate);
			LocalDate endDate = LocalDate.parse(toDate);

			logger.debug("Parsed dates - startDate: {}, endDate: {}", startDate, endDate);

			IWorkbook wb = this.excelService.buildRecuperoGeneralExcel(title, headers, this.queryFactory.queryRecuperoGeneral(institutionId, startDate, endDate), institutionId, startDate, endDate);

			String filename = "Recupero - Obras sociales - " + excelUtilsService.getPeriodForFilenameFromDates(startDate, endDate) + "." + wb.getExtension();

			response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
			response.setContentType(wb.getContentType());

			logger.debug("Writing 'recupero - obras sociales' excel report to response with filename: {}", filename);

			OutputStream outputStream = response.getOutputStream();
			wb.write(outputStream);
			outputStream.close();
			outputStream.flush();
			response.flushBuffer();

			logger.info("Successfully wrote 'recupero - obras sociales' report for institutionId: {}", institutionId);
		} catch (Exception e) {
			logger.error("Error generating 'recupero - obras sociales' report for institutionId: {}", institutionId, e);
			throw e;
		}

	}
}
