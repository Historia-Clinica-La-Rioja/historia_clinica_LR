package net.pladema.provincialreports.programreports.service;

import ar.lamansys.sgx.shared.reports.util.manager.WorkbookCreator;
import ar.lamansys.sgx.shared.reports.util.struct.ICell;
import ar.lamansys.sgx.shared.reports.util.struct.ICellStyle;
import ar.lamansys.sgx.shared.reports.util.struct.IRow;
import ar.lamansys.sgx.shared.reports.util.struct.ISheet;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;

import net.pladema.provincialreports.programreports.repository.EpidemiologyOneConsultationDetail;

import net.pladema.provincialreports.programreports.repository.EpidemiologyTwoConsultationDetail;
import net.pladema.provincialreports.programreports.repository.RecuperoGeneralConsultationDetail;
import net.pladema.provincialreports.reportformat.DateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.provincialreports.reportformat.domain.service.ReportExcelUtilsService;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class NewProgramReportsExcelService {


	@Autowired
	private final DateFormat dateTools;

	@Autowired
	public ReportExcelUtilsService excelUtilsService;

	public NewProgramReportsExcelService(InstitutionRepository institutionRepository, DateFormat dateTools) {
		this.dateTools = dateTools;
	}

	public IWorkbook buildEpidemiologyOneExcel(String title, String[] headers, List<EpidemiologyOneConsultationDetail> result, Integer institutionId, LocalDate startDate, LocalDate endDate) {

		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();

		excelUtilsService.createHeaderCellsStyle(workbook);

		ISheet sheet = workbook.createSheet(title);

		excelUtilsService.fillRow(sheet, excelUtilsService.getHeaderDataWithoutObservation(headers, title, 7, excelUtilsService.periodStringFromLocalDates(startDate, endDate), institutionId));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());

		ICellStyle dataCellsStyle = excelUtilsService.getDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillEpidemiologyOneRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.setMinimalHeaderDimensions(sheet);
		excelUtilsService.setSheetDimensions(sheet);

		return workbook;
	}

	public IWorkbook buildEpidemiologyTwoExcel(String title, String[] headers, List<EpidemiologyTwoConsultationDetail> result, Integer institutionId, LocalDate startDate, LocalDate endDate) {

		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();

		excelUtilsService.createHeaderCellsStyle(workbook);

		ISheet sheet = workbook.createSheet(title);

		excelUtilsService.fillRow(sheet, excelUtilsService.getHeaderDataWithoutObservation(headers, title, 7, excelUtilsService.periodStringFromLocalDates(startDate, endDate), institutionId));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());

		ICellStyle dataCellsStyle = excelUtilsService.getDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillEpidemiologyTwoRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.setMinimalHeaderDimensions(sheet);
		excelUtilsService.setSheetDimensions(sheet);

		// manual adjustments
		sheet.setColumnWidth(1, 90);
		sheet.setColumnWidth(2, 90);

		return workbook;
	}

	public IWorkbook buildRecuperoGeneralExcel(String title, String[] headers, List<RecuperoGeneralConsultationDetail> result, Integer institutionId, LocalDate startDate, LocalDate endDate) {

		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();

		excelUtilsService.createHeaderCellsStyle(workbook);

		ISheet sheet = workbook.createSheet(title);

		excelUtilsService.fillRow(sheet, excelUtilsService.getHeaderDataWithoutObservation(headers, title, 17, excelUtilsService.periodStringFromLocalDates(startDate, endDate), institutionId));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());

		ICellStyle dataCellsStyle = excelUtilsService.getDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillRecuperoGeneralRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.setMinimalHeaderDimensions(sheet);
		excelUtilsService.setSheetDimensions(sheet);

		return workbook;
	}

	public void fillEpidemiologyOneRow(IRow row, EpidemiologyOneConsultationDetail content, ICellStyle style) {
		AtomicInteger cellNumber = new AtomicInteger(0);

		ICell cell0 = row.createCell(cellNumber.getAndIncrement());
		cell0.setCellStyle(style);
		cell0.setCellValue(content.getPatientFullName());

		ICell cell1 = row.createCell(cellNumber.getAndIncrement());
		cell1.setCellStyle(style);
		cell1.setCellValue(content.getCoding());

		ICell cell2 = row.createCell(cellNumber.getAndIncrement());
		cell2.setCellStyle(style);
		cell2.setCellValue(dateTools.dateFromYMDToDMY(content.getBirthDate()));

		ICell cell3 = row.createCell(cellNumber.getAndIncrement());
		cell3.setCellStyle(style);
		cell3.setCellValue(content.getGender());

		ICell cell4 = row.createCell(cellNumber.getAndIncrement());
		cell4.setCellStyle(style);
		cell4.setCellValue(dateTools.dateFromYMDHMSNOToDMY(content.getStartDate()));

		ICell cell5 = row.createCell(cellNumber.getAndIncrement());
		cell5.setCellStyle(style);
		cell5.setCellValue(content.getDepartment());

		ICell cell6 = row.createCell(cellNumber.getAndIncrement());
		cell6.setCellStyle(style);
		cell6.setCellValue(content.getAddress());

		ICell cell7 = row.createCell(cellNumber.getAndIncrement());
		cell7.setCellStyle(style);
		cell7.setCellValue(content.getCie10Codes());

		ICell cell8 = row.createCell(cellNumber.getAndIncrement());
		cell8.setCellStyle(style);
		cell8.setCellValue(content.getIdentificationNumber());

		ICell cell9 = row.createCell(cellNumber.getAndIncrement());
		cell9.setCellStyle(style);
		cell9.setCellValue(content.getProblems());

	}

	public void fillEpidemiologyTwoRow(IRow row, EpidemiologyTwoConsultationDetail content, ICellStyle style) {
		AtomicInteger cellNumber = new AtomicInteger(0);

		ICell cell0 = row.createCell(cellNumber.getAndIncrement());
		cell0.setCellStyle(style);
		cell0.setCellValue(content.getDiagnostic());

		ICell cell1 = row.createCell(cellNumber.getAndIncrement());
		cell1.setCellStyle(style);
		cell1.setCellValue(content.getGrp());

		ICell cell2 = row.createCell(cellNumber.getAndIncrement());
		cell2.setCellStyle(style);
		cell2.setCellValue(content.getCounter());

	}

	public void fillRecuperoGeneralRow(IRow row, RecuperoGeneralConsultationDetail content, ICellStyle style) {
		AtomicInteger cellNumber = new AtomicInteger(0);

		ICell cell0 = row.createCell(cellNumber.getAndIncrement());
		cell0.setCellStyle(style);
		cell0.setCellValue(content.getOperativeUnit());

		ICell cell1 = row.createCell(cellNumber.getAndIncrement());
		cell1.setCellStyle(style);
		cell1.setCellValue(content.getLender());

		ICell cell2 = row.createCell(cellNumber.getAndIncrement());
		cell2.setCellStyle(style);
		cell2.setCellValue(content.getLenderDni());

		ICell cell3 = row.createCell(cellNumber.getAndIncrement());
		cell3.setCellStyle(style);
		cell3.setCellValue(dateTools.reformatDateThree(content.getAttentionDate()));

		ICell cell4 = row.createCell(cellNumber.getAndIncrement());
		cell4.setCellStyle(style);
		cell4.setCellValue(content.getHour());

		ICell cell5 = row.createCell(cellNumber.getAndIncrement());
		cell5.setCellStyle(style);
		cell5.setCellValue(content.getConsultationNumber());

		ICell cell6 = row.createCell(cellNumber.getAndIncrement());
		cell6.setCellStyle(style);
		cell6.setCellValue(content.getPatientDni());

		ICell cell7 = row.createCell(cellNumber.getAndIncrement());
		cell7.setCellStyle(style);
		cell7.setCellValue(content.getPatientName());

		ICell cell8 = row.createCell(cellNumber.getAndIncrement());
		cell8.setCellStyle(style);
		cell8.setCellValue(content.getGender());

		ICell cell9 = row.createCell(cellNumber.getAndIncrement());
		cell9.setCellStyle(style);
		cell9.setCellValue(dateTools.reformatDateFive(content.getBirthDate()));

		ICell cell10 = row.createCell(cellNumber.getAndIncrement());
		cell10.setCellStyle(style);
		cell10.setCellValue(content.getAgeTurn());

		ICell cell11 = row.createCell(cellNumber.getAndIncrement());
		cell11.setCellStyle(style);
		cell11.setCellValue(content.getAgeToday());

		ICell cell12 = row.createCell(cellNumber.getAndIncrement());
		cell12.setCellStyle(style);
		cell12.setCellValue(content.getMedicalCoverage());

		ICell cell13 = row.createCell(cellNumber.getAndIncrement());
		cell13.setCellStyle(style);
		cell13.setCellValue(content.getAddress());

		ICell cell14 = row.createCell(cellNumber.getAndIncrement());
		cell14.setCellStyle(style);
		cell14.setCellValue(content.getLocation());

		ICell cell15 = row.createCell(cellNumber.getAndIncrement());
		cell15.setCellStyle(style);
		cell15.setCellValue(content.getReasons());

		ICell cell16 = row.createCell(cellNumber.getAndIncrement());
		cell16.setCellStyle(style);
		cell16.setCellValue(content.getProcedures());

		ICell cell17 = row.createCell(cellNumber.getAndIncrement());
		cell17.setCellStyle(style);
		cell17.setCellValue(content.getProblems());

		ICell cell18 = row.createCell(cellNumber.getAndIncrement());
		cell18.setCellStyle(style);
		cell18.setCellValue(content.getMedication());

		ICell cell19 = row.createCell(cellNumber.getAndIncrement());
		cell19.setCellStyle(style);
		cell19.setCellValue(content.getEvolution());

	}
}
