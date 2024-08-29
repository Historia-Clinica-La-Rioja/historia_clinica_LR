package net.pladema.provincialreports.odontologicalreports.service;

import ar.lamansys.sgx.shared.reports.util.manager.WorkbookCreator;
import ar.lamansys.sgx.shared.reports.util.struct.ICellStyle;
import ar.lamansys.sgx.shared.reports.util.struct.IRow;
import ar.lamansys.sgx.shared.reports.util.struct.ISheet;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.provincialreports.generalreports.repository.DiabeticHypertensionConsultationDetail;
import net.pladema.provincialreports.odontologicalreports.repository.OdontologicalProceduresConsultationDetail;
import net.pladema.provincialreports.odontologicalreports.repository.OdontologyConsultationDetail;
import net.pladema.provincialreports.reportformat.DateFormat;

import net.pladema.provincialreports.reportformat.domain.service.ReportExcelUtilsService;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OdontologicalReportsExcelService {
	private final DateFormat dateTools;

	private final ReportExcelUtilsService excelUtilsService;

	public OdontologicalReportsExcelService(DateFormat dateTools, ReportExcelUtilsService excelUtilsService) {
		this.dateTools = dateTools;
		this.excelUtilsService = excelUtilsService;
	}

	public IWorkbook buildOdontologyExcel(String title, String[] headers, List<OdontologyConsultationDetail> result, Integer institutionId, LocalDate startDate, LocalDate endDate) {
		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();
		excelUtilsService.newCreateHeaderCellsStyle(workbook);
		ISheet sheet = workbook.createSheet(title);
		excelUtilsService.newFillRow(sheet, excelUtilsService.newGetHeaderDataWithoutObservation(headers, title, 7, 0, excelUtilsService.newPeriodStringFromLocalDates(startDate, endDate), institutionId));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle dataCellsStyle = excelUtilsService.newCreateDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillOdontologyRow(row, resultData, dataCellsStyle);
		});

		excelUtilsService.newSetMinimalHeaderDimensions(sheet);
		excelUtilsService.newSetSheetDimensions(sheet);

		return workbook;
	}

	public IWorkbook buildOdontologicalProceduresExcel(String title, String[] headers, List<OdontologicalProceduresConsultationDetail> result, Integer institutionId, LocalDate startDate, LocalDate endDate) {
		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();
		excelUtilsService.newCreateHeaderCellsStyle(workbook);
		ISheet sheet = workbook.createSheet(title);
		excelUtilsService.newFillRow(sheet, excelUtilsService.newGetHeaderDataWithoutObservation(headers, title, 20, 0, excelUtilsService.newPeriodStringFromLocalDates(startDate, endDate), institutionId));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle dataCellsStyle = excelUtilsService.newCreateDataCellsStyle(workbook);

		result.forEach(resultData -> {
			IRow row = sheet.createRow(rowNumber.getAndIncrement());
			fillOdontologicalProceduresExcel(row, resultData, dataCellsStyle);
		});

		excelUtilsService.newSetMinimalHeaderDimensions(sheet);
		excelUtilsService.newSetSheetDimensions(sheet);

		return workbook;
	}

	public void fillOdontologyRow(IRow row, OdontologyConsultationDetail content, ICellStyle style) {
		excelUtilsService.setCellValue(row, 0, style, content.getProfessional());
		excelUtilsService.setCellValue(row, 1, style, content.getProcedures());
		excelUtilsService.setCellValue(row, 2, style, content.getCounter());
	}

	public void fillOdontologicalProceduresExcel(IRow row, OdontologicalProceduresConsultationDetail content, ICellStyle style) {
		excelUtilsService.setCellValue(row, 0, style, content.getProfessionalName());
		excelUtilsService.setCellValue(row, 1, style, content.getProfessionalIdentificationNumber());
		excelUtilsService.setCellValue(row, 2, style, content.getProfessionalLicenseNumber());
		excelUtilsService.setCellValue(row, 3, style, dateTools.newReformatDate(content.getAttentionDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 4, style, content.getAttentionHour());
		excelUtilsService.setCellValue(row, 5, style, content.getPatientNames());
		excelUtilsService.setCellValue(row, 6, style, content.getPatientIdentificationNumber());
		excelUtilsService.setCellValue(row, 7, style, content.getPatientGender());
		excelUtilsService.setCellValue(row, 8, style, dateTools.newReformatDate(content.getPatientBirthDate(), "dd-MM-yyyy"));
		excelUtilsService.setCellValue(row, 9, style, content.getPatientAgeTurn());
		excelUtilsService.setCellValue(row, 10, style, content.getPatientMedicalCoverage());
		excelUtilsService.setCellValue(row, 11, style, content.getPatientAddress());
		excelUtilsService.setCellValue(row, 12, style, content.getPatientLocation());
		excelUtilsService.setCellValue(row, 13, style, content.getPatientPermanentCPO());
		excelUtilsService.setCellValue(row, 14, style, content.getPatientTemporaryCEO());
		excelUtilsService.setCellValue(row, 15, style, content.getReasons());
		excelUtilsService.setCellValue(row, 16, style, content.getOtherDiagnoses());
		excelUtilsService.setCellValue(row, 17, style, content.getOtherProcedures());
		excelUtilsService.setCellValue(row, 18, style, content.getAllergies());
		excelUtilsService.setCellValue(row, 19, style, content.getUsualMedication());
		excelUtilsService.setCellValue(row, 20, style, content.getDentalDiagnoses());
		excelUtilsService.setCellValue(row, 21, style, content.getDentalProcedures());
		excelUtilsService.setCellValue(row, 22, style, content.getEvolution());
	}
}
