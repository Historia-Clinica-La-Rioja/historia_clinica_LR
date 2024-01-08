package net.pladema.provincialreports.programreports.controller;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.provincialreports.programreports.repository.ProgramReportQueryFactory;
import net.pladema.provincialreports.programreports.service.ProgramReportExcelService;

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
public class ProgramReportController {

	private static final Logger LOG = LoggerFactory.getLogger(ProgramReportController.class);

	private static final String OUTPUT = "Output -> {}";

	private final ProgramReportExcelService excelService;

	private final ProgramReportQueryFactory programReportQueryFactory;

	private final LocalDateMapper localDateMapper;

	public ProgramReportController(ProgramReportQueryFactory programReportQueryFactory, ProgramReportExcelService excelService, LocalDateMapper localDateMapper) {
		this.programReportQueryFactory = programReportQueryFactory;
		this.excelService = excelService;
		this.localDateMapper = localDateMapper;
	}

	@GetMapping(value = "/{institutionId}/epidemiology-one")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getEpidemiologyOneExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true) String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String title = "Reporte de Epidemiología - Vigilancia Epidemiológica";
		String [] headers = new String[]{"Apellido y Nombre", "Codificación", "Fecha de nacimiento", "Sexo", "Fecha",
				"Departamento", "Domicilio", "CIE10", "Documento", "Diagnóstico"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelService.buildExcelEpidemiologyOne(title, headers, this.programReportQueryFactory.queryEpidemiologyOne(institutionId, startDate, endDate));

		String filename = title + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename=" + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/epidemiology-two")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getEpidemiologyTwoExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true) String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String title = "Reporte de Epidemiología - Notificación Colectiva";
		String [] headers = new String[]{"Diagnostico/Codigo", "Grupo", "Total"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelService.buildExcelEpidemiologyTwo(title, headers, this.programReportQueryFactory.queryEpidemiologyTwo(institutionId, startDate, endDate));

		String filename = title + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename=" + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/recupero-general")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getRecuperoGeneralExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true) String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String title = "Reporte de Recupero - Obras Sociales";

		String [] headers = new String[]{"Institution", "Unidad Operativa", "Prestador", "DNI", "Fecha de atención","Hora", "Cons.N°", "DNI Paciente", "Nombre Paciente", "Sexo",
				"Fecha de nacimiento", "Edad a fecha del turno", "Edad a Hoy","Obra/s social/es", "Domicilio", "Localidad", "Motivos", "Procedimientos",
				"Problemas", "Medicación", "Evolución"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelService.buildExcelRecuperoGeneral(title, headers, this.programReportQueryFactory.queryRecuperoGeneral(institutionId, startDate, endDate));

		String filename = title + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename=" + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/sumar-general")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getSumarGeneralExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true) String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate,
			@RequestParam(value = "clinicalSpecialtyId", required = false) Integer clinicalSpecialtyId,
			@RequestParam(value = "doctorId", required = false) Integer doctorId,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String [] headers = new String[]{"Institucion","Unidad Operativa", "Prestador","DNI","Fecha de Atencion","Hora","Cons. N°","DNI Paciente","Nombre Paciente","Sexo","Genero","Nombre con el que se identifica",
				"Fecha de nacimiento","Edad a fecha del turno","Edad a hoy","Etnia","Obra/s Social/es","Domicilio","Localidad","Nivel de Instruccion","Situacion Laboral",
				"Presión sistólica","Presión diastólica","Presion arterial media","Temperatura","Frecuencia cardiaca","Frecuencia respiratoria","Saturación de hemoglobina con oxigeno",
				"Altura","Peso","Indice de Masa corporal","Perímetro cefálico","Motivos","Procedimientos","Problemas","Medicacion","Evolución"};

		String title = "Reporte de Sumar";

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelService.buildExcelSumarGeneral(title, headers, this.programReportQueryFactory.querySumarGeneral(institutionId, startDate, endDate, clinicalSpecialtyId, doctorId));

		String filename = title + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename=" + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/sumar-odontologico")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getSumarOdontologicoExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true) String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel{}", institutionId);
		LOG.debug("Inputs parameters -> institutionID {}, fromDate {}, toDate{}", institutionId);

		String title = "Reporte de Sumar - Odontológico";
		String[] headers = new String[]{"Institucion", "Unidad Operativa", "Prestador", "DNI", "Fecha de atencion", "Hora",
				"DNI Paciente", "Nombre Paciente", "Sexo", "Fecha de nacimiento", "Edad a fecha del turno", "Obra/s social/es",
				"Domicilio", "Localidad", "Indice CPO - Permanentes", "Indice CEO - Temporarios", "Motivos", "Procedimientos",
				"Procedimientos de Odontología", "Problemas", "Diagnósticos de Odontología"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelService.buildExcelOdontological(title, headers, this.programReportQueryFactory.querySumarOdontologico(institutionId, startDate, endDate));

		String filename = title + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename=" + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/recupero-odontologico")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getRecuperoOdontologicoExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true) String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String title = "Reporte de Recupero Odontológico - Obras Sociales";
		String[] headers = new String[]{"Institucion", "Unidad Operativa", "Prestador", "DNI", "Fecha de Atencion", "Hora",
				"DNI paciente", "Nombre Paciente", "Sexo", "Fecha de nacimiento", "Edad a fecha de turno", "Obra social",
				"Domicilio", "Localidad", "CPO permanentes", "CEO permanentes", "Motivos", "procedimientos",
				"Procedimientos de odontologia", "Problemas", "Diagnosticos de odontologia"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelService.buildExcelRecuperoOdontologico(title, headers, this.programReportQueryFactory.queryRecuperoOdontologico(institutionId, startDate, endDate));

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
