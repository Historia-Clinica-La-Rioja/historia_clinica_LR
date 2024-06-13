package net.pladema.provincialreports.programreports.service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.reports.util.manager.WorkbookCreator;
import ar.lamansys.sgx.shared.reports.util.struct.ICell;
import ar.lamansys.sgx.shared.reports.util.struct.ICellStyle;
import ar.lamansys.sgx.shared.reports.util.struct.IRow;
import ar.lamansys.sgx.shared.reports.util.struct.ISheet;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.provincialreports.programreports.repository.EpidemiologyOneConsultationDetail;
import net.pladema.provincialreports.programreports.repository.EpidemiologyTwoConsultationDetail;
import net.pladema.provincialreports.programreports.repository.OdontologicalConsultationDetail;
import net.pladema.provincialreports.programreports.repository.RecuperoGeneralConsultationDetail;
import net.pladema.provincialreports.programreports.repository.SumarGeneralConsultationDetail;
import net.pladema.provincialreports.reportformat.DateFormat;
import net.pladema.provincialreports.reportformat.domain.service.ReportExcelUtilsService;

@Service
public class NewProgramReportsExcelService {


	@Autowired
	private final DateFormat dateTools;

	@Autowired
	public ReportExcelUtilsService excelUtilsService;

	public NewProgramReportsExcelService(DateFormat dateTools) {
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

	public IWorkbook buildSumarGeneralExcel(String title, String[] headers, List<SumarGeneralConsultationDetail> result, Integer institutionId, LocalDate startDate, LocalDate endDate) {

		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();

		excelUtilsService.createHeaderCellsStyle(workbook);

		for (SumarGeneralConsultationDetail imc : result) {
			Double peso = imc.getWeight() != null ? Double.valueOf(imc.getWeight()) : null;
			Double altura = imc.getHeight() != null ? Double.valueOf(imc.getHeight()) : null;
			Double imcResult;
			if (peso != null && altura != null) {
				imcResult = peso / (altura * altura);
				DecimalFormat df = new DecimalFormat("##.##");
				String imcFormat = df.format(imcResult * 10000);
				SumarGeneralConsultationDetail consultationDetailRecuperoSumar = new SumarGeneralConsultationDetail(imcFormat);
				imc.setBmi(consultationDetailRecuperoSumar.getBmi());
			}
		}

		ISheet sheet = workbook.createSheet(title);

		excelUtilsService.fillRow(sheet, excelUtilsService.getHeaderDataWithoutObservation(headers, title, 28, excelUtilsService.periodStringFromLocalDates(startDate, endDate), institutionId));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());

		ICellStyle dataCellsStyle = excelUtilsService.getDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillSumarGeneralRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.setMinimalHeaderDimensions(sheet);
		excelUtilsService.setSheetDimensions(sheet);


		return workbook;
	}

	public IWorkbook buildSumarOdontologicoExcel(String title, String[] headers, List<OdontologicalConsultationDetail> result, Integer institutionId, LocalDate startDate, LocalDate endDate) {

		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();

		excelUtilsService.createHeaderCellsStyle(workbook);

		ISheet sheet = workbook.createSheet(title);

		excelUtilsService.fillRow(sheet, excelUtilsService.getHeaderDataWithoutObservation(headers, title, 18, excelUtilsService.periodStringFromLocalDates(startDate, endDate), institutionId));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());

		ICellStyle dataCellsStyle = excelUtilsService.getDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillOdontologicalConsultationRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.setMinimalHeaderDimensions(sheet);
		excelUtilsService.setSheetDimensions(sheet);

