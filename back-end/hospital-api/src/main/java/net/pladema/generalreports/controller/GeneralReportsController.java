package net.pladema.generalreports.controller;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.generalreports.repository.QueryFactoryGR;
import net.pladema.generalreports.service.ExcelServiceGR;

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
public class GeneralReportsController {

	private static final Logger LOG = LoggerFactory.getLogger(GeneralReportsController.class);

	private static final String OUTPUT = "Output -> {}";

	private final ExcelServiceGR excelServiceGR;

	private final QueryFactoryGR queryFactoryGR;

	private final LocalDateMapper localDateMapper;

	public GeneralReportsController(QueryFactoryGR queryFactoryGR, ExcelServiceGR excelServiceGR, LocalDateMapper localDateMapper){
		this.queryFactoryGR = queryFactoryGR;
		this.excelServiceGR = excelServiceGR;
		this.localDateMapper = localDateMapper;
	}

	@GetMapping(value = "/{institutionId}/dailyEmergency")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getDailyEmergencyExcelReport(
			@PathVariable Integer institutionId,
			HttpServletResponse response
	)throws Exception{
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String title = "Emergencias";
		String[] headers = new String[] {"Fecha de creación", "ID", "Institución", "Ambulancia", "Oficina", "Sector", "Intervención Policial", "Fecha de atención", "Hora de atención", "Identificación", "Apellidos", "Nombres",
				"Obra social", "Medio de Ingreso", "Estado", "Tipo", "Notas del Triage", "Triage", "Fecha de Alta", "Ambulancia de Alta", "Tipo de Alta", "Salida"};

		IWorkbook wb = this.excelServiceGR.buildExcelEmergencias(title, headers, this.queryFactoryGR.queryEmergencias(institutionId));

		String filename = title + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename=" + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/diabetics")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getDiabeticsExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value="fromDate", required = true) String fromDate,
			@RequestParam(value="toDate", required = true) String toDate,
			HttpServletResponse response
	)throws Exception{
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String title = "Diabéticos Confirmados";
		String[] headers = new String[] {"ID", "Institución", "Fecha", "Prestador", "DNI-Prestador", "Paciente", "DNI-Paciente", "Problema", "Motivos",
			"Hemoglobina Glicosilada", "Medicación"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelServiceGR.buildExcelDiabeticosHipertensos(title, headers, this.queryFactoryGR.queryDiabeticos(institutionId, startDate, endDate));

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
			@RequestParam(value="fromDate", required = true) String fromDate,
			@RequestParam(value="toDate", required = true) String toDate,
			HttpServletResponse response
	)throws Exception{
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String title = "Hipertensos Confirmados";
		String[] headers = new String[] {"ID", "Institución", "Fecha", "Prestador", "DNI-Prestador", "Paciente", "DNI-Paciente", "Problema", "Motivos",
				"Presión Arterial", "Medicación"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelServiceGR.buildExcelDiabeticosHipertensos(title, headers, this.queryFactoryGR.queryHipertensos(institutionId, startDate, endDate));

		String filename = title + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename=" + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/patientEmergencies")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getPatientEmergenciesExcelReport(
			@PathVariable Integer institutionId,
			HttpServletResponse response
	)throws Exception{
		LOG.debug("Se creará el excel{}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate {}", institutionId);

		String title = "Reporte Enfermeria - Emergencias Pacientes";
		String[] headers = new String[] {"Institucion", "Ambulancia", "Oficina", "Sector", "Intervención Policial","Fecha","Hora",
				"Profesional que registró la atención", "Ultimo profesional que lo antendió","Identificación","Apellidos","Nombres","Sexo",
				"Genero","Nombre con el que se identifica","Fecha de nacimiento","Edad a fecha del turno","Edad a hoy","Etnia","Domicilio",
				"Localidad","Obra social","Medio de Ingreso","Estado","Tipo","Notas del Triage","Triage","Fecha de alta","Ambulancia de alta",
				"Tipo de alta","Salida"};

		IWorkbook wb = this.excelServiceGR.buildExcelPatientEmergencies(title,headers,this.queryFactoryGR.queryPatientEmergencies(institutionId));

		String filename = title + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename= "+ filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/outpatientNursing")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getOutpatientNursingExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true) String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate,
			HttpServletResponse response
	)throws Exception{
		LOG.debug("Se creará el excel{}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate{}, toDate{}", institutionId);

		String title = "Reporte Enfemeria - Enfermeria Ambulatorio";
		String[] headers = new String[]{"Institucion","Unidad Operativa","Prestador","DNI","Fecha de Atencion","Hora","Const N°","DNI Paciente",
				"Nombre Paciente","Sexo","Genero","Nombre con el que se identifica","Fecha de Nacimiento","Edad a fecha del turno","Edad a Hoy","Etnia",
				"Obra/s Social/es","Domicilio","Localidad","Nivel de instruccion","Situacion Laboral","Signos vitales","Procedimientos","Evolucion"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelServiceGR.buildExcelOutpatientNursing(title, headers, this.queryFactoryGR.queryOutpatientNursing(institutionId,startDate,endDate));

		String filename = title + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename= "+ filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/nursingInternment")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getNursingInternmentExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true) String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate,
			HttpServletResponse response
	)throws Exception{
		LOG.debug("Se creará el excel{}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate{}, toDate{}", institutionId);

		String title = "Reporte Enfermeria - Enfermeria Internacion";
		String[] headers = new String[]{"Institucion","Apellidos","Nombres","Genero","Identificacion","Profesional","Matricula",
				"Ingreso","Alta Probable","Cama","Categoria","Habitacion","Sector","Alta","Procedimientos","Signos Vitales"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelServiceGR.buildExcelNursingInternment(title, headers, this.queryFactoryGR.queryNursingInternment(institutionId,startDate,endDate));

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
