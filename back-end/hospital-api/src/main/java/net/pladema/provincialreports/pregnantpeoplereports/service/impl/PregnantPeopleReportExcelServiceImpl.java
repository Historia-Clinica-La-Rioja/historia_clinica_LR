package net.pladema.provincialreports.pregnantpeoplereports.service.impl;

import ar.lamansys.sgx.shared.reports.util.CellContent;
import ar.lamansys.sgx.shared.reports.util.manager.WorkbookCreator;
import ar.lamansys.sgx.shared.reports.util.struct.ICell;
import ar.lamansys.sgx.shared.reports.util.struct.ICellStyle;
import ar.lamansys.sgx.shared.reports.util.struct.IRow;
import ar.lamansys.sgx.shared.reports.util.struct.ISheet;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.provincialreports.pregnantpeoplereports.repository.PregnantAttentionsConsultationDetail;
import net.pladema.provincialreports.pregnantpeoplereports.repository.PregnantControlsConsultationDetail;
import net.pladema.provincialreports.pregnantpeoplereports.service.PregnantPeopleReportExcelService;

import net.pladema.provincialreports.reportformat.DateFormat;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class PregnantPeopleReportExcelServiceImpl implements PregnantPeopleReportExcelService {

	private ICellStyle basicStyle;
	private ICellStyle titleStyle;
	private ICellStyle fieldStyle;
	private ICellStyle subTitleStyle;

	private final DateFormat reformatdate;

	public PregnantPeopleReportExcelServiceImpl(DateFormat reformatdate) {
		this.reformatdate = reformatdate;
	}

	@Override
	public IWorkbook buildExcelPregnantAttentions(String title, String[] headers, List<PregnantAttentionsConsultationDetail> result, String startDate, String endDate, String institutionName, String observations) {
		IWorkbook wb = WorkbookCreator.createExcelWorkbook();
		createCellStyle(wb);

		ISheet sheet = wb.createSheet(title);

		fillRow(sheet, getHeaderData(headers, title, startDate, endDate, institutionName, observations));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());

		ICellStyle styleDataRow = createDataRowStyle(wb);

		result.forEach(resultData -> {
			IRow newDataRow = sheet.createRow(rowNumber.getAndIncrement());
			fillRowContent(newDataRow, resultData, styleDataRow);
		});

		setDimensions(sheet);
		return wb;
	}

	@Override
	public IWorkbook buildExcelPregnantControls(String title, String[] headers, List<PregnantControlsConsultationDetail> result, String startDate, String endDate, String institutionName, String observations) {
		IWorkbook wb = WorkbookCreator.createExcelWorkbook();
		createCellStyle(wb);

		ISheet sheet = wb.createSheet(title);

		fillRow(sheet, getHeaderData(headers, title, startDate, endDate, institutionName, observations));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());

		ICellStyle styleDataRow = createDataRowStyle(wb);

		result.forEach(resultData -> {
			IRow newDataRow = sheet.createRow(rowNumber.getAndIncrement());
			fillRowContent(newDataRow, resultData, styleDataRow);
		});

		setDimensions(sheet);
		return wb;
	}

	private void createCellStyle(IWorkbook workbook) {
		basicStyle = workbook.createStyle();
		basicStyle.setFontSize((short) 12);
		basicStyle.setBold(false);
		basicStyle.setWrap(false);
		basicStyle.setBorders(true);
		basicStyle.setHAlign(ICellStyle.HALIGNMENT.CENTER);
		basicStyle.setVAlign(ICellStyle.VALIGNMENT.CENTER);

		titleStyle = workbook.createStyle();
		titleStyle.setFontSize((short) 25);
		titleStyle.setBold(true);
		titleStyle.setWrap(false);
		titleStyle.setBorders(true);
		titleStyle.setHAlign(ICellStyle.HALIGNMENT.CENTER);
		titleStyle.setVAlign(ICellStyle.VALIGNMENT.CENTER);

		fieldStyle = workbook.createStyle();
		fieldStyle.setFontSize((short) 10);
		fieldStyle.setBold(false);
		fieldStyle.setWrap(false);
		fieldStyle.setBorders(true);
		fieldStyle.setHAlign(ICellStyle.HALIGNMENT.LEFT);
		fieldStyle.setVAlign(ICellStyle.VALIGNMENT.BOTTOM);

		subTitleStyle = workbook.createStyle();
		subTitleStyle.setFontSize((short) 12);
		subTitleStyle.setBold(true);
		subTitleStyle.setWrap(false);
		subTitleStyle.setBorders(true);
		subTitleStyle.setHAlign(ICellStyle.HALIGNMENT.LEFT);
		subTitleStyle.setVAlign(ICellStyle.VALIGNMENT.BOTTOM);
	}

	private List<CellContent> getHeaderData(String[] subtitles, String title, String startDate, String endDate, String institutionName, String observations) {
		List<CellContent> data = new ArrayList<>();

		int nRow = 0;

		data.add(new CellContent(nRow, 0, 1, 2, "", basicStyle));
		data.add(new CellContent(nRow, 2, 2, 1, "2", titleStyle));
		data.add(new CellContent(nRow, 3, 2, 16, title, titleStyle));
		data.add(new CellContent(nRow, 19, 1, 3, "1. Hoja N°", fieldStyle));
		data.add(new CellContent(nRow, 22, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 23, 1, 1, "", basicStyle));

		nRow++;
		data.add(new CellContent(nRow, 0, 1, 2, "PERIODO: " + reformatdate.ReformatDateThree(startDate) + " hasta " + reformatdate.ReformatDateThree(endDate), basicStyle));
		data.add(new CellContent(nRow, 19, 1, 5, "", basicStyle));

		nRow++;
		data.add(new CellContent(nRow, 0, 1, 2, "2. ESTABLECIMIENTO: " + institutionName, fieldStyle));
		data.add(new CellContent(nRow, 2, 1, 14, "OBSERVACIONES: " + observations, basicStyle));
		data.add(new CellContent(nRow, 16, 1, 1, "3. MES", fieldStyle));
		data.add(new CellContent(nRow, 17, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 18, 1, 1, "4. AÑO", fieldStyle));
		data.add(new CellContent(nRow, 19, 1, 5, "", basicStyle));

		nRow++;
		data.add(new CellContent(nRow, 0, 1, 2, "5. PARTIDO", fieldStyle));
		data.add(new CellContent(nRow, 2, 1, 8, "", basicStyle));
		data.add(new CellContent(nRow, 10, 1, 1, "6. REGIÓN SANITARIA", fieldStyle));
		data.add(new CellContent(nRow, 11, 1, 5, "", basicStyle));
		data.add(new CellContent(nRow, 16, 1, 3, "7. SERVICIO", fieldStyle));
		data.add(new CellContent(nRow, 19, 1, 3, "", basicStyle));
		data.add(new CellContent(nRow, 22, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 23, 1, 1, "", basicStyle));

		nRow++;
		int column = 0;
		for (String subtitle : subtitles)
			data.add(new CellContent(nRow, column++, 1, 1, subtitle, subTitleStyle));

		return data;
	}

	private void setDimensions(ISheet sheet) {
		sheet.autoSizeColumns();
		int nRow = 0;
		sheet.setRowHeight(nRow++, 50);
		sheet.setRowHeight(nRow++, 40);
		sheet.setRowHeight(nRow++, 35);

		while (nRow < sheet.getCantRows()) sheet.setRowHeight(nRow++, 21);
	}

	private ICellStyle createDataRowStyle(IWorkbook workbook) {
		ICellStyle cellStyle = workbook.createStyle();
		cellStyle.setFontSize((short) 12);
		cellStyle.setBorders(true);
		return cellStyle;
	}

	private void fillRow(ISheet sheet, List<CellContent> data) {
		Map<Integer, List<CellContent>> cellByRow = data.stream().collect(Collectors.groupingBy(CellContent::getRow));

		for (Map.Entry<Integer, List<CellContent>> entry : cellByRow.entrySet()) {
			int nRow = entry.getKey();
			IRow row = sheet.createRow(nRow);
			entry.getValue().forEach(cell -> createCell(sheet, row, nRow, cell));
		}
	}

	private void createCell(ISheet sheet, IRow row, int nRow, CellContent data) {

		int nColumn = data.getColumn();

		ICell cell = row.createCell(data.getColumn());
		cell.setCellStyle(data.getStyle());
		cell.setCellValue(data);

		if (data.isCellRange()) sheet.addMergedRegion(nRow, data.lastRow(), nColumn, data.lastCol(), true);
	}

	private void fillRowContent(IRow row, PregnantAttentionsConsultationDetail content, ICellStyle style) {
		AtomicInteger rowNumber = new AtomicInteger(0);

		ICell cell = row.createCell(rowNumber.getAndIncrement());
		cell.setCellValue(content.getOperativeUnit());
		cell.setCellStyle(style);

		ICell cell1 = row.createCell(rowNumber.getAndIncrement());
		cell1.setCellValue(content.getPatientIdentificationNumber());
		cell1.setCellStyle(style);

		ICell cell2 = row.createCell(rowNumber.getAndIncrement());
		cell2.setCellValue(content.getPatientLastName());
		cell2.setCellStyle(style);

		ICell cell3 = row.createCell(rowNumber.getAndIncrement());
		cell3.setCellValue(content.getPatientFirstName());
		cell3.setCellStyle(style);

		ICell cell4 = row.createCell(rowNumber.getAndIncrement());
		cell4.setCellValue(reformatdate.ReformatDateThree(content.getPatientBirthDate()));
		cell4.setCellStyle(style);

		ICell cell5 = row.createCell(rowNumber.getAndIncrement());
		cell5.setCellValue(content.getPatientAgeToday());
		cell5.setCellStyle(style);

		ICell cell6 = row.createCell(rowNumber.getAndIncrement());
		cell6.setCellValue(content.getPatientAddress());
		cell6.setCellStyle(style);

		ICell cell7 = row.createCell(rowNumber.getAndIncrement());
		cell7.setCellValue(content.getPhoneNumber());
		cell7.setCellStyle(style);

		ICell cell8 = row.createCell(rowNumber.getAndIncrement());
		cell8.setCellValue(content.getPatientLocation());
		cell8.setCellStyle(style);

		ICell cell9 = row.createCell(rowNumber.getAndIncrement());
		cell9.setCellValue(reformatdate.ReformatDateFour(content.getAttentionDate()));
		cell9.setCellStyle(style);

		ICell cell10 = row.createCell(rowNumber.getAndIncrement());
		cell10.setCellValue(content.getPatientMedicalCoverage());
		cell10.setCellStyle(style);

		ICell cell11 = row.createCell(rowNumber.getAndIncrement());
		cell11.setCellValue(content.getReasons());
		cell11.setCellStyle(style);

		ICell cell12 = row.createCell(rowNumber.getAndIncrement());
		cell12.setCellValue(content.getProblems());
		cell12.setCellStyle(style);

		ICell cell13 = row.createCell(rowNumber.getAndIncrement());
		cell13.setCellValue(content.getProcedures());
		cell13.setCellStyle(style);

		ICell cell14 = row.createCell(rowNumber.getAndIncrement());
		cell14.setCellValue(content.getEvolution());
		cell14.setCellStyle(style);
	}

	private void fillRowContent(IRow row, PregnantControlsConsultationDetail content, ICellStyle style) {
		AtomicInteger rowNumber = new AtomicInteger(0);

		ICell cell = row.createCell(rowNumber.getAndIncrement());
		cell.setCellValue(content.getPatientIdentificationNumber());
		cell.setCellStyle(style);

		ICell cell1 = row.createCell(rowNumber.getAndIncrement());
		cell1.setCellValue(content.getPatientLastName());
		cell1.setCellStyle(style);

		ICell cell2 = row.createCell(rowNumber.getAndIncrement());
		cell2.setCellValue(content.getPatientFirstName());
		cell2.setCellStyle(style);

		ICell cell3 = row.createCell(rowNumber.getAndIncrement());
		cell3.setCellValue(reformatdate.ReformatDateThree(content.getPatientBirthDate()));
		cell3.setCellStyle(style);

		ICell cell4 = row.createCell(rowNumber.getAndIncrement());
		cell4.setCellValue(content.getPatientAgeToday());
		cell4.setCellStyle(style);

		ICell cell5 = row.createCell(rowNumber.getAndIncrement());
		cell5.setCellValue(content.getPatientAddress());
		cell5.setCellStyle(style);

		ICell cell6 = row.createCell(rowNumber.getAndIncrement());
		cell6.setCellValue(content.getPhoneNumber());
		cell6.setCellStyle(style);

		ICell cell7 = row.createCell(rowNumber.getAndIncrement());
		cell7.setCellValue(content.getPatientLocation());
		cell7.setCellStyle(style);

		ICell cell8 = row.createCell(rowNumber.getAndIncrement());
		cell8.setCellValue(content.getNumberConsultations());
		cell8.setCellStyle(style);

		ICell cell9 = row.createCell(rowNumber.getAndIncrement());
		cell9.setCellValue(content.getConsultations());
		cell9.setCellStyle(style);

	}

}
