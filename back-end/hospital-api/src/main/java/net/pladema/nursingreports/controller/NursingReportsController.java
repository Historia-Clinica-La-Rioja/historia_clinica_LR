package net.pladema.nursingreports.controller;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.nursingreports.repository.QueryFactoryNR;
import net.pladema.nursingreports.service.ExcelServiceNR;

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
public class NursingReportsController {

	private static final Logger LOG = LoggerFactory.getLogger(NursingReportsController.class);

	private final ExcelServiceNR excelServiceNR;

	private final QueryFactoryNR queryFactoryNR;

	private final LocalDateMapper localDateMapper;

	public NursingReportsController(ExcelServiceNR excelServiceNR, QueryFactoryNR queryFactoryNR, LocalDateMapper localDateMapper) {
		this.excelServiceNR = excelServiceNR;
		this.queryFactoryNR = queryFactoryNR;
		this.localDateMapper = localDateMapper;
	}

	@GetMapping(value = "/{institutionId}/hospitalizationNursing")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody void getHospitalizationNursingExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true) String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate{}, toDate{}", institutionId, fromDate, toDate);

		String title = "Reporte Enfermeria - Enfermeria Internacion";
		String[] headers = new String[]{"Institucion", "Apellidos", "Nombres", "Genero", "Identificacion",
				"Profesional", "Matricula", "Ingreso", "Alta Probable", "Cama", "Categoria", "Habitacion",
				"Sector", "Alta", "Procedimientos", "Signos Vitales"
		};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelServiceNR.buildExcelHospitalizationNursing(title, headers, this.queryFactoryNR.queryHospitalizationNursing(institutionId, startDate, endDate));

		String filename = title + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename=" + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/outpatientNursing")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody void getOutpatientNursingExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true) String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel{}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate{}, toDate{}", institutionId, fromDate, toDate);

		String title = "Reporte Enfemeria - Enfermeria Ambulatorio";
		String[] headers = new String[]{"Institución", "Unidad Operativa", "Prestador", "DNI Prestador",
				"Fecha de Atención", "Hora", "N° Consulta", "DNI Paciente", "Nombre Paciente", "Sexo", "Género",
				"Nombre con el que se identifica", "Fecha de Nacimiento", "Edad a fecha del turno", "Edad a Hoy",
				"Etnia", "Obra/s Social/es", "Domicilio", "Localidad", "Nivel de instrucción", "Situación Laboral",
				"Presión Sistólica", "Presión Diastólica", "Presión Arterial Media", "Temperatura",
				"Frecuencia Cardiaca", "Frecuencia Respiratoria", "Saturación de hemoglobina con oxígeno",
				"Altura", "Peso", "Índice de Masa Corporal", "Procedimientos", "Evolución"
		};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelServiceNR.buildExcelOutpatientNursing(title, headers, this.queryFactoryNR.queryOutpatientNursing(institutionId, startDate, endDate));

		String filename = title + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename= " + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/nursingEmergencies")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody void getNursingEmergenciesExcelReport(
			@PathVariable Integer institutionId,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel{}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate {}", institutionId);

		String title = "Reporte Enfermeria - Emergencias Pacientes";
		String[] headers = new String[]{"Institucion", "Ambulancia", "Oficina", "Sector", "Intervención Policial",
				"Fecha", "Hora", "Profesional que registró la atención", "Ultimo profesional que lo antendió",
				"Identificación", "Apellidos", "Nombres", "Sexo", "Genero", "Nombre con el que se identifica",
				"Fecha de nacimiento", "Edad a fecha del turno", "Edad a hoy", "Etnia", "Domicilio", "Localidad",
				"Obra social", "Medio de Ingreso", "Estado", "Tipo", "Notas del Triage", "Triage", "Fecha de alta",
				"Ambulancia de alta", "Tipo de alta", "Salida"
		};

		IWorkbook wb = this.excelServiceNR.buildExcelNursingEmergencies(title, headers, this.queryFactoryNR.queryNursingEmergencies(institutionId));

		String filename = title + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename= " + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/totalNursingRecovery")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody void getTotalNursingRecoveryExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true) String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel{}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate{}, toDate{}", institutionId, fromDate, toDate);

		String title = "Reporte de procedimientos realizados por Enfermeria";
		String[] headers = new String[]{"Institución", "Origen", "Prestador", "DNI", "Fecha de atencion", "Hora",
				"DNI paciente", "Nombre paciente", "Sexo", "Genero", "Nombre con el que se identifica",
				"Fecha de nacimiento", "Edad a fecha del turno", "Edad a Hoy", "Etnia", "Obra/s sociale/es",
				"Domicilio", "Localidad", "Nivel de instruccion", "Situacion laboral", "Presión sistólica",
				"Presión diastólica", "Presión arterial media", "Temperatura", "Frecuencia cardiaca",
				"Frecuencia respiratoria", "Saturación de hemoglobina con oxígeno", "Altura" , "Peso",
				"Índice de Masa Corporal", "Procedimientos", "Problemas", "Medicacion", "Evolución"
		};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelServiceNR.buildExcelTotalNursingRecovery(title, headers, this.queryFactoryNR.queryTotalNursingRecovery(institutionId, startDate, endDate));

		String filename = title + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename=" + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/vaccinesNursing")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody void getVaccinesNursingExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true) String fromDate,
			@RequestParam(value = "toDate", required = true) String toDate,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel{}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate{}, toDate{}", institutionId, fromDate, toDate);

		String title = "Reporte de Enfermería - Vacunas";
		String[] headers = new String[]{"Institución", "Unidad operativa", "Prestador", "DNI Prestador",
				"Fecha de atención", "DNI Paciente", "Nombre Paciente", "Sexo", "Fecha de nacimiento",
				"Edad a fecha del turno", "Vacuna", "SCTID", "CIE10", "Estado", "Condición", "Esquema", "Dosis",
				"Lote"
		};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelServiceNR.buildExcelVaccinesNursing(title, headers, this.queryFactoryNR.queryVaccinesNursing(institutionId, startDate, endDate));

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
