package net.pladema.generalreports.controller;

import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.generalreports.repository.QueryFactoryGR;
import net.pladema.generalreports.service.ExcelServiceGR;

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
@RequestMapping("generalreports")
public class GeneralReportsController {

	private static final Logger LOG = LoggerFactory.getLogger(GeneralReportsController.class);

	private static final String OUTPUT = "Output -> {}";

	private final ExcelServiceGR excelServiceGR;

	private final QueryFactoryGR queryFactoryGR;

	public GeneralReportsController(QueryFactoryGR queryFactoryGR, ExcelServiceGR excelServiceGR){
		this.queryFactoryGR = queryFactoryGR;
		this.excelServiceGR = excelServiceGR;
	}

	@GetMapping(value = "/{institutionId}/dailyEmergency")
	public @ResponseBody
	void getDaiylyEmergencyExcelReport(
			@PathVariable Integer institutionId,
			HttpServletResponse response
	)throws Exception{
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String title = "Emergencias";
		String[] headers = new String[] {"Institución", "Ambulancia", "Oficina", "Sector", "Intervención Policial", "Fecha", "Identificación", "Apellidos", "Nombres",
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
	public @ResponseBody
	void getDiabeticsExcelReport(
			@PathVariable Integer institutionId,
			HttpServletResponse response
	)throws Exception{
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String title = "Diabéticos Confirmados";
		String[] headers = new String[] {"ID", "Institución", "Fecha", "Prestador", "DNI-Prestador", "Paciente", "DNI-Paciente", "Problema", "Motivos",
			"Hemoglobina Glicosilada", "Medicación"};

		IWorkbook wb = this.excelServiceGR.buildExcelDiabeticosHipertensos(title, headers, this.queryFactoryGR.queryDiabeticos(institutionId));

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
	public @ResponseBody
	void getHypertensiveExcelReport(
			@PathVariable Integer institutionId,
			HttpServletResponse response
	)throws Exception{
		LOG.debug("Se creará el excel {}", institutionId);
		LOG.debug("Inputs parameters -> institutionId {}, fromDate {}, toDate{}", institutionId);

		String title = "Hipertensos Confirmados";
		String[] headers = new String[] {"ID", "Institución", "Fecha", "Prestador", "DNI-Prestador", "Paciente", "DNI-Paciente", "Problema", "Motivos",
				"Presión Arterial", "Medicación"};

		IWorkbook wb = this.excelServiceGR.buildExcelDiabeticosHipertensos(title, headers, this.queryFactoryGR.queryHipertensos(institutionId));

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
