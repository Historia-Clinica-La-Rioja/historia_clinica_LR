package net.pladema.provincialreports.generalreports.controller;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.provincialreports.generalreports.repository.GeneralReportQueryFactory;
import net.pladema.provincialreports.generalreports.service.GeneralReportExcelService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import java.io.OutputStream;
import java.time.LocalDate;

@RestController
@RequestMapping("generalreports")
public class GeneralReportController {

	private static final Logger LOG = LoggerFactory.getLogger(GeneralReportController.class);

	private static final String OUTPUT = "Output -> {}";

	private final GeneralReportExcelService excelService;

	private final GeneralReportQueryFactory generalReportQueryFactory;

	private final LocalDateMapper localDateMapper;

	public GeneralReportController(GeneralReportQueryFactory generalReportQueryFactory, GeneralReportExcelService excelService, LocalDateMapper localDateMapper) {
		this.generalReportQueryFactory = generalReportQueryFactory;
		this.excelService = excelService;
		this.localDateMapper = localDateMapper;
	}

	@GetMapping(value = "/{institutionId}/emergency")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getEmergencyExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true) String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String title = "Reporte de Emergencias";
		String[] headers = new String[]{"Institución", "Ambulancia", "Oficina", "Sector", "Intervención Policial",
				"Fecha de atención", "Hora de atención", "Identificación", "Apellidos", "Nombres", "Obra social",
				"Medio de Ingreso", "Estado", "Tipo", "Notas del Triage", "Triage", "Fecha de Alta",
				"Ambulancia de Alta", "Tipo de Alta", "Salida", "Motivos", "Problemas", "Evolución"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelService.buildExcelEmergency(title, headers, this.generalReportQueryFactory.queryEmergency(institutionId, startDate, endDate));

		String filename = title + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename=" + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/diabetic")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getDiabeticExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true) String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String title = "Reporte de Diabéticos Confirmados";
		String[] headers = new String[]{"Institución", "Fecha", "Apellido/s del Prestador", "Nombre/s del Prestador",
				"DNI-Prestador", "Apellido/s del Paciente", "Nombre/s del Paciente", "DNI-Paciente", "Sexo", "Fecha de nacimiento",
				"Edad a fecha del turno", "Motivos", "Problema", "Hemoglobina Glicosilada", "Medicación"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelService.buildExcelDiabeticHypertension(title, headers, this.generalReportQueryFactory.queryDiabetics(institutionId, startDate, endDate));

		String filename = title + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename=" + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/hypertensive")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getHypertensiveExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true) String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String title = "Reporte de Hipertensos Confirmados";
		String[] headers = new String[]{"Institución", "Fecha", "Apellido/s del Prestador", "Nombre/s del Prestador",
				"DNI-Prestador", "Apellido/s del Paciente", "Nombre/s del Paciente", "DNI-Paciente", "Sexo", "Fecha de nacimiento",
				"Edad a fecha del turno", "Motivos", "Problema", "Presión Arterial", "Medicación"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelService.buildExcelDiabeticHypertension(title, headers, this.generalReportQueryFactory.queryHypertensive(institutionId, startDate, endDate));

		String filename = title + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename=" + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/complementary-studies")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getComplementaryStudiesExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true) String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel{}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate{}, toDate{}", institutionId);

		String title = "Reporte de Estudios Complementarios";
		String[] headers = new String[]{"Institución", "Fecha", "Categoria", "Estado de la Orden","Tipo de Solicitud", "Origen de Solicitud",
				"Nombre del Paciente", "Tipo de Documento", "Numero del Documento del Paciente", "Obra Social", "Numero de Afiliado",
				"Nombre del Profesional", "Tipo de Documento", "Numero de Documento", "Licencia", "Nota", "Fecha de Emisión",
				"Nombre del Estudio", "Notas Adicionales", "Problema Asociado"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelService.buildExcelComplementaryStudies(title, headers, this.generalReportQueryFactory.queryComplementaryStudies(institutionId, startDate, endDate));

		String filename = title + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename=" + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}
}
