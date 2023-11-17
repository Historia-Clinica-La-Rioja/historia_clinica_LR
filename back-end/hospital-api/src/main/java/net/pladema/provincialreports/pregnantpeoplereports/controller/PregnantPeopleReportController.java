package net.pladema.provincialreports.pregnantpeoplereports.controller;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.establishment.repository.entity.Institution;
import net.pladema.provincialreports.pregnantpeoplereports.repository.PregnantPeopleReportQueryFactory;
import net.pladema.provincialreports.pregnantpeoplereports.service.PregnantPeopleReportExcelService;

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
@RequestMapping("pregnantpeoplereports")
public class PregnantPeopleReportController {

	private static final Logger LOG = LoggerFactory.getLogger(PregnantPeopleReportController.class);

	private static final String OUTPUT = "Output -> {}";

	private final PregnantPeopleReportExcelService excelService;

	private final PregnantPeopleReportQueryFactory queryFactory;

	private final LocalDateMapper localDateMapper;

	private InstitutionRepository institutionRepository;

	public PregnantPeopleReportController(
			PregnantPeopleReportQueryFactory queryFactory,
			PregnantPeopleReportExcelService excelService,
			LocalDateMapper localDateMapper,
			InstitutionRepository institutionRepository
	) {
		this.queryFactory = queryFactory;
		this.excelService = excelService;
		this.localDateMapper = localDateMapper;
		this.institutionRepository = institutionRepository;
	}

	@GetMapping(value = "/{institutionId}/pregnant-attentions")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getPregnantAttentionsExcelReport(
			@PathVariable Integer institutionId,
			@RequestParam(value = "fromDate", required = true)String fromDate,
			@RequestParam(value = "toDate", required = true)String toDate,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate {}", institutionId, fromDate, toDate);

		Institution institution = institutionRepository.getById(institutionId);
		String institutionName = (institution != null) ? institution.getName() : "";

		String title = "Atenciones a Personas Gestantes";
		String[] headers = { "Unidad operativa", "DNI del paciente", "Apellido", "Nombre", "Fecha de nacimiento", "Edad actual",
				"Domicilio", "Teléfono", "Localidad", "Fecha de atención", "Obra social", "Motivo de la atención", "Problemas",
				"Procedimientos", "Evolución"};

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = this.excelService.buildExcelPregnantAttentions(title, headers, this.queryFactory.queryPregnantAttentions(institutionId, startDate, endDate), fromDate, toDate, institutionName);

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
