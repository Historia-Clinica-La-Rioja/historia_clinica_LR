package net.pladema.odontologyreport.controller;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.odontologyreport.repository.QueryFactoryOdontology;
import net.pladema.odontologyreport.service.ExcelServiceOdontology;
import net.pladema.programreports.controller.ProgramReportsController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import java.io.OutputStream;

@RestController
@RequestMapping("odontologyreports")
public class OdontologyReportsController {

	private static final Logger LOG = LoggerFactory.getLogger(ProgramReportsController.class);

	private static final String OUTPUT = "Output -> {}";

	private final ExcelServiceOdontology excelService;

	private final QueryFactoryOdontology queryFactoryOdontology;

	public OdontologyReportsController(ExcelServiceOdontology excelService, QueryFactoryOdontology queryFactoryOdontology) {
		this.excelService = excelService;
		this.queryFactoryOdontology = queryFactoryOdontology;
	}

	String [] headers = new String[] {"Institución", "Profesional", "Procedimiento", "Total"};

	@GetMapping(value = "/{institutionId}/monthlyPromocionPrimerNivel")
	public @ResponseBody
	void getMonthlyPromocionExcelReport(
			@PathVariable Integer institutionId,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String tittle = "Consultas de Odontologia - Promoción Primer Nivel";

		IWorkbook wb = this.excelService.buildExcelOdontology(tittle, headers, this.queryFactoryOdontology.queryReporteIPromocion(institutionId));

		String filename = tittle + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename=" + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/monthlyPrevencionPrimerNivel")
	public @ResponseBody
	void getMonthlyPrevencionExcelReport(
			@PathVariable Integer institutionId,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String tittle = "Consultas de Odontologia - Prevención Primer Nivel";

		IWorkbook wb = this.excelService.buildExcelOdontology(tittle, headers, this.queryFactoryOdontology.queryReporteIIPrevencion(institutionId));

		String filename = tittle + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename=" + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/monthlyPrevencionGrupalPrimerNivel")
	public @ResponseBody
	void getMonthlyPrevencionGrupalExcelReport(
			@PathVariable Integer institutionId,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String tittle = "Consultas de Odontologia - Prevención Grupal Primer Nivel";

		IWorkbook wb = this.excelService.buildExcelOdontology(tittle, headers, this.queryFactoryOdontology.queryReporteIIIPrevencionGrupal(institutionId));

		String filename = tittle + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename=" + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/monthlyOperatoriaSegundoNivel")
	public @ResponseBody
	void getMonthlyOperatoriaExcelReport(
			@PathVariable Integer institutionId,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String tittle = "Consultas de Odontologia - Operatoria Segundo Nivel";

		IWorkbook wb = this.excelService.buildExcelOdontology(tittle, headers, this.queryFactoryOdontology.queryReporteIVOperatoria(institutionId));

		String filename = tittle + "." + wb.getExtension();
		response.addHeader("Content-disposition", "attachment;filename=" + filename);
		response.setContentType(wb.getContentType());

		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.close();
		out.flush();
		response.flushBuffer();
	}

	@GetMapping(value = "/{institutionId}/monthlyEndodonciaSegundoNivel")
	public @ResponseBody
	void getMonthlyEndodonciaExcelReport(
			@PathVariable Integer institutionId,
			HttpServletResponse response
	) throws Exception {
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String tittle = "Consultas de Odontologia - Endodoncia Segundo Nivel";

		IWorkbook wb = this.excelService.buildExcelOdontology(tittle, headers, this.queryFactoryOdontology.queryReporteVEndodoncia(institutionId));

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
