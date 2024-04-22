package net.pladema.provincialreports.epidemiologyreports.controller;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.establishment.repository.entity.Institution;
import net.pladema.provincialreports.epidemiologyreports.repository.EpidemiologyReportQueryFactory;
import net.pladema.provincialreports.epidemiologyreports.service.EpidemiologyReportExcelService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import java.io.OutputStream;

@RestController
@RequestMapping("epidemiologyreports")
public class EpidemiologyReportController {

	private static final Logger LOG = LoggerFactory.getLogger(EpidemiologyReportController.class);

	private static final String OUTPUT = "Output -> {}";

	private final EpidemiologyReportExcelService excelService;

	private final EpidemiologyReportQueryFactory queryFactory;

	private final LocalDateMapper localDateMapper;

	private final InstitutionRepository institutionRepository;

	public EpidemiologyReportController(
			EpidemiologyReportExcelService excelService,
			EpidemiologyReportQueryFactory queryFactory,
			LocalDateMapper localDateMapper,
			InstitutionRepository institutionRepository
	) {
		this.excelService = excelService;
		this.queryFactory = queryFactory;
		this.localDateMapper = localDateMapper;
		this.institutionRepository = institutionRepository;
	}

	@GetMapping(value = "/{institutionId}/dengue-patient-control")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA')")
	public @ResponseBody
	void getDenguePatientControlExcelReport(
			@PathVariable Integer institutionId,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}", institutionId);

		Institution institution = institutionRepository.getById(institutionId);
		String institutionName = (institution != null) ? institution.getName() : "";

		String observations = "Este reporte presenta los posibles casos de dengue registrados entre las 00:00 y las 23:59 del día actual. " +
				"Incluye aquellos que mencionan \"Dengue\", \"flavivirus\" o \"fiebre hemorrágica\" y sus códigos asociados. " +
				"Se recomienda revisar el reporte \"Atenciones Relacionadas al Dengue: Consultas Completas\" para confirmar o descartar casos marcados como \"posibles falsos positivos\"";

		String title = "Atenciones Relacionadas al Dengue: Control de Pacientes";
		String[] headers = {"Posible falso positivo", "DNI del paciente", "Apellido", "Nombre", "Sexo", "Fecha de nacimiento", "Edad", "Domicilio",
				"Teléfono", "Localidad"};

		IWorkbook wb = this.excelService.buildExcelDenguePatientControl(title, headers, this.queryFactory.queryDenguePatientControl(institutionId), institutionName, observations);

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
