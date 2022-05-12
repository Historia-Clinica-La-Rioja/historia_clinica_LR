package net.pladema.programreports.controller;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.programreports.repository.QueryFactoryPR;
import net.pladema.programreports.service.ExcelServiceEpiI;

import net.pladema.programreports.service.ExcelServiceEpiII;

import net.pladema.programreports.service.ExcelServiceRecupero;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("programreports")
public class ProgramReportsController {

	private static final Logger LOG = LoggerFactory.getLogger(ProgramReportsController.class);

	private static final String OUTPUT = "Output -> {}";

	private final ExcelServiceEpiI excelServiceEpiI;

	private final ExcelServiceEpiII excelServiceEpiII;

	private final ExcelServiceRecupero excelServiceRecupero;

	private final QueryFactoryPR queryFactoryPR;

	private final LocalDateMapper localDateMapper;


	public ProgramReportsController(ExcelServiceEpiI excelServiceEpiI, ExcelServiceEpiII excelServiceEpiII, ExcelServiceRecupero excelServiceRecupero, QueryFactoryPR queryFactoryPR, LocalDateMapper localDateMapper) {
		this.excelServiceEpiI = excelServiceEpiI;
		this.excelServiceEpiII = excelServiceEpiII;
		this.excelServiceRecupero = excelServiceRecupero;
		this.queryFactoryPR = queryFactoryPR;
		this.localDateMapper = localDateMapper;
	}

	@GetMapping(value = "/{institutionId}/monthlyEpiI")
	public @ResponseBody
	void getMonthlyEpiIExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "clinicalSpecialtyId", required = false) Integer clinicalSpecialtyId,
			@RequestParam(value = "doctorId", required = false) Integer doctorId,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId, fromDate, toDate);

		String tittle = "Epidemiologia I";
		String [] headers = new String[]{"Apellido y Nombre", "Codificación", "Fecha de nacimiento", "Sexo", "Fecha", "Departamento", "Domicilio", "CIE10", "Documento", "Diagnóstico"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelServiceEpiI.buildExcelFromQuery(tittle, headers, this.queryFactoryPR.query(institutionId));

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
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "clinicalSpecialtyId", required = false) Integer clinicalSpecialtyId,
			@RequestParam(value = "doctorId", required = false) Integer doctorId,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId, fromDate, toDate);

		String tittle = "Epidemiologia II";
		String [] headers = new String[]{"Diagnostico/Codigo", "Grupo", "Total"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelServiceEpiII.buildExcelFromQuery(tittle, headers, this.queryFactoryPR.queryII(institutionId));

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
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "clinicalSpecialtyId", required = false) Integer clinicalSpecialtyId,
			@RequestParam(value = "doctorId", required = false) Integer doctorId,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId, fromDate, toDate);

		String tittle = "Recupero";
		String [] headers = new String[]{"Unidad Operativa", "Prestador", "DNI", "Fecha de atención", "Cons.N°", "DNI Paciente", "Nombre Paciente", "Sexo", "Genero",
				"Nombre con el que se identifica", "Fecha de nacimiento", "Edad a fecha del turno", "Edad a Hoy", "Etnia", "Obra/s social/es", "Domicilio",
				"Localidad", "Nivel de Instrucción", "Situación laboral", "Presión sistólica", "Presión diastólica", "Presión arterial media", "Temperatura",
				"Frecuencia cardíaca", "Presión respiratoria", "Saturación de hemoglobina con oxígeno", "Altura", "Peso", "Indice de masa corporal", "Motivos",
				"Procedimientos", "Problemas", "Medicación", "Evolución"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelServiceRecupero.buildExcelFromQuery(tittle, headers, this.queryFactoryPR.queryIII(institutionId));

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
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "clinicalSpecialtyId", required = false) Integer clinicalSpecialtyId,
			@RequestParam(value = "doctorId", required = false) Integer doctorId,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId, fromDate, toDate);

		String tittle = "Recupero - Sumar";
		String [] headers = new String[]{"Unidad Operativa", "Prestador", "DNI", "Fecha de atención", "Cons.N°", "DNI Paciente", "Nombre Paciente", "Sexo", "Genero",
				"Nombre con el que se identifica", "Fecha de nacimiento", "Edad a fecha del turno", "Edad a Hoy", "Etnia", "Obra/s social/es", "Domicilio",
				"Localidad", "Nivel de Instrucción", "Situación laboral", "Presión sistólica", "Presión diastólica", "Presión arterial media", "Temperatura",
				"Frecuencia cardíaca", "Presión respiratoria", "Saturación de hemoglobina con oxígeno", "Altura", "Peso", "Indice de masa corporal", "Motivos",
				"Procedimientos", "Problemas", "Medicación", "Evolución"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelServiceRecupero.buildExcelFromQuery(tittle, headers, this.queryFactoryPR.queryIV(institutionId));

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
