package net.pladema.provincialreports.generalreports.service;

import ar.lamansys.sgx.shared.reports.util.CellContent;
import ar.lamansys.sgx.shared.reports.util.manager.WorkbookCreator;
import ar.lamansys.sgx.shared.reports.util.struct.ICellStyle;
import ar.lamansys.sgx.shared.reports.util.struct.IRow;
import ar.lamansys.sgx.shared.reports.util.struct.ISheet;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.provincialreports.generalreports.repository.DiabeticHypertensionConsultationDetail;
import net.pladema.provincialreports.generalreports.repository.EmergencyConsultationDetail;
import net.pladema.provincialreports.reportformat.DateFormat;

import net.pladema.provincialreports.reportformat.domain.service.ReportExcelUtilsService;

import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class NewGeneralReportsExcelService {

	private final DateFormat dateTools;

	private final ReportExcelUtilsService excelUtilsService;

	public NewGeneralReportsExcelService(DateFormat dateTools, ReportExcelUtilsService excelUtilsService) {
		this.dateTools = dateTools;
		this.excelUtilsService = excelUtilsService;
	}

	public IWorkbook buildEmergencyExcel(String title, String[] headers, List<EmergencyConsultationDetail> result, Integer institutionId, LocalDate startDate, LocalDate endDate) {
		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();
		excelUtilsService.newCreateHeaderCellsStyle(workbook);
		ISheet sheet = workbook.createSheet(title);
		excelUtilsService.newFillRow(sheet, excelUtilsService.newGetHeaderDataWithoutObservation(headers, title, 16, 1, excelUtilsService.newPeriodStringFromLocalDates(startDate, endDate), institutionId));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle dataCellsStyle = excelUtilsService.newCreateDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillEmergencyRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.newFillRow(sheet, excelUtilsService.addTotalCountRow(workbook, sheet, 4, 16, 7));

		excelUtilsService.newSetMinimalHeaderDimensions(sheet);
		excelUtilsService.newSetSheetDimensions(sheet);

		return workbook;
	}

	public IWorkbook buildDiabeticsExcel(String title, String[] headers, List<DiabeticHypertensionConsultationDetail> result, Integer institutionId, LocalDate startDate, LocalDate endDate) {
		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();
		excelUtilsService.newCreateHeaderCellsStyle(workbook);
		ISheet sheet = workbook.createSheet(title);
		excelUtilsService.newFillRow(sheet, excelUtilsService.newGetHeaderDataWithoutObservation(headers, title, 11, 0, excelUtilsService.newPeriodStringFromLocalDates(startDate, endDate), institutionId));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle dataCellsStyle = excelUtilsService.newCreateDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillDiabeticsRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.newSetMinimalHeaderDimensions(sheet);
		excelUtilsService.newSetSheetDimensions(sheet);

		return workbook;
	}

	public void fillEmergencyRow(IRow row, EmergencyConsultationDetail content, ICellStyle style) {
		excelUtilsService.setCellValue(row, 0, style, content.getIdentification());
		excelUtilsService.setCellValue(row, 1, style, content.getLastNames());
		excelUtilsService.setCellValue(row, 2, style, content.getNames());
		excelUtilsService.setCellValue(row, 3, style, content.getMedicalCoverage());
		excelUtilsService.setCellValue(row, 4, style, dateTools.newReformatDate(content.getAttentionDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 5, style, dateTools.standardizeTime(content.getAttentionHour()));
		excelUtilsService.setCellValue(row, 6, style, content.getTypeOfEntry());
		excelUtilsService.setCellValue(row, 7, style, content.getAmbulance());
		excelUtilsService.setCellValue(row, 8, style, content.getOffice());
		excelUtilsService.setCellValue(row, 9, style, content.getSector());
		excelUtilsService.setCellValue(row, 10, style, content.getSituation());
		excelUtilsService.setCellValue(row, 11, style, content.getEmergencyCareType());
		excelUtilsService.setCellValue(row, 12, style, content.getTriageNotes());
		excelUtilsService.setCellValue(row, 13, style, content.getTriageLevel());
		excelUtilsService.setCellValue(row, 14, style, content.getDischargeDate());
		excelUtilsService.setCellValue(row, 15, style, content.getDischargeAmbulance());
		excelUtilsService.setCellValue(row, 16, style, content.getDischargeType());
		excelUtilsService.setCellValue(row, 17, style, content.getPatientExit());
		excelUtilsService.setCellValue(row, 18, style, content.getPoliceIntervention());
	}

	public void fillDiabeticsRow(IRow row, DiabeticHypertensionConsultationDetail content, ICellStyle style) {
		excelUtilsService.setCellValue(row, 0, style, content.getAttentionDate());
		excelUtilsService.setCellValue(row, 1, style, content.getLenderLastNames());
		excelUtilsService.setCellValue(row, 2, style, content.getLenderNames());
		excelUtilsService.setCellValue(row, 3, style, content.getLenderDni());
		excelUtilsService.setCellValue(row, 4, style, content.getPatientLastNames());
		excelUtilsService.setCellValue(row, 5, style, content.getPatientNames());
		excelUtilsService.setCellValue(row, 6, style, content.getPatientDni());
		excelUtilsService.setCellValue(row, 7, style, content.getGender());
		excelUtilsService.setCellValue(row, 8, style, content.getBirthDate());
		excelUtilsService.setCellValue(row, 9, style, content.getAgeTurn());
		excelUtilsService.setCellValue(row, 10, style, content.getReasons());
		excelUtilsService.setCellValue(row, 11, style, content.getProblem());
		excelUtilsService.setCellValue(row, 12, style, content.getGlycosylatedHemoglobinBloodPressure());
		excelUtilsService.setCellValue(row, 13, style, content.getMedication());
	}
}
