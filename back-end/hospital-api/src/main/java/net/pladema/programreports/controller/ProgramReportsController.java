package net.pladema.programreports.controller;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.programreports.repository.QueryFactoryPR;
import net.pladema.programreports.service.ExcelServicePR;

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
@RequestMapping("programreports")
public class ProgramReportsController {

	private static final Logger LOG = LoggerFactory.getLogger(ProgramReportsController.class);

	private static final String OUTPUT = "Output -> {}";

	private final ExcelServicePR excelService;

	private final QueryFactoryPR queryFactoryPR;

	private final LocalDateMapper localDateMapper;

	public ProgramReportsController(QueryFactoryPR queryFactoryPR, ExcelServicePR excelService, LocalDateMapper localDateMapper) {
		this.queryFactoryPR = queryFactoryPR;
		this.excelService = excelService;
		this.localDateMapper = localDateMapper;
	}

	@GetMapping(value = "/{institutionId}/monthlyEpiI")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getMonthlyEpiIExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value="fromDate", required = true) String fromDate,
			@RequestParam(value="toDate", required = true) String toDate,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String tittle = "Epidemiologia I";
		String [] headers = new String[]{"Apellido y Nombre", "Codificación", "Fecha de nacimiento", "Sexo", "Fecha", "Departamento", "Domicilio", "CIE10", "Documento", "Diagnóstico"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelService.buildExcelEpidemiologiaI(tittle, headers, this.queryFactoryPR.queryEpidemiologiaI(institutionId, startDate, endDate));

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
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getMonthlyEpiIIExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value="fromDate", required = true) String fromDate,
			@RequestParam(value="toDate", required = true) String toDate,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String tittle = "Epidemiologia II";
		String [] headers = new String[]{"Diagnostico/Codigo", "Grupo", "Total"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelService.buildExcelEpidemiologiaII(tittle, headers, this.queryFactoryPR.queryEpidemiologiaII(institutionId, startDate, endDate));

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
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getMonthlyRecuperoExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value="fromDate", required = true) String fromDate,
			@RequestParam(value="toDate", required = true) String toDate,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String tittle = "Recupero";

		String [] headers = new String[]{"Institution", "Unidad Operativa", "Prestador", "DNI", "Fecha de atención","Hora", "Cons.N°", "DNI Paciente", "Nombre Paciente", "Sexo",
				"Fecha de nacimiento", "Edad a fecha del turno", "Edad a Hoy","Obra/s social/es", "Domicilio", "Localidad", "Indice de masa corporal", "Motivos", "Procedimientos",
				"Problemas", "Medicación", "Evolución"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelService.buildExcelRecupero(tittle, headers, this.queryFactoryPR.queryRecupero(institutionId, startDate, endDate));

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
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getMonthlySumarExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value="fromDate", required = true) String fromDate,
			@RequestParam(value="toDate", required = true) String toDate,
			@RequestParam(value = "clinicalSpecialtyId", required = false) Integer clinicalSpecialtyId,
			@RequestParam(value ="doctorId", required = false) Integer doctorId,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String [] headers = new String[]{"Institution", "Unidad Operativa", "Prestador", "DNI", "Fecha de atención","Hora", "Cons.N°", "DNI Paciente", "Nombre Paciente", "Sexo",
				"Fecha de nacimiento", "Edad a fecha del turno", "Edad a Hoy","Obra/s social/es", "Domicilio", "Localidad", "Indice de masa corporal", "Motivos", "Procedimientos",
				"Problemas", "Medicación", "Evolución",  "Genero", "Nombre con el que se identifica","Etnia", "Nivel de Instrucción", "Situación laboral", "Presión sistólica", "Presión diastólica",
				"Presión arterial media", "Temperatura", "Frecuencia cardíaca", "Presión respiratoria", "Saturación de hemoglobina con oxígeno", "Altura", "Peso", "Perímetro Cefálico"};

		String tittle = "Recupero - Sumar";

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelService.buildExcelRecupero(tittle, headers, this.queryFactoryPR.querySumar(institutionId, startDate, endDate, clinicalSpecialtyId, doctorId));

		String filename = tittle + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename=" + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/odontology")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getMonthlyOdontologyExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value="fromDate", required = true) String fromDate,
			@RequestParam(value="toDate", required = true) String toDate,
			HttpServletResponse response
	)throws Exception{
		LOG.debug("Se creará el excel{}", institutionId);
		LOG.debug("Inputs parameters -> institutionID {}, fromDate {}, toDate{}", institutionId);

		String title = "Reporte Odontologia General";
		String[] headers = new String[]{"Institucion", "Unidad Operativa", "Prestador", "DNI", "Fecha de atencion", "Cons. N°",
				"DNI Paciente", "Nombre Paciente", "Sexo", "Genero", "Nombre con el que se identifica", "Fecha de nacimiento",
				"Edad a fecha del turno", "Edad a Hoy", "Etnia", "Obra/s social/es", "Indice CPO - Permanentes", "Indice CEO - Temporarios",
				"Domicilio", "Localidad", "Nivel de Instruccion", "Dientes Permanentes Presentes", "Dientes Temporales Presentes",
				"Situacion laboral", "Motivos", "Procedimientos", "problemas", "Medicacion", "Evolucion"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelService.buildExcelOdontologia(title, headers, this.queryFactoryPR.queryOdontologia(institutionId, startDate, endDate));

		String filename = title + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename= " + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

}
