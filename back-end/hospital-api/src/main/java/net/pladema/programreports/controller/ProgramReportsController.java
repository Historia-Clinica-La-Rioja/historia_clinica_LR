package net.pladema.programreports.controller;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.programreports.repository.QueryFactoryPR;
import net.pladema.programreports.service.ExcelServiceEpidemiologiaI;

import net.pladema.programreports.service.ExcelServiceEpidemiologiaII;

import net.pladema.programreports.service.ExcelServiceRecupero;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import java.io.OutputStream;

@RestController
@RequestMapping("programreports")
public class ProgramReportsController {

	private static final Logger LOG = LoggerFactory.getLogger(ProgramReportsController.class);

	private static final String OUTPUT = "Output -> {}";

	private final ExcelServiceEpidemiologiaI excelServiceEpidemiologiaI;

	private final ExcelServiceEpidemiologiaII excelServiceEpidemiologiaII;

	private final ExcelServiceRecupero excelServiceRecupero;

	private final QueryFactoryPR queryFactoryPR;

	private final LocalDateMapper localDateMapper;


	public ProgramReportsController(ExcelServiceEpidemiologiaI excelServiceEpiI, ExcelServiceEpidemiologiaII excelServiceEpiII, ExcelServiceEpidemiologiaI excelServiceEpidemiologiaI, ExcelServiceEpidemiologiaII excelServiceEpidemiologiaII, ExcelServiceRecupero excelServiceRecupero, QueryFactoryPR queryFactoryPR, LocalDateMapper localDateMapper) {
		this.excelServiceEpidemiologiaI = excelServiceEpidemiologiaI;
		this.excelServiceEpidemiologiaII = excelServiceEpidemiologiaII;
		this.excelServiceRecupero = excelServiceRecupero;
		this.queryFactoryPR = queryFactoryPR;
		this.localDateMapper = localDateMapper;
	}

	@GetMapping(value = "/{institutionId}/monthlyEpiI")
	public @ResponseBody
	void getMonthlyEpiIExcelReport(
			@PathVariable Integer institutionId,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String tittle = "Epidemiologia I";
		String [] headers = new String[]{"Apellido y Nombre", "Codificación", "Fecha de nacimiento", "Sexo", "Fecha", "Departamento", "Domicilio", "CIE10", "Documento", "Diagnóstico"};

		IWorkbook wb = this.excelServiceEpidemiologiaI.buildExcelFromQuery(tittle, headers, this.queryFactoryPR.queryEpidemiologiaI(institutionId));

		String filename = tittle + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename=" + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/monthlyEpiII")
	public @ResponseBody
	void getMonthlyEpiIIExcelReport(
			@PathVariable Integer institutionId,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String tittle = "Epidemiologia II";
		String [] headers = new String[]{"Diagnostico/Codigo", "Grupo", "Total"};

		IWorkbook wb = this.excelServiceEpidemiologiaII.buildExcelFromQuery(tittle, headers, this.queryFactoryPR.queryEpidemiologiaII(institutionId));

		String filename = tittle + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename=" + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/monthlyRecupero")
	public @ResponseBody
	void getMonthlyRecuperoExcelReport(
			@PathVariable Integer institutionId,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String tittle = "Recupero";
		String [] headers = new String[]{"Unidad Operativa", "Prestador", "DNI", "Fecha de atención", "Cons.N°", "DNI Paciente", "Nombre Paciente", "Sexo", "Genero",
				"Nombre con el que se identifica", "Fecha de nacimiento", "Edad a fecha del turno", "Edad a Hoy", "Etnia", "Obra/s social/es", "Domicilio",
				"Localidad", "Nivel de Instrucción", "Situación laboral", "Presión sistólica", "Presión diastólica", "Presión arterial media", "Temperatura",
				"Frecuencia cardíaca", "Presión respiratoria", "Saturación de hemoglobina con oxígeno", "Altura", "Peso", "Indice de masa corporal", "Motivos",
				"Procedimientos", "Problemas", "Medicación", "Evolución"};

		IWorkbook wb = this.excelServiceRecupero.buildExcelFromQuery(tittle, headers, this.queryFactoryPR.queryRecupero(institutionId));

		String filename = tittle + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename=" + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/monthlySumar")
	public @ResponseBody
	void getMonthlySumarExcelReport(
			@PathVariable Integer institutionId,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String tittle = "Recupero - Sumar";
		String [] headers = new String[]{"Unidad Operativa", "Prestador", "DNI", "Fecha de atención", "Cons.N°", "DNI Paciente", "Nombre Paciente", "Sexo", "Genero",
				"Nombre con el que se identifica", "Fecha de nacimiento", "Edad a fecha del turno", "Edad a Hoy", "Etnia", "Obra/s social/es", "Domicilio",
				"Localidad", "Nivel de Instrucción", "Situación laboral", "Presión sistólica", "Presión diastólica", "Presión arterial media", "Temperatura",
				"Frecuencia cardíaca", "Presión respiratoria", "Saturación de hemoglobina con oxígeno", "Altura", "Peso", "Indice de masa corporal", "Motivos",
				"Procedimientos", "Problemas", "Medicación", "Evolución"};

		IWorkbook wb = this.excelServiceRecupero.buildExcelFromQuery(tittle, headers, this.queryFactoryPR.querySumar(institutionId));

		String filename = tittle + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename=" + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}
}
