package net.pladema.provincialreports.programreports.service;

import ar.lamansys.sgx.shared.reports.util.manager.WorkbookCreator;
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

import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ProgramReportsExcelService {
	private final DateFormat dateTools;
	private final ReportExcelUtilsService excelUtilsService;

	public ProgramReportsExcelService(DateFormat dateTools, ReportExcelUtilsService excelUtilsService) {
		this.dateTools = dateTools;
		this.excelUtilsService = excelUtilsService;
	}

	public IWorkbook buildEpidemiologyOneExcel(String title, String[] headers, List<EpidemiologyOneConsultationDetail> result, Integer institutionId, LocalDate startDate, LocalDate endDate) {
		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();
		excelUtilsService.createHeaderCellsStyle(workbook);
		ISheet sheet = workbook.createSheet(title);
		excelUtilsService.fillRow(sheet, excelUtilsService.getHeaderDataWithoutObservation(headers, title, 7, 0, excelUtilsService.periodStringFromLocalDates(startDate, endDate), institutionId, null));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle dataCellsStyle = excelUtilsService.createDataCellsStyle(workbook);

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
		excelUtilsService.fillRow(sheet, excelUtilsService.getHeaderDataWithoutObservation(headers, title, 7, 0, excelUtilsService.periodStringFromLocalDates(startDate, endDate), institutionId, null));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle dataCellsStyle = excelUtilsService.createDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillEpidemiologyTwoRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.setMinimalHeaderDimensions(sheet);
		excelUtilsService.setSheetDimensions(sheet);

		return workbook;
	}

	public IWorkbook buildRecuperoGeneralExcel(String title, String[] headers, List<RecuperoGeneralConsultationDetail> result, Integer institutionId, LocalDate startDate, LocalDate endDate) {
		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();
		excelUtilsService.createHeaderCellsStyle(workbook);
		ISheet sheet = workbook.createSheet(title);
		excelUtilsService.fillRow(sheet, excelUtilsService.getHeaderDataWithoutObservation(headers, title, 17, 0, excelUtilsService.periodStringFromLocalDates(startDate, endDate), institutionId, null));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle dataCellsStyle = excelUtilsService.createDataCellsStyle(workbook);

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

		result.forEach(this::calculateAndSetBmiForSumarGeneral);

		ISheet sheet = workbook.createSheet(title);
		excelUtilsService.fillRow(sheet, excelUtilsService.getHeaderDataWithoutObservation(headers, title, 28, 0, excelUtilsService.periodStringFromLocalDates(startDate, endDate), institutionId, null));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle dataCellsStyle = excelUtilsService.createDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillSumarGeneralRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.setMinimalHeaderDimensions(sheet);
		excelUtilsService.setSheetDimensions(sheet);

		return workbook;
	}

	public IWorkbook buildRecuperoOdontologicoExcel(String title, String[] headers, List<OdontologicalConsultationDetail> result, Integer institutionId, LocalDate startDate, LocalDate endDate) {
		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();
		excelUtilsService.createHeaderCellsStyle(workbook);
		ISheet sheet = workbook.createSheet(title);
		excelUtilsService.fillRow(sheet, excelUtilsService.getHeaderDataWithoutObservation(headers, title, 18, 0, excelUtilsService.periodStringFromLocalDates(startDate, endDate), institutionId, null));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle dataCellsStyle = excelUtilsService.createDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillOdontologicalConsultationRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.setMinimalHeaderDimensions(sheet);
		excelUtilsService.setSheetDimensions(sheet);

		return workbook;
	}

	public IWorkbook buildSumarOdontologicoExcel(String title, String[] headers, List<OdontologicalConsultationDetail> result, Integer institutionId, LocalDate startDate, LocalDate endDate) {
		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();
		excelUtilsService.createHeaderCellsStyle(workbook);
		ISheet sheet = workbook.createSheet(title);
		excelUtilsService.fillRow(sheet, excelUtilsService.getHeaderDataWithoutObservation(headers, title, 18, 0, excelUtilsService.periodStringFromLocalDates(startDate, endDate), institutionId, null));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle dataCellsStyle = excelUtilsService.createDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillOdontologicalConsultationRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.setMinimalHeaderDimensions(sheet);
		excelUtilsService.setSheetDimensions(sheet);

		return workbook;
	}

	private void calculateAndSetBmiForSumarGeneral(SumarGeneralConsultationDetail consultationDetail) {
		Double weight = consultationDetail.getWeight() != null ? Double.valueOf(consultationDetail.getWeight()) : null;
		Double height = consultationDetail.getHeight() != null ? Double.valueOf(consultationDetail.getHeight()) : null;
		if (weight != null && height != null) {
			double bmiResult = weight / (height * height);
			DecimalFormat df = new DecimalFormat("##.##");
			String formattedBmi = df.format(bmiResult * 10000);
			SumarGeneralConsultationDetail bmiDetail = new SumarGeneralConsultationDetail(formattedBmi);
			consultationDetail.setBmi(bmiDetail.getBmi());
		}
	}

	public void fillEpidemiologyOneRow(IRow row, EpidemiologyOneConsultationDetail content, ICellStyle style) {
		excelUtilsService.setCellValue(row, 0, style, content.getPatientFullName());
		excelUtilsService.setCellValue(row, 1, style, content.getCoding());
		excelUtilsService.setCellValue(row, 2, style, dateTools.newReformatDate(content.getBirthDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 3, style, content.getGender());
		excelUtilsService.setCellValue(row, 4, style, content.getStartDate());
		excelUtilsService.setCellValue(row, 5, style, content.getDepartment());
		excelUtilsService.setCellValue(row, 6, style, content.getAddress());
		excelUtilsService.setCellValue(row, 7, style, content.getCie10Codes());
		excelUtilsService.setCellValue(row, 8, style, content.getIdentificationNumber());
		excelUtilsService.setCellValue(row, 9, style, content.getProblems());
	}

	public void fillEpidemiologyTwoRow(IRow row, EpidemiologyTwoConsultationDetail content, ICellStyle style) {
		excelUtilsService.setCellValue(row, 0, style, content.getDiagnostic());
		excelUtilsService.setCellValue(row, 1, style, content.getGrp());
		excelUtilsService.setCellValue(row, 2, style, content.getCounter());
	}

	public void fillRecuperoGeneralRow(IRow row, RecuperoGeneralConsultationDetail content, ICellStyle style) {
		excelUtilsService.setCellValue(row, 0, style, content.getOperativeUnit());
		excelUtilsService.setCellValue(row, 1, style, content.getLender());
		excelUtilsService.setCellValue(row, 2, style, content.getLenderDni());
		excelUtilsService.setCellValue(row, 3, style, dateTools.newReformatDate(content.getAttentionDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 4, style, dateTools.standardizeTime(content.getHour()));
		excelUtilsService.setCellValue(row, 5, style, content.getConsultationNumber());
		excelUtilsService.setCellValue(row, 6, style, content.getPatientDni());
		excelUtilsService.setCellValue(row, 7, style, content.getPatientName());
		excelUtilsService.setCellValue(row, 8, style, content.getGender());
		excelUtilsService.setCellValue(row, 9, style, dateTools.newReformatDate(content.getBirthDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 10, style, content.getAgeTurn());
		excelUtilsService.setCellValue(row, 11, style, content.getAgeToday());
		excelUtilsService.setCellValue(row, 12, style, content.getMedicalCoverage());
		excelUtilsService.setCellValue(row, 13, style, content.getAddress());
		excelUtilsService.setCellValue(row, 14, style, content.getLocation());
		excelUtilsService.setCellValue(row, 15, style, content.getReasons());
		excelUtilsService.setCellValue(row, 16, style, content.getProcedures());
		excelUtilsService.setCellValue(row, 17, style, content.getProblems());
		excelUtilsService.setCellValue(row, 18, style, content.getMedication());
		excelUtilsService.setCellValue(row, 19, style, content.getEvolution());
	}

	public void fillSumarGeneralRow(IRow row, SumarGeneralConsultationDetail content, ICellStyle style) {
		excelUtilsService.setCellValue(row, 0, style, content.getOperativeUnit());
		excelUtilsService.setCellValue(row, 1, style, content.getLender());
		excelUtilsService.setCellValue(row, 2, style, content.getLenderDni());
		excelUtilsService.setCellValue(row, 3, style, dateTools.newReformatDate(content.getAttentionDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 4, style, dateTools.standardizeTime(content.getHour()));
		excelUtilsService.setCellValue(row, 5, style, content.getPatientDni());
		excelUtilsService.setCellValue(row, 6, style, content.getPatientName());
		excelUtilsService.setCellValue(row, 7, style, content.getGender());
		excelUtilsService.setCellValue(row, 8, style, dateTools.newReformatDate(content.getBirthDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 9, style, content.getAgeTurn());
		excelUtilsService.setCellValue(row, 10, style, content.getEthnicity());
		excelUtilsService.setCellValue(row, 11, style, content.getMedicalCoverage());
		excelUtilsService.setCellValue(row, 12, style, content.getDirection());
		excelUtilsService.setCellValue(row, 13, style, content.getNeighborhood());
		excelUtilsService.setCellValue(row, 14, style, content.getLocation());
		excelUtilsService.setCellValue(row, 15, style, content.getSystolicBloodPressure());
		excelUtilsService.setCellValue(row, 16, style, content.getDiastolicBloodPressure());
		excelUtilsService.setCellValue(row, 17, style, content.getMeanArterialPressure());
		excelUtilsService.setCellValue(row, 18, style, content.getTemperature());
		excelUtilsService.setCellValue(row, 19, style, content.getHeartRate());
		excelUtilsService.setCellValue(row, 20, style, content.getRespirationRate());
		excelUtilsService.setCellValue(row, 21, style, content.getOxygenSaturationHemoglobin());
		excelUtilsService.setCellValue(row, 22, style, content.getHeight());
		excelUtilsService.setCellValue(row, 23, style, content.getWeight());
		excelUtilsService.setCellValue(row, 24, style, content.getBmi());
		excelUtilsService.setCellValue(row, 25, style, content.getHeadCircumference());
		excelUtilsService.setCellValue(row, 26, style, content.getReasons());
		excelUtilsService.setCellValue(row, 27, style, content.getProcedures());
		excelUtilsService.setCellValue(row, 28, style, content.getProblems());
		excelUtilsService.setCellValue(row, 29, style, content.getMedication());
		excelUtilsService.setCellValue(row, 30, style, content.getEvolution());
	}

	public void fillOdontologicalConsultationRow(IRow row, OdontologicalConsultationDetail content, ICellStyle style) {
		excelUtilsService.setCellValue(row, 0, style, content.getOperativeUnit());
		excelUtilsService.setCellValue(row, 1, style, content.getLender());
		excelUtilsService.setCellValue(row, 2, style, content.getLenderDni());
		excelUtilsService.setCellValue(row, 3, style, dateTools.newReformatDate(content.getAttentionDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 4, style, dateTools.standardizeTime(content.getAttentionHour()));
		excelUtilsService.setCellValue(row, 5, style, content.getPatientDni());
		excelUtilsService.setCellValue(row, 6, style, content.getPatientName());
		excelUtilsService.setCellValue(row, 7, style, content.getGender());
		excelUtilsService.setCellValue(row, 8, style, dateTools.newReformatDate(content.getBirthDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 9, style, content.getAgeTurn());
		excelUtilsService.setCellValue(row, 10, style, content.getMedicalCoverage());
		excelUtilsService.setCellValue(row, 11, style, content.getDirection());
		excelUtilsService.setCellValue(row, 12, style, content.getNeighborhood());
		excelUtilsService.setCellValue(row, 13, style, content.getLocation());
		excelUtilsService.setCellValue(row, 14, style, content.getIndexCpo());
		excelUtilsService.setCellValue(row, 15, style, content.getIndexCeo());
		excelUtilsService.setCellValue(row, 16, style, content.getReasons());
		excelUtilsService.setCellValue(row, 17, style, content.getProcedures());
		excelUtilsService.setCellValue(row, 18, style, content.getDentalProcedures());
		excelUtilsService.setCellValue(row, 19, style, content.getProblems());
		excelUtilsService.setCellValue(row, 20, style, content.getDentistryDiagnostics());
	}
}
