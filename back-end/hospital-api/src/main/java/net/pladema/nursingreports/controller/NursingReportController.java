package net.pladema.nursingreports.controller;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.establishment.repository.entity.Institution;
import net.pladema.nursingreports.repository.NursingReportQueryFactory;
import net.pladema.nursingreports.service.NursingReportExcelService;

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
@RequestMapping("nursingreports")
public class NursingReportController {

	private static final Logger LOG = LoggerFactory.getLogger(NursingReportController.class);

	private final NursingReportExcelService excelService;

	private final NursingReportQueryFactory queryFactory;

	private final LocalDateMapper localDateMapper;

	private final InstitutionRepository institutionRepository;

	public NursingReportController(
			NursingReportQueryFactory queryFactory,
			NursingReportExcelService excelService,
			LocalDateMapper localDateMapper,
			InstitutionRepository institutionRepository
	) {
		this.queryFactory = queryFactory;
		this.excelService = excelService;
		this.localDateMapper = localDateMapper;
		this.institutionRepository = institutionRepository;
	}

	@GetMapping(value = "/{institutionId}/nursing-emergency")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getNursingEmergencyExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true) String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate{}, toDate{}", institutionId);

		String title = "Reporte de Enfermería - Emergencias";
		String[] headers = new String[]{"Institucion", "Ambulancia", "Oficina", "Sector", "Intervención Policial",
				"Fecha", "Hora", "Profesional que registró la atención", "Ultimo profesional que lo antendió",
				"Identificación", "Apellidos", "Nombres", "Sexo", "Genero", "Nombre con el que se identifica",
				"Fecha de nacimiento", "Edad a fecha del turno", "Edad a hoy", "Etnia", "Domicilio", "Localidad",
				"Obra social", "Medio de Ingreso", "Estado", "Tipo", "Notas del Triage", "Triage", "Fecha de alta",
				"Ambulancia de alta", "Tipo de alta", "Salida"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelService.buildExcelNursingEmergency(title, headers, this.queryFactory.queryNursingEmergency(institutionId, startDate, endDate));

		String filename = title + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename= " + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/nursing-outpatient")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getNursingOutpatientExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true) String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel{}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate{}, toDate{}", institutionId, fromDate, toDate);


		String title = "Reporte de Enfermería - Ambulatorio";
		String[] headers = new String[]{"Institución", "Unidad Operativa", "Prestador", "DNI Prestador",
				"Fecha de Atención", "Hora", "N° Consulta", "DNI Paciente", "Nombre Paciente", "Sexo", "Género",
				"Nombre con el que se identifica", "Fecha de Nacimiento", "Edad a fecha del turno", "Edad a Hoy",
				"Etnia", "Obra/s Social/es", "Domicilio", "Localidad", "Nivel de instrucción", "Situación Laboral",
				"Presión Sistólica", "Presión Diastólica", "Presión Arterial Media", "Temperatura",
				"Frecuencia Cardiaca", "Frecuencia Respiratoria", "Saturación de hemoglobina con oxígeno",
				"Altura", "Peso", "Índice de Masa Corporal", "Procedimientos", "Evolución"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelService.buildExcelNursingOutpatient(title, headers, this.queryFactory.queryNursingOutpatient(institutionId, startDate, endDate));

		String filename = title + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename= " + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/nursing-hospitalization")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getNursingHospitalizationExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true) String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate{}, toDate{}", institutionId, fromDate, toDate);

		String title = "Reporte de Enfermería - Internación";
		String[] headers = new String[]{"Institucion", "Apellidos", "Nombres", "Genero", "Identificacion",
				"Profesional", "Matricula", "Ingreso", "Alta Probable", "Cama", "Categoria", "Habitacion",
				"Sector", "Alta", "Procedimientos", "Signos Vitales"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelService.buildExcelNursingHospitalization(title, headers, this.queryFactory.queryNursingHospitalization(institutionId, startDate, endDate));

		String filename = title + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename=" + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/nursing-procedures")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getNursingProceduresExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true) String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel{}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate{}, toDate{}", institutionId, fromDate, toDate);

		String title = "Reporte de Enfermería - Procedimientos";
		String[] headers = new String[]{"Institución", "Origen", "Prestador", "DNI", "Fecha de atencion", "Hora",
				"DNI paciente", "Nombre paciente", "Sexo", "Genero", "Nombre con el que se identifica",
				"Fecha de nacimiento", "Edad a fecha del turno", "Edad a Hoy", "Etnia", "Obra/s sociale/es",
				"Domicilio", "Localidad", "Nivel de instruccion", "Situacion laboral", "Presión sistólica",
				"Presión diastólica", "Presión arterial media", "Temperatura", "Frecuencia cardiaca",
				"Frecuencia respiratoria", "Saturación de hemoglobina con oxígeno", "Altura" , "Peso",
				"Índice de Masa Corporal", "Procedimientos", "Problemas", "Medicacion", "Evolución"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelService.buildExcelNursingProcedures(title, headers, this.queryFactory.queryNursingProcedures(institutionId, startDate, endDate));

		String filename = title + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename=" + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/nursing-vaccine")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getNursingVaccineExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true) String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel{}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate{}, toDate{}", institutionId, fromDate, toDate);

		Institution institution = institutionRepository.getById(institutionId);
		String institutionName = (institution != null) ? institution.getName() : "";

		String title = "Reporte de Enfermería - Vacunas";
		String[] headers = new String[]{"Unidad operativa", "Prestador", "DNI Prestador",
				"Fecha de atención", "DNI Paciente", "Nombre Paciente", "Sexo", "Fecha de nacimiento",
				"Edad a fecha del turno", "Vacuna", "SCTID", "CIE10", "Estado", "Condición", "Esquema", "Dosis",
				"Lote"};


		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelService.buildExcelNursingVaccine(title, headers, this.queryFactory.queryNursingVaccine(institutionId, startDate, endDate), fromDate, toDate, institutionName);

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