		return workbook;
	}

	public IWorkbook buildRecuperoOdontologicoExcel(String title, String[] headers, List<OdontologicalConsultationDetail> result, Integer institutionId, LocalDate startDate, LocalDate endDate) {

		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();

		excelUtilsService.createHeaderCellsStyle(workbook);

		ISheet sheet = workbook.createSheet(title);

		excelUtilsService.fillRow(sheet, excelUtilsService.getHeaderDataWithoutObservation(headers, title, 18, excelUtilsService.periodStringFromLocalDates(startDate, endDate), institutionId));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());

		ICellStyle dataCellsStyle = excelUtilsService.getDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillOdontologicalConsultationRow(row, resultData, dataCellsStyle);
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

	private void fillSumarGeneralRow(IRow row, SumarGeneralConsultationDetail content, ICellStyle style) {
		AtomicInteger rowNumber = new AtomicInteger(0);

		ICell cell0 = row.createCell(rowNumber.getAndIncrement());
		cell0.setCellStyle(style);
		cell0.setCellValue(content.getOperativeUnit());

		ICell cell1 = row.createCell(rowNumber.getAndIncrement());
		cell1.setCellStyle(style);
		cell1.setCellValue(content.getLender());

		ICell cell2 = row.createCell(rowNumber.getAndIncrement());
		cell2.setCellStyle(style);
		cell2.setCellValue(content.getLenderDni());

		ICell cell3 = row.createCell(rowNumber.getAndIncrement());
		cell3.setCellStyle(style);
		cell3.setCellValue(content.getAttentionDate());

		ICell cell4 = row.createCell(rowNumber.getAndIncrement());
		cell4.setCellStyle(style);
		cell4.setCellValue(content.getHour());

		ICell cell6 = row.createCell(rowNumber.getAndIncrement());
		cell6.setCellStyle(style);
		cell6.setCellValue(content.getPatientDni());

		ICell cell7 = row.createCell(rowNumber.getAndIncrement());
		cell7.setCellStyle(style);
		cell7.setCellValue(content.getPatientName());

		ICell cell8 = row.createCell(rowNumber.getAndIncrement());
		cell8.setCellStyle(style);
		cell8.setCellValue(content.getGender());

		ICell cell11 = row.createCell(rowNumber.getAndIncrement());
		cell11.setCellStyle(style);
		cell11.setCellValue(content.getBirthDate());

		ICell cell12 = row.createCell(rowNumber.getAndIncrement());
		cell12.setCellStyle(style);
		cell12.setCellValue(content.getAgeTurn());

		ICell cell14 = row.createCell(rowNumber.getAndIncrement());
		cell14.setCellStyle(style);
		cell14.setCellValue(content.getEthnicity());

		ICell cell15 = row.createCell(rowNumber.getAndIncrement());
		cell15.setCellStyle(style);
		cell15.setCellValue(content.getMedicalCoverage());

		ICell cell16 = row.createCell(rowNumber.getAndIncrement());
		cell16.setCellStyle(style);
		cell16.setCellValue(content.getDirection());

		ICell cell17 = row.createCell(rowNumber.getAndIncrement());
		cell17.setCellStyle(style);
		cell17.setCellValue(content.getNeighborhood());

		ICell cell18 = row.createCell(rowNumber.getAndIncrement());
		cell18.setCellStyle(style);
		cell18.setCellValue(content.getLocation());

		ICell cell21 = row.createCell(rowNumber.getAndIncrement());
		cell21.setCellStyle(style);
		cell21.setCellValue(content.getSystolicBloodPressure());

		ICell cell22 = row.createCell(rowNumber.getAndIncrement());
		cell22.setCellStyle(style);
		cell22.setCellValue(content.getDiastolicBloodPressure());

		ICell cell23 = row.createCell(rowNumber.getAndIncrement());
		cell23.setCellStyle(style);
		cell23.setCellValue(content.getMeanArterialPressure());

		ICell cell24 = row.createCell(rowNumber.getAndIncrement());
		cell24.setCellStyle(style);
		cell24.setCellValue(content.getTemperature());

		ICell cell25 = row.createCell(rowNumber.getAndIncrement());
		cell25.setCellStyle(style);
		cell25.setCellValue(content.getHeartRate());

		ICell cell26 = row.createCell(rowNumber.getAndIncrement());
		cell26.setCellStyle(style);
		cell26.setCellValue(content.getRespirationRate());

		ICell cell27 = row.createCell(rowNumber.getAndIncrement());
		cell27.setCellStyle(style);
		cell27.setCellValue(content.getOxygenSaturationHemoglobin());

		ICell cell28 = row.createCell(rowNumber.getAndIncrement());
		cell28.setCellStyle(style);
		cell28.setCellValue(content.getHeight());

		ICell cell29 = row.createCell(rowNumber.getAndIncrement());
		cell29.setCellStyle(style);
		cell29.setCellValue(content.getWeight());

		ICell cell30 = row.createCell(rowNumber.getAndIncrement());
		cell30.setCellStyle(style);
		cell30.setCellValue(content.getBmi());

		ICell cell31 = row.createCell(rowNumber.getAndIncrement());
		cell31.setCellStyle(style);
		cell31.setCellValue(content.getHeadCircumference());

		ICell cell32 = row.createCell(rowNumber.getAndIncrement());
		cell32.setCellStyle(style);
		cell32.setCellValue(content.getReasons());

		ICell cell33 = row.createCell(rowNumber.getAndIncrement());
		cell33.setCellStyle(style);
		cell33.setCellValue(content.getProcedures());

		ICell cell34 = row.createCell(rowNumber.getAndIncrement());
		cell34.setCellStyle(style);
		cell34.setCellValue(content.getProblems());

		ICell cell35 = row.createCell(rowNumber.getAndIncrement());
		cell35.setCellStyle(style);
		cell35.setCellValue(content.getMedication());

		ICell cell36 = row.createCell(rowNumber.getAndIncrement());
		cell36.setCellStyle(style);
		cell36.setCellValue(content.getEvolution());

	}

	private void fillOdontologicalConsultationRow(IRow row, OdontologicalConsultationDetail content, ICellStyle style) {
		AtomicInteger rowNumber = new AtomicInteger(0);

		ICell cell0 = row.createCell(rowNumber.getAndIncrement());
		cell0.setCellStyle(style);
		cell0.setCellValue(content.getOperativeUnit());

		ICell cell1 = row.createCell(rowNumber.getAndIncrement());
		cell1.setCellStyle(style);
		cell1.setCellValue(content.getLender());

		ICell cell2 = row.createCell(rowNumber.getAndIncrement());
		cell2.setCellStyle(style);
		cell2.setCellValue(content.getLenderDni());

		ICell cell3 = row.createCell(rowNumber.getAndIncrement());
		cell3.setCellStyle(style);
		cell3.setCellValue(dateTools.reformatDateThree(content.getAttentionDate()));

		ICell cell4 = row.createCell(rowNumber.getAndIncrement());
		cell4.setCellStyle(style);
		cell4.setCellValue(content.getAttentionHour());

		ICell cell5 = row.createCell(rowNumber.getAndIncrement());
		cell5.setCellStyle(style);
		cell5.setCellValue(content.getPatientDni());

		ICell cell6 = row.createCell(rowNumber.getAndIncrement());
		cell6.setCellStyle(style);
		cell6.setCellValue(content.getPatientName());

		ICell cell7 = row.createCell(rowNumber.getAndIncrement());
		cell7.setCellStyle(style);
		cell7.setCellValue(content.getGender());

		ICell cell8 = row.createCell(rowNumber.getAndIncrement());
		cell8.setCellStyle(style);
		cell8.setCellValue(dateTools.reformatDateFive(content.getBirthDate()));

		ICell cell9 = row.createCell(rowNumber.getAndIncrement());
		cell9.setCellStyle(style);
		cell9.setCellValue(content.getAgeTurn());

		ICell cell10 = row.createCell(rowNumber.getAndIncrement());
		cell10.setCellStyle(style);
		cell10.setCellValue(content.getMedicalCoverage());

		ICell cell11 = row.createCell(rowNumber.getAndIncrement());
		cell11.setCellStyle(style);
		cell11.setCellValue(content.getDirection());

		ICell cell12 = row.createCell(rowNumber.getAndIncrement());
		cell12.setCellStyle(style);
		cell12.setCellValue(content.getNeighborhood());

		ICell cell13 = row.createCell(rowNumber.getAndIncrement());
		cell13.setCellStyle(style);
		cell13.setCellValue(content.getLocation());

		ICell cell14 = row.createCell(rowNumber.getAndIncrement());
		cell14.setCellStyle(style);
		cell14.setCellValue(content.getIndexCpo());

		ICell cell15 = row.createCell(rowNumber.getAndIncrement());
		cell15.setCellStyle(style);
		cell15.setCellValue(content.getIndexCeo());

		ICell cell16 = row.createCell(rowNumber.getAndIncrement());
		cell16.setCellStyle(style);
		cell16.setCellValue(content.getReasons());

		ICell cell17 = row.createCell(rowNumber.getAndIncrement());
		cell17.setCellStyle(style);
		cell17.setCellValue(content.getProcedures());

		ICell cell18 = row.createCell(rowNumber.getAndIncrement());
		cell18.setCellStyle(style);
		cell18.setCellValue(content.getDentalProcedures());

		ICell cell19 = row.createCell(rowNumber.getAndIncrement());
		cell19.setCellStyle(style);
		cell19.setCellValue(content.getProblems());

		ICell cell20 = row.createCell(rowNumber.getAndIncrement());
		cell20.setCellStyle(style);
		cell20.setCellValue(content.getDentistryDiagnostics());

	}
}
