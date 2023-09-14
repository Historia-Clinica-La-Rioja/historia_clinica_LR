package net.pladema.olderadultsreports.controller;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.establishment.repository.entity.Institution;
import net.pladema.olderadultsreports.repository.OlderAdultsReportQueryFactory;
import net.pladema.olderadultsreports.service.OlderAdultsReportExcelService;

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
@RequestMapping("olderadultsreports")
public class OlderAdultsReportController {

	private static final Logger LOG = LoggerFactory.getLogger(OlderAdultsReportController.class);

	private static final String OUTPUT = "Output -> {}";

	private final OlderAdultsReportExcelService excelService;

	private final OlderAdultsReportQueryFactory queryFactory;

	private final LocalDateMapper localDateMapper;

	private final InstitutionRepository institutionRepository;

	public OlderAdultsReportController(
			OlderAdultsReportQueryFactory queryFactory,
			OlderAdultsReportExcelService excelService,
			LocalDateMapper localDateMapper,
			InstitutionRepository institutionRepository
	) {
		this.queryFactory = queryFactory;
		this.excelService = excelService;
		this.localDateMapper = localDateMapper;
		this.institutionRepository = institutionRepository;
	}

	@GetMapping(value = "/{institutionId}/older-adults-outpatient")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getOlderAdultsOutpatientExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true)String fromDate,
			@RequestParam(value = "toDate", required = true)String toDate,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel{}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate{}, toDate{}", institutionId);

		String title = "Reporte del Adulto Mayor - Ambulatoria";
		String[] headers = new String[]{"Institution", "Prestador", "DNI", "Fecha de Atencion", "Hora", "Cons. N°",
				"DNI Paciente", "Nombre Paciente", "Sexo", "Fecha de Nacimiento", "Edad a Fecha del Turno", "Edad a Hoy", "Obra/s social/es", "Domicilio",
				"Localidad", "Telefono", "Problemas"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelService.buildExcelOlderAdultsOutpatient(title, headers, this.queryFactory.queryOlderAdultsOutpatient(institutionId, startDate, endDate), fromDate, toDate);

		String filename = title + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename=" + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/older-adults-hospitalization")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getOlderAdultsHospitalizationExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true)String fromDate,
			@RequestParam(value = "toDate", required = true)String toDate,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel{}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate{}, toDate{}", institutionId);

		String title = "Reporte del Adulto Mayor - Internaciones";
		String[] headers =  new String[]{"Institution","Apellidos","Nombres","Genero","Identificación","Fecha de Nacimiento",
				"Edad a fecha de turno","Edad a hoy","Telefono","Ingreso","Alta probable","Cama","Categoria","Habitacion","Sector","Alta","Problemas"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelService.buildExcelOlderAdultsHospitalization(title, headers, this.queryFactory.queryOlderAdultsHospitalization(institutionId, startDate, endDate), fromDate, toDate);

		String filename = title + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename=" + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/polypharmacy")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getPolypharmacyExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true)String fromDate,
			@RequestParam(value = "toDate", required = true)String toDate,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate {}", institutionId, fromDate, toDate);

		Institution institution = institutionRepository.getById(institutionId);
		String institutionName = (institution != null) ? institution.getName() : "";

		String title = "Reporte de Adulto mayor - Polifarmacia";
		String[] headers = {"Nombre de paciente", "Tipo de identificación", "Núm. de identificación", "Sexo", "Género",
				"Nombre con que se identifica", "Edad a fecha de atención", "Obra social", "Medicación", "SNOMED", "Tipo de origen",
				"¿Crónico?", "Problema asociado", "Fecha de atención", "Institución", "Profesional emisor",
				"Num. de identificación del profesional", "Licencia", "Especialidad del profesional"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelService.buildExcelPolypharmacy(title, headers, this.queryFactory.queryPolypharmacy(institutionId, startDate, endDate), fromDate, toDate, institutionName);

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
