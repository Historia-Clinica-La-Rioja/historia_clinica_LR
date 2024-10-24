package net.pladema.provincialreports.generalreports.service;

import ar.lamansys.sgx.shared.reports.util.manager.WorkbookCreator;
import ar.lamansys.sgx.shared.reports.util.struct.ICellStyle;
import ar.lamansys.sgx.shared.reports.util.struct.IRow;
import ar.lamansys.sgx.shared.reports.util.struct.ISheet;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.provincialreports.generalreports.repository.ComplementaryStudiesConsultationDetail;
import net.pladema.provincialreports.generalreports.repository.DiabeticHypertensionConsultationDetail;
import net.pladema.provincialreports.generalreports.repository.EmergencyConsultationDetail;
import net.pladema.provincialreports.generalreports.repository.MedicinesPrescriptionConsultationDetail;
import net.pladema.provincialreports.reportformat.DateFormat;

import net.pladema.provincialreports.reportformat.domain.service.ReportExcelUtilsService;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class GeneralReportsExcelService {

	private final DateFormat dateTools;

	private final ReportExcelUtilsService excelUtilsService;

	public GeneralReportsExcelService(DateFormat dateTools, ReportExcelUtilsService excelUtilsService) {
		this.dateTools = dateTools;
		this.excelUtilsService = excelUtilsService;
	}

	public IWorkbook buildEmergencyExcel(String title, String[] headers, List<EmergencyConsultationDetail> result, Integer institutionId, LocalDate startDate, LocalDate endDate) {
		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();
		excelUtilsService.createHeaderCellsStyle(workbook);
		ISheet sheet = workbook.createSheet(title);
		excelUtilsService.fillRow(sheet, excelUtilsService.getHeaderDataWithoutObservation(headers, title, 16, 1, excelUtilsService.periodStringFromLocalDates(startDate, endDate), institutionId, null));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle dataCellsStyle = excelUtilsService.createDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillEmergencyRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.fillRow(sheet, excelUtilsService.addTotalCountRow(workbook, sheet, 4, 7));

		excelUtilsService.setMinimalHeaderDimensions(sheet);
		excelUtilsService.setSheetDimensions(sheet);

		return workbook;
	}

	public IWorkbook buildDiabeticsOrHypertensivesExcel(String title, String[] headers, List<DiabeticHypertensionConsultationDetail> result, Integer institutionId, LocalDate startDate, LocalDate endDate) {
		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();
		excelUtilsService.createHeaderCellsStyle(workbook);
		ISheet sheet = workbook.createSheet(title);
		excelUtilsService.fillRow(sheet, excelUtilsService.getHeaderDataWithoutObservation(headers, title, 11, 0, excelUtilsService.periodStringFromLocalDates(startDate, endDate), institutionId, null));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle dataCellsStyle = excelUtilsService.createDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillDiabeticsOrHypertensivesRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.setMinimalHeaderDimensions(sheet);
		excelUtilsService.setSheetDimensions(sheet);

		return workbook;
	}

	public IWorkbook buildComplementaryStudiesExcel(String title, String[] headers, List<ComplementaryStudiesConsultationDetail> result, Integer institutionId, LocalDate startDate, LocalDate endDate) {
		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();
		excelUtilsService.createHeaderCellsStyle(workbook);
		ISheet sheet = workbook.createSheet(title);
		excelUtilsService.fillRow(sheet, excelUtilsService.getHeaderDataWithoutObservation(headers, title, 16, 0, excelUtilsService.periodStringFromLocalDates(startDate, endDate), institutionId, null));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle dataCellsStyle = excelUtilsService.createDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillComplementaryStudiesRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.setMinimalHeaderDimensions(sheet);
		excelUtilsService.setSheetDimensions(sheet);

		return workbook;
	}

	public IWorkbook buildMedicinesPrescriptionExcel(String title, String[] headers, List<MedicinesPrescriptionConsultationDetail> result, Integer institutionId, LocalDate startDate, LocalDate endDate) {
		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();
		excelUtilsService.createHeaderCellsStyle(workbook);
		ISheet sheet = workbook.createSheet(title);
		excelUtilsService.fillRow(sheet, excelUtilsService.getHeaderDataWithoutObservation(headers, title, 21, 0, excelUtilsService.periodStringFromLocalDates(startDate, endDate), institutionId, null));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle dataCellsStyle = excelUtilsService.createDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillMedicinesPrescriptionRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.setMinimalHeaderDimensions(sheet);
		excelUtilsService.setSheetDimensions(sheet);

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

	public void fillDiabeticsOrHypertensivesRow(IRow row, DiabeticHypertensionConsultationDetail content, ICellStyle style) {
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

	public void fillComplementaryStudiesRow(IRow row, ComplementaryStudiesConsultationDetail content, ICellStyle style) {
		excelUtilsService.setCellValue(row, 0, style, content.getServiceRequestDate());
		excelUtilsService.setCellValue(row, 1, style, content.getServiceRequestCategory());
		excelUtilsService.setCellValue(row, 2, style, content.getOrderStatus());
		excelUtilsService.setCellValue(row, 3, style, content.getRequestType());
		excelUtilsService.setCellValue(row, 4, style, content.getRequestOrigin());
		excelUtilsService.setCellValue(row, 5, style, content.getPatientFullName());
		excelUtilsService.setCellValue(row, 6, style, content.getPatientDocumentType());
		excelUtilsService.setCellValue(row, 7, style, content.getPatientDocumentNumber());
		excelUtilsService.setCellValue(row, 8, style, content.getMedicalCoverage());
		excelUtilsService.setCellValue(row, 9, style, content.getAffiliateNumber());
		excelUtilsService.setCellValue(row, 10, style, content.getProfessionalFullName());
		excelUtilsService.setCellValue(row, 11, style, content.getProfessionalDocumentType());
		excelUtilsService.setCellValue(row, 12, style, content.getProfessionalDocumentNumber());
		excelUtilsService.setCellValue(row, 13, style, content.getLicense());
		excelUtilsService.setCellValue(row, 14, style, content.getNote());
		excelUtilsService.setCellValue(row, 15, style, content.getIssueDate());
		excelUtilsService.setCellValue(row, 16, style, content.getStudyName());
		excelUtilsService.setCellValue(row, 17, style, content.getAdditionalNotes());
		excelUtilsService.setCellValue(row, 18, style, content.getAssociatedProblem());
	}

	public void fillMedicinesPrescriptionRow(IRow row, MedicinesPrescriptionConsultationDetail content, ICellStyle style) {
		excelUtilsService.setCellValue(row, 0, style, content.getPrescriber());
		excelUtilsService.setCellValue(row, 1, style, content.getPrescriberLicense());
		excelUtilsService.setCellValue(row, 2, style, content.getPrescriberProvincialLicense());
		excelUtilsService.setCellValue(row, 3, style, content.getPrescriberNationalLicense());
		excelUtilsService.setCellValue(row, 4, style, dateTools.newReformatDate(content.getDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 5, style, content.getPatient());
		excelUtilsService.setCellValue(row, 6, style, content.getPatientDNI());
		excelUtilsService.setCellValue(row, 7, style, content.getMedicalCoverage());
		excelUtilsService.setCellValue(row, 8, style, content.getRelatedDiagnosis());
		excelUtilsService.setCellValue(row, 9, style, content.getIsChronic());
		excelUtilsService.setCellValue(row, 10, style, content.getEvent());
		excelUtilsService.setCellValue(row, 11, style, content.getPrescription());
		excelUtilsService.setCellValue(row, 12, style, content.getMedicine());
		excelUtilsService.setCellValue(row, 13, style, content.getDuration());
		excelUtilsService.setCellValue(row, 14, style, content.getFrequency());
		excelUtilsService.setCellValue(row, 15, style, dateTools.newReformatDate(content.getStartDate(), "dd-MM-yyyy HH:mm"));
		excelUtilsService.setCellValue(row, 16, style, dateTools.newReformatDate(content.getEndDate(), "dd-MM-yyyy HH:mm"));
		excelUtilsService.setCellValue(row, 17, style, dateTools.newReformatDate(content.getSuspensionStartDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 18, style, dateTools.newReformatDate(content.getSuspensionEndDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 19, style, content.getDosage());
		excelUtilsService.setCellValue(row, 20, style, content.getDosePerDay());
		excelUtilsService.setCellValue(row, 21, style, content.getDosePerUnit());
		excelUtilsService.setCellValue(row, 22, style, content.getObservations());
		excelUtilsService.setCellValue(row, 23, style, content.getPrescriptionStatus());
	}
}
