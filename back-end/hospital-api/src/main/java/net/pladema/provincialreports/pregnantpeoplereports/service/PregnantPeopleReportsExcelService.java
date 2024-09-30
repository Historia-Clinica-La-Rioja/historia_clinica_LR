package net.pladema.provincialreports.pregnantpeoplereports.service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.reports.util.manager.WorkbookCreator;
import ar.lamansys.sgx.shared.reports.util.struct.ICellStyle;
import ar.lamansys.sgx.shared.reports.util.struct.IRow;
import ar.lamansys.sgx.shared.reports.util.struct.ISheet;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.provincialreports.pregnantpeoplereports.repository.PregnantAttentionsConsultationDetail;
import net.pladema.provincialreports.pregnantpeoplereports.repository.PregnantControlsConsultationDetail;
import net.pladema.provincialreports.reportformat.DateFormat;
import net.pladema.provincialreports.reportformat.domain.service.ReportExcelUtilsService;

@Service
public class PregnantPeopleReportsExcelService {
	private static final String PREGNANT_CONTROLS_OBSERVATION = "Observación: este informe presenta a las personas gestantes que han tenido al menos un control prenatal durante un período que incluye los 10 meses previos a la fecha de inicio especificada, manteniendo constante la fecha de fin";

	private final DateFormat dateTools;

	private final ReportExcelUtilsService excelUtilsService;

	public PregnantPeopleReportsExcelService(DateFormat dateTools, ReportExcelUtilsService excelUtilsService) {
		this.dateTools = dateTools;
		this.excelUtilsService = excelUtilsService;
	}

	public IWorkbook buildPregnantAttentionsExcel(String title, String[] headers, List<PregnantAttentionsConsultationDetail> result, Integer institutionId, LocalDate startDate, LocalDate endDate) {
		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();
		excelUtilsService.newCreateHeaderCellsStyle(workbook);
		ISheet sheet = workbook.createSheet(title);
		excelUtilsService.newFillRow(sheet, excelUtilsService.newGetHeaderDataWithoutObservation(headers, title, 11, 0, excelUtilsService.newPeriodStringFromLocalDates(startDate, endDate), institutionId, null));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle dataCellsStyle = excelUtilsService.newCreateDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillPregnantAttentionsRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.newSetMinimalHeaderDimensions(sheet);
		excelUtilsService.newSetSheetDimensions(sheet);

		return workbook;
	}

	public IWorkbook buildPregnantControlsExcel(String title, String[] headers, List<PregnantControlsConsultationDetail> result, Integer institutionId, LocalDate startDate, LocalDate endDate) {
		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();
		excelUtilsService.newCreateHeaderCellsStyle(workbook);
		ISheet sheet = workbook.createSheet(title);
		excelUtilsService.newFillRow(sheet, excelUtilsService.newGetHeaderDataWithoutObservation(headers, title, 6, 0, excelUtilsService.newPeriodStringFromLocalDates(startDate, endDate), institutionId, PREGNANT_CONTROLS_OBSERVATION));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle dataCellsStyle = excelUtilsService.newCreateDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillPregnantControlsRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.newSetMinimalHeaderDimensions(sheet);
		excelUtilsService.newSetSheetDimensions(sheet);

		return workbook;
	}

	public void fillPregnantAttentionsRow(IRow row, PregnantAttentionsConsultationDetail content, ICellStyle style) {
		excelUtilsService.setCellValue(row, 0, style, content.getOperativeUnit());
		excelUtilsService.setCellValue(row, 1, style, content.getPatientIdentificationNumber());
		excelUtilsService.setCellValue(row, 2, style, content.getPatientLastName() + " " +content.getPatientFirstName());
		excelUtilsService.setCellValue(row, 3, style, dateTools.newReformatDate(content.getPatientBirthDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 4, style, content.getPatientAgeAtAttention());
		excelUtilsService.setCellValue(row, 5, style, content.getPatientAddress());
		excelUtilsService.setCellValue(row, 6, style, content.getPhoneNumber());
		excelUtilsService.setCellValue(row, 7, style, content.getPatientLocation());
		excelUtilsService.setCellValue(row, 8, style, dateTools.newReformatDate(content.getAttentionDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 9, style, content.getPatientMedicalCoverage());
		excelUtilsService.setCellValue(row, 10, style, content.getReasons());
		excelUtilsService.setCellValue(row, 11, style, content.getProblems());
		excelUtilsService.setCellValue(row, 12, style, content.getProcedures());
		excelUtilsService.setCellValue(row, 13, style, content.getEvolution());
	}

	public void fillPregnantControlsRow(IRow row, PregnantControlsConsultationDetail content, ICellStyle style) {
		excelUtilsService.setCellValue(row, 0, style, content.getPatientIdentificationNumber());
		excelUtilsService.setCellValue(row, 1, style, content.getPatientLastName() + " " + content.getPatientFirstName());
		excelUtilsService.setCellValue(row, 2, style, content.getPatientBirthDate());
		excelUtilsService.setCellValue(row, 3, style, content.getPatientAgeAtAttention());
		excelUtilsService.setCellValue(row, 4, style, content.getPatientAddress());
		excelUtilsService.setCellValue(row, 5, style, content.getPhoneNumber());
		excelUtilsService.setCellValue(row, 6, style, content.getPatientLocation());
		excelUtilsService.setCellValue(row, 7, style, content.getNumberConsultations());
		excelUtilsService.setCellValue(row, 8, style, content.getConsultations());
	}
}
