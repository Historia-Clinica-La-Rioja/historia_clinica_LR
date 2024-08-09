package net.pladema.provincialreports.generalreports.service;

import ar.lamansys.sgx.shared.reports.util.manager.WorkbookCreator;
import ar.lamansys.sgx.shared.reports.util.struct.ICellStyle;
import ar.lamansys.sgx.shared.reports.util.struct.IRow;
import ar.lamansys.sgx.shared.reports.util.struct.ISheet;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.provincialreports.generalreports.repository.EmergencyConsultationDetail;
import net.pladema.provincialreports.reportformat.DateFormat;

import net.pladema.provincialreports.reportformat.domain.service.ReportExcelUtilsService;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
		excelUtilsService.newFillRow(sheet, excelUtilsService.newGetHeaderDataWithoutObservation(headers, title, 7, excelUtilsService.newPeriodStringFromLocalDates(startDate, endDate), institutionId));
		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle dataCellsStyle = excelUtilsService.newCreateDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillEmergencyRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.newSetMinimalHeaderDimensions(sheet);
		excelUtilsService.newSetSheetDimensions(sheet);

		return workbook;
	}

	public void fillEmergencyRow(IRow row, EmergencyConsultationDetail content, ICellStyle style) {
		excelUtilsService.setCellValue(row, 0, style, content.getIdentification());
		excelUtilsService.setCellValue(row, 1, style, content.getLastName());
		excelUtilsService.setCellValue(row, 2, style, content.getNames());
		excelUtilsService.setCellValue(row, 3, style, content.getMedicalCoverage());
		excelUtilsService.setCellValue(row, 4, style, dateTools.newReformatDate(content.getAttentionDate(), "dd/MM/yyyy", "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 5, style, content.getEmergencyCareEntrance());
		excelUtilsService.setCellValue(row, 6, style, content.getAmbulance());
		excelUtilsService.setCellValue(row, 7, style, content.getOffice());
		excelUtilsService.setCellValue(row, 8, style, content.getSector());
		excelUtilsService.setCellValue(row, 9, style, content.getSituation());
		excelUtilsService.setCellValue(row, 10, style, content.getEmergencyCareType());
		excelUtilsService.setCellValue(row, 11, style, content.getTriageNote());
		excelUtilsService.setCellValue(row, 12, style, content.getTriageLevel());
		excelUtilsService.setCellValue(row, 13, style, content.getDateDischarge());
		excelUtilsService.setCellValue(row, 14, style, content.getAmbulanceDischarge());
		excelUtilsService.setCellValue(row, 15, style, content.getDateDischarge());
		excelUtilsService.setCellValue(row, 16, style, content.getPatientExit());
		excelUtilsService.setCellValue(row, 17, style, content.getPoliceIntervention());
	}
}
