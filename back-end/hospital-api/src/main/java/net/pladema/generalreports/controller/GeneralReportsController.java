package net.pladema.generalreports.controller;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.generalreports.repository.OutPatientOlderAdults;
import net.pladema.generalreports.repository.QueryFactoryGR;
import net.pladema.generalreports.service.ExcelServiceGR;

import net.pladema.medicalconsultation.appointment.application.port.NewAppointmentNotification;

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
	) throws Exception{
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String title = "Emergencias";
		String[] headers = new String[]{"Fecha de creación", "ID", "Institución", "Ambulancia", "Oficina", "Sector", "Intervención Policial", "Fecha de atención", "Hora de atención", "Identificación", "Apellidos", "Nombres", "Obra social", "Medio de Ingreso", "Estado", "Tipo", "Notas del Triage", "Triage", "Fecha de Alta", "Ambulancia de Alta", "Tipo de Alta", "Salida"};

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
			@RequestParam(value = "fromDate", required = true) String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String title = "Diabéticos Confirmados";
		String[] headers = new String[]{"ID", "Institución", "Fecha", "Prestador", "DNI-Prestador", "Paciente", "DNI-Paciente", "Problema", "Motivos", "Hemoglobina Glicosilada", "Medicación"};

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
	public @ResponseBody void getHypertensiveExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true) String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String title = "Hipertensos Confirmados";
		String[] headers = new String[]{"ID", "Institución", "Fecha", "Prestador", "DNI-Prestador", "Paciente", "DNI-Paciente", "Problema", "Motivos", "Presión Arterial", "Medicación"};

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

	@GetMapping(value = "/{institutionId}/complementaryStudies")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody void getComplementaryStudiesExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true) String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate,
			HttpServletResponse response
	) throws Exception{
		LOG.debug("Se creará el excel{}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate{}, toDate{}", institutionId);

		String title = "Reportes de Estudios Complementarios";
		String[] headers = new String[]{"Institución", "Fecha", "Categoria", "Estado de la Orden","Tipo de Solicitud", "Origen de Solicitud",
				"Nombre del Paciente", "Tipo de Documento", "Numero del Documento del Paciente", "Obra Social", "Numero de Afiliado",
				"Nombre del Profesional", "Tipo de Documento", "Numero de Documento", "Licencia", "Nota", "Fecha de Emisión",
				"Nombre del Estudio", "Notas Adicionales", "Problema Asociado"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelServiceGR.buildExcelComplementaryStudies(title,headers,this.queryFactoryGR.queryComplementaryStudies(institutionId, startDate, endDate));

		String filename = title + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename=" + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/outPatientOlderAdults")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getOutPatientOlderAdults(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true)String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate,
			HttpServletResponse response
	)throws Exception{
		LOG.debug("Se creará el excel{}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate{}, toDate{}", institutionId);

		String fechaInicio = fromDate;
		String fechaFin = toDate;

		String title = "Consultas ambulatorias del Adulto Mayor";
		String headers[] = new String[]{"Institution", "Prestador", "DNI", "Fecha de Atencion", "Hora", "Cons. N°",
		"DNI Paciente", "Nombre Paciente", "Sexo", "Fecha de Nacimiento", "Edad a Fecha del Turno", "Edad a Hoy", "Obra/s social/es", "Domicilio",
		"Localidad", "Telefono", "Problemas"};


		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelServiceGR.buildExcelOutPatientOlderAdults(title, headers, this.queryFactoryGR.queryOutPatientOlderAdults(institutionId, startDate, endDate),fechaInicio , fechaFin);

		String filename = title + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename=" + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/hospitalizationOlderAdults")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getHospitalizationOlderAdults(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true)String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate,
			HttpServletResponse response
	)throws Exception{

		String fechaInicio = fromDate;
		String fechaFin = toDate;

		String title = "Internaciones del Adulto Mayor";
		String[] headers =  new String[]{"Institution","Apellidos","Nombres","Genero","Identificación","Fecha de Nacimiento",
		"Edad a fecha de turno","Edad a hoy","Telefono","Ingreso","Alta probable","Cama","Categoria","Habitacion","Sector","Alta","Problemas"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelServiceGR.buildExcelHospitalizationOlderAdults(title,headers, this.queryFactoryGR.queryHospitalizationOlderAdults(institutionId, startDate, endDate), fechaInicio, fechaFin);

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
