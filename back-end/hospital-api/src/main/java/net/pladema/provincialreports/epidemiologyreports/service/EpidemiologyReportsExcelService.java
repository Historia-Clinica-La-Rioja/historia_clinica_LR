package net.pladema.provincialreports.epidemiologyreports.service;

import ar.lamansys.sgx.shared.reports.util.manager.WorkbookCreator;
import ar.lamansys.sgx.shared.reports.util.struct.ICellStyle;
import ar.lamansys.sgx.shared.reports.util.struct.IRow;
import ar.lamansys.sgx.shared.reports.util.struct.ISheet;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.provincialreports.epidemiologyreports.repository.CompleteDengueConsultationDetail;
import net.pladema.provincialreports.epidemiologyreports.repository.DenguePatientControlConsultationDetail;
import net.pladema.provincialreports.reportformat.DateFormat;

import net.pladema.provincialreports.reportformat.domain.service.ReportExcelUtilsService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class EpidemiologyReportsExcelService {
	private final static String DENGUE_CONTROLS_OBSERVATION = "Este reporte presenta los posibles casos de dengue registrados entre 00:00 y 23:59 horas del día de emisión. Incluye aquellos \nque mencionan 'dengue', 'flavivirus', 'fiebre hemorrágica' y los códigos asociados a estos. Se recomienda ver\n el reporte 'Atenciones relacionadas al dengue - Consultas completas' para confirmar o descartar posibles 'falsos positivos'";
	private final static String DENGUE_COMPLETE_CONSULTATIONS_OBSERVATION = "Este reporte presenta los posibles casos de dengue registrados entre las 00:00 y 23:59 del día de emisión. Brinda información sobre motivos, problemas, procedimientos, evoluciones de las consultas y datos de signos vitales de pacientes relacionados con dengue, flavivirus y fiebre hemorrágica. Para una visión integral, consulte el reporte 'Atenciones relacionadas al dengue - Control de pacientes'.";

	private final DateFormat dateTools;

	private final ReportExcelUtilsService excelUtilsService;

	public EpidemiologyReportsExcelService(DateFormat dateTools, ReportExcelUtilsService excelUtilsService) {
		this.dateTools = dateTools;
		this.excelUtilsService = excelUtilsService;
	}

	public IWorkbook buildDenguePatientControlExcel(String title, String[] headers, List<DenguePatientControlConsultationDetail> result, Integer institutionId) {
		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();
		excelUtilsService.createHeaderCellsStyle(workbook);
		ISheet sheet = workbook.createSheet(title);
		excelUtilsService.fillRow(sheet, excelUtilsService.getHeaderDataWithoutObservation(headers, title, 11, 1, null, institutionId, DENGUE_CONTROLS_OBSERVATION));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle dataCellsStyle = excelUtilsService.createDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillDenguePatientControlRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.setMinimalHeaderDimensions(sheet);
		excelUtilsService.setSheetDimensions(sheet);

		return workbook;
	}

	public IWorkbook buildCompleteDengueConsultationExcel(String title, String[] headers, List<CompleteDengueConsultationDetail> result, Integer institutionId) {
		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();
		excelUtilsService.createHeaderCellsStyle(workbook);
		ISheet sheet = workbook.createSheet(title);
		excelUtilsService.fillRow(sheet, excelUtilsService.getHeaderDataWithoutObservation(headers, title, 21, 0, null, institutionId, DENGUE_COMPLETE_CONSULTATIONS_OBSERVATION));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle dataCellsStyle = excelUtilsService.createDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillCompleteDengueConsultationRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.setMinimalHeaderDimensions(sheet);
		excelUtilsService.setSheetDimensions(sheet);

		return workbook;
	}

	public void fillDenguePatientControlRow(IRow row, DenguePatientControlConsultationDetail content, ICellStyle style) {
		excelUtilsService.setCellValue(row, 0, style, content.getIsFalsePositive());
		excelUtilsService.setCellValue(row, 1, style, content.getPatientIdentificationNumber());
		excelUtilsService.setCellValue(row, 2, style, content.getPatientLastName() + " " + content.getPatientFirstName());
		excelUtilsService.setCellValue(row, 3, style, content.getPatientSex());
		excelUtilsService.setCellValue(row, 4, style, dateTools.newReformatDate(content.getPatientBirthDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 5, style, content.getPatientAge());
		excelUtilsService.setCellValue(row, 6, style, content.getPatientAddress());
		excelUtilsService.setCellValue(row, 7, style, content.getPatientPhoneNumber());
		excelUtilsService.setCellValue(row, 8, style, content.getPatientLocation());
	}

	public void fillCompleteDengueConsultationRow(IRow row, CompleteDengueConsultationDetail content, ICellStyle style) {
		excelUtilsService.setCellValue(row, 0, style, content.getIsFalsePositive());
		excelUtilsService.setCellValue(row, 1, style, content.getOrigin());
		excelUtilsService.setCellValue(row, 2, style, content.getOperativeUnit());
		excelUtilsService.setCellValue(row, 3, style, content.getPatientIdentificationNumber());
		excelUtilsService.setCellValue(row, 4, style, content.getPatientLastName() + " " + content.getPatientFirstName());
		excelUtilsService.setCellValue(row, 5, style, content.getPatientSex());
		excelUtilsService.setCellValue(row, 6, style, dateTools.newReformatDate(content.getPatientBirthDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 7, style, content.getPatientAge());
		excelUtilsService.setCellValue(row, 8, style, content.getAttentionHour());
		excelUtilsService.setCellValue(row, 9, style, content.getMedicalCoverage());
		excelUtilsService.setCellValue(row, 10, style, content.getSystolicPressure());
		excelUtilsService.setCellValue(row, 11, style, content.getDiastolicPressure());
		excelUtilsService.setCellValue(row, 12, style, content.getMeanArterialPressure());
		excelUtilsService.setCellValue(row, 13, style, content.getTemperature());
		excelUtilsService.setCellValue(row, 14, style, content.getHeartRate());
		excelUtilsService.setCellValue(row, 15, style, content.getRespiratoryRate());
		excelUtilsService.setCellValue(row, 16, style, content.getBloodOxygenSaturation());
		excelUtilsService.setCellValue(row, 17, style, content.getHeight());
		excelUtilsService.setCellValue(row, 18, style, content.getWeight());
		excelUtilsService.setCellValue(row, 19, style, content.getBmi());
		excelUtilsService.setCellValue(row, 20, style, content.getReason());
		excelUtilsService.setCellValue(row, 21, style, content.getProblems());
		excelUtilsService.setCellValue(row, 22, style, content.getProcedures());
		excelUtilsService.setCellValue(row, 23, style, content.getEvolution());

	}
}
