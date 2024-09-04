package net.pladema.provincialreports.olderadultsreports.service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.reports.util.manager.WorkbookCreator;
import ar.lamansys.sgx.shared.reports.util.struct.ICellStyle;
import ar.lamansys.sgx.shared.reports.util.struct.IRow;
import ar.lamansys.sgx.shared.reports.util.struct.ISheet;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.provincialreports.olderadultsreports.repository.OlderAdultsHospitalizationConsultationDetail;
import net.pladema.provincialreports.olderadultsreports.repository.OlderAdultsOutpatientConsultationDetail;
import net.pladema.provincialreports.olderadultsreports.repository.PolypharmacyConsultationDetail;
import net.pladema.provincialreports.reportformat.DateFormat;
import net.pladema.provincialreports.reportformat.domain.service.ReportExcelUtilsService;

@Service
public class OlderAdultReportsExcelService {
	private final DateFormat dateTools;

	private final ReportExcelUtilsService excelUtilsService;

	public OlderAdultReportsExcelService(DateFormat dateTools, ReportExcelUtilsService excelUtilsService) {
		this.dateTools = dateTools;
		this.excelUtilsService = excelUtilsService;
	}

	public IWorkbook buildOlderAdultOutpatientExcel(String title, String[] headers, List<OlderAdultsOutpatientConsultationDetail> result, Integer institutionId, LocalDate startDate, LocalDate endDate) {
		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();
		excelUtilsService.newCreateHeaderCellsStyle(workbook);
		ISheet sheet = workbook.createSheet(title);
		excelUtilsService.newFillRow(sheet, excelUtilsService.newGetHeaderDataWithoutObservation(headers, title, 12, 0, excelUtilsService.newPeriodStringFromLocalDates(startDate, endDate), institutionId));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle dataCellsStyle = excelUtilsService.newCreateDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillOlderAdultsOutpatientRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.newSetMinimalHeaderDimensions(sheet);
		excelUtilsService.newSetSheetDimensions(sheet);

		return workbook;
	}

	public IWorkbook buildOlderAdultHospitalizationExcel(String title, String[] headers, List<OlderAdultsHospitalizationConsultationDetail> result, Integer institutionId, LocalDate startDate, LocalDate endDate) {
		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();
		excelUtilsService.newCreateHeaderCellsStyle(workbook);
		ISheet sheet = workbook.createSheet(title);
		excelUtilsService.newFillRow(sheet, excelUtilsService.newGetHeaderDataWithoutObservation(headers, title, 11, 0, excelUtilsService.newPeriodStringFromLocalDates(startDate, endDate), institutionId));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle dataCellsStyle = excelUtilsService.newCreateDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillOlderAdultsHospitalizationRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.newSetMinimalHeaderDimensions(sheet);
		excelUtilsService.newSetSheetDimensions(sheet);

		return workbook;
	}

	public IWorkbook buildPolypharmacyExcel(String title, String[] headers, List<PolypharmacyConsultationDetail> result, Integer institutionId, LocalDate startDate, LocalDate endDate) {
		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();
		excelUtilsService.newCreateHeaderCellsStyle(workbook);
		ISheet sheet = workbook.createSheet(title);
		excelUtilsService.newFillRow(sheet, excelUtilsService.newGetHeaderDataWithoutObservation(headers, title, 11, 0, excelUtilsService.newPeriodStringFromLocalDates(startDate, endDate), institutionId));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle dataCellsStyle = excelUtilsService.newCreateDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillPolypharmacyRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.newSetMinimalHeaderDimensions(sheet);
		excelUtilsService.newSetSheetDimensions(sheet);

		return workbook;
	}

	public void fillOlderAdultsOutpatientRow(IRow row, OlderAdultsOutpatientConsultationDetail content, ICellStyle style) {
		excelUtilsService.setCellValue(row, 0, style, content.getLender());
		excelUtilsService.setCellValue(row, 1, style, content.getLenderDni());
		excelUtilsService.setCellValue(row, 2, style, dateTools.newReformatDate(content.getAttentionDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 3, style, dateTools.standardizeTime(content.getAttentionHour()));
		excelUtilsService.setCellValue(row, 4, style, content.getConsultationNumber());
		excelUtilsService.setCellValue(row, 5, style, content.getPatientName());
		excelUtilsService.setCellValue(row, 6, style, content.getPatientDni());
		excelUtilsService.setCellValue(row, 7, style, content.getGender());
		excelUtilsService.setCellValue(row, 8, style, dateTools.newReformatDate(content.getBirthDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 9, style, content.getAgeTurn());
		excelUtilsService.setCellValue(row, 10, style, content.getMedicalCoverage());
		excelUtilsService.setCellValue(row, 11, style, content.getDirection());
		excelUtilsService.setCellValue(row, 12, style, content.getPatientLocation());
		excelUtilsService.setCellValue(row, 13, style, content.getPhoneNumber());
		excelUtilsService.setCellValue(row, 14, style, content.getProblems());
	}

	public void fillOlderAdultsHospitalizationRow(IRow row, OlderAdultsHospitalizationConsultationDetail content, ICellStyle style) {
		excelUtilsService.setCellValue(row, 0, style, content.getPatientLastNames() + " " + content.getPatientNames());
		excelUtilsService.setCellValue(row, 1, style, content.getIdentification());
		excelUtilsService.setCellValue(row, 2, style, content.getGender());
		excelUtilsService.setCellValue(row, 3, style, dateTools.newReformatDate(content.getBirthDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 4, style, content.getAgeTurn());
		excelUtilsService.setCellValue(row, 5, style, content.getPhone());
		excelUtilsService.setCellValue(row, 6, style, content.getEntryDate());
		excelUtilsService.setCellValue(row, 7, style, content.getProbableDischargeDate());
		excelUtilsService.setCellValue(row, 8, style, content.getBed());
		excelUtilsService.setCellValue(row, 9, style, content.getCategory());
		excelUtilsService.setCellValue(row, 10, style, content.getRoom());
		excelUtilsService.setCellValue(row, 11, style, content.getSector());
		excelUtilsService.setCellValue(row, 12, style, content.getDischargeDate());
		excelUtilsService.setCellValue(row, 13, style, content.getProblems());
	}

	public void fillPolypharmacyRow(IRow row, PolypharmacyConsultationDetail content, ICellStyle style) {
		excelUtilsService.setCellValue(row, 0, style, content.getPatientName());
		excelUtilsService.setCellValue(row, 1, style, content.getPatientIdType() + " " + content.getPatientIdNumber());
		excelUtilsService.setCellValue(row, 2, style, content.getPatientSex());
		excelUtilsService.setCellValue(row, 3, style, content.getPatientAgeWhenAttended());
		excelUtilsService.setCellValue(row, 4, style, content.getProfessionalName());
		excelUtilsService.setCellValue(row, 5, style, content.getProfessionalIdNumber());
		excelUtilsService.setCellValue(row, 6, style, content.getProfessionalSpeciality());
		excelUtilsService.setCellValue(row, 7, style, dateTools.newReformatDate(content.getStartDate().substring(0, 10), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 8, style, content.getRelatedProblem());
		excelUtilsService.setCellValue(row, 9, style, content.getIsChronic());
		excelUtilsService.setCellValue(row, 10, style, content.getOrigin());
		excelUtilsService.setCellValue(row, 11, style, content.getSnomed());
		excelUtilsService.setCellValue(row, 12, style, content.getMedication());
		excelUtilsService.setCellValue(row, 13, style, content.getMedicalInsurance());
	}
}
