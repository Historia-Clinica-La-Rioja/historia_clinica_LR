package net.pladema.provincialreports.reportformat.domain.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.reports.util.CellContent;
import ar.lamansys.sgx.shared.reports.util.struct.ICell;
import ar.lamansys.sgx.shared.reports.util.struct.ICellStyle;
import ar.lamansys.sgx.shared.reports.util.struct.IRow;
import ar.lamansys.sgx.shared.reports.util.struct.ISheet;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.establishment.repository.entity.Institution;

@Slf4j
@Service
public class ReportExcelUtilsService {


	private static final String DATE_FORMAT_WITH_SLASH = "dd/MM/yyyy";
	private static final String DATE_FORMAT_WITH_DASH = "dd-MM-yyyy";
	private static final String DATE_FORMAT_WITH_DOT = "dd.MM.yyyy";
	private static final String SOURCE_NOTE = "HSI ha sido la fuente de los datos presentados en este documento";

	@Autowired
	private final InstitutionRepository institutionRepository;

	private ICellStyle fieldStyle;
	private ICellStyle boldTitleStyle;
	private ICellStyle mainTitleStyle;
	private ICellStyle observationStyle;
	private ICellStyle sourceNoteStyle;

	public ReportExcelUtilsService(InstitutionRepository institutionRepository) {
		this.institutionRepository = institutionRepository;
	}

	public List<CellContent> getHeaderDataWithoutObservation(String[] subtitles, String title, Integer mainTitleColumns, String formattedPeriod, Integer institutionId) {
		// recommended minimum mainTitleColumns = 7
		Institution institution = institutionRepository.findById(institutionId)
				.orElseThrow(() -> new IllegalArgumentException("Institution not found with id " + institutionId));

		List<CellContent> data = new ArrayList<>();
		int rowNum = 0; // row 1

		data.add(new CellContent(rowNum, 0, 1, 3, " ", fieldStyle));
		data.add(new CellContent(rowNum, 3, 2, mainTitleColumns, title, mainTitleStyle));

		rowNum++;

		data.add(new CellContent(rowNum, 0, 1, 1, "PERÍODO:", boldTitleStyle));
		data.add(new CellContent(rowNum, 1, 1, 2, formattedPeriod, fieldStyle));

		rowNum++;

		data.add(new CellContent(rowNum, 0, 1, 1, "FECHA DE EMISIÓN:", boldTitleStyle));
		data.add(new CellContent(rowNum, 1, 1, 2, currentDateAsDDMMYYYY("dash"), fieldStyle));
		data.add(new CellContent(rowNum, 3, 2, mainTitleColumns, SOURCE_NOTE, sourceNoteStyle));

		rowNum++;

		data.add(new CellContent(rowNum, 0, 1, 1, "INSTITUCIÓN:", boldTitleStyle));
		data.add(new CellContent(rowNum, 1, 1, 2, institution.getName(), fieldStyle));

		rowNum++;

		data.add(new CellContent(rowNum, 0, 1, mainTitleColumns + 3, " ", fieldStyle));

		rowNum++;

		int colNum = 0;

		for (String subtitle : subtitles) {
			data.add(new CellContent(rowNum, colNum++, 1, 1, subtitle, boldTitleStyle));
		}

		return data;
	}

	public void fillRow(ISheet sheet, List<CellContent> data) {
		Map<Integer, List<CellContent>> cellByRow = data.stream()
				.collect(Collectors.groupingBy(CellContent::getRow));

		for (Map.Entry<Integer, List<CellContent>> entry : cellByRow.entrySet()) {
			int rowNum = entry.getKey();
			IRow row = sheet.createRow(rowNum);
			entry.getValue().forEach(cell -> createCell(sheet, row, rowNum, cell));
		}
	}

	public ICellStyle getDataCellsStyle(IWorkbook workbook) {
		ICellStyle cellStyle = workbook.createStyle();
		cellStyle.setFontSize((short) 12);
		cellStyle.setBorders(true);
		cellStyle.setHAlign(ICellStyle.HALIGNMENT.LEFT);
		cellStyle.setVAlign(ICellStyle.VALIGNMENT.TOP);
		return cellStyle;
	}

	private void createCell(ISheet sheet, IRow row, int rowNum, CellContent data) {
		int columnNum = data.getColumn();

		ICell cell = row.createCell(columnNum);
		cell.setCellStyle(data.getStyle());
		cell.setCellValue(data);

		if (data.isCellRange()) {
			sheet.addMergedRegion(rowNum, data.lastRow(), columnNum, data.lastCol(), true);
		}
	}

	public void createHeaderCellsStyle(IWorkbook workbook) {

		fieldStyle = workbook.createStyle();
		fieldStyle.setFontSize((short) 12);
		fieldStyle.setBold(false);
		fieldStyle.setWrap(false);
		fieldStyle.setBorders(true);
		fieldStyle.setHAlign(ICellStyle.HALIGNMENT.LEFT);
		fieldStyle.setVAlign(ICellStyle.VALIGNMENT.CENTER);

		boldTitleStyle = workbook.createStyle();
		boldTitleStyle.setFontSize((short) 13);
		boldTitleStyle.setBold(true);
		boldTitleStyle.setWrap(false);
		boldTitleStyle.setBorders(true);
		boldTitleStyle.setHAlign(ICellStyle.HALIGNMENT.LEFT);
		boldTitleStyle.setVAlign(ICellStyle.VALIGNMENT.CENTER);

		mainTitleStyle = workbook.createStyle();
		mainTitleStyle.setFontSize((short) 24);
		mainTitleStyle.setBold(true);
		mainTitleStyle.setWrap(false);
		mainTitleStyle.setBorders(true);
		mainTitleStyle.setHAlign(ICellStyle.HALIGNMENT.CENTER);
		mainTitleStyle.setVAlign(ICellStyle.VALIGNMENT.CENTER);

		observationStyle = workbook.createStyle();
		observationStyle.setFontSize((short) 12);
		observationStyle.setBold(false);
		observationStyle.setWrap(false);
		observationStyle.setBorders(true);
		observationStyle.setHAlign(ICellStyle.HALIGNMENT.CENTER);
		observationStyle.setVAlign(ICellStyle.VALIGNMENT.CENTER);

		sourceNoteStyle = workbook.createStyle();
		sourceNoteStyle.setFontSize((short) 12);
		sourceNoteStyle.setBold(false);
		sourceNoteStyle.setWrap(false);
		sourceNoteStyle.setBorders(true);
		sourceNoteStyle.setHAlign(ICellStyle.HALIGNMENT.CENTER);
		sourceNoteStyle.setVAlign(ICellStyle.VALIGNMENT.TOP);

	}

	public void setSheetDimensions(ISheet sheet) {
		sheet.autoSizeColumns();

		int rowNumber = 0;
		sheet.setRowHeight(rowNumber++, 40);
		sheet.setRowHeight(rowNumber++, 23);
		sheet.setRowHeight(rowNumber++, 23);
		sheet.setRowHeight(rowNumber++, 23);
		sheet.setRowHeight(rowNumber++, 23);

		do sheet.setRowHeight(rowNumber++, 23); while (rowNumber < sheet.getCantRows());

	}

	public void setMinimalHeaderDimensions(ISheet sheet) {
		int columnNumber = 0;
		sheet.setColumnWidth(columnNumber++, 175);
		sheet.setColumnWidth(columnNumber++, 125);
		sheet.setColumnWidth(columnNumber++, 125);

		do sheet.setColumnWidth(columnNumber++, 125); while (columnNumber < 10);

	}

	public String currentDateAsDDMMYYYY(String separator) {
		LocalDate currentDate = LocalDate.now();

		if (Objects.equals(separator, "backslash")) {

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			return currentDate.format(formatter);

		} else if (Objects.equals(separator, "dash")) {

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			return currentDate.format(formatter);

		}

		return "Specify separator as function arg";
	}

	public String periodStringFromLocalDates(LocalDate startDate, LocalDate endDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		String formattedStartDate = startDate.format(formatter);
		String formattedEndDate = endDate.format(formatter);

		return formattedStartDate + " - " + formattedEndDate;
	}

	public String getPeriodForFilenameFromDates(LocalDate startDate, LocalDate endDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

		String formattedStartDate = startDate.format(formatter);
		String formattedEndDate = endDate.format(formatter);

		return formattedStartDate + " - " + formattedEndDate;
	}

	// new code with reduced boilerplate

	public List<CellContent> newGetHeaderDataWithoutObservation(String[] subtitles, String title,
															 @NonNull Integer mainTitleColumns,
															 String formattedPeriod,
															 @NonNull Integer institutionId) {
		// recommended minimum mainTitleColumns = 7
		Institution institution = institutionRepository.findById(institutionId)
				.orElseThrow(() -> new IllegalArgumentException("Institution not found with id " + institutionId));

		List<CellContent> data = new ArrayList<>();
		int rowNum = 0; // row 1

		data.add(new CellContent(rowNum, 0, 1, 3, " ", fieldStyle));
		data.add(new CellContent(rowNum, 3, 2, mainTitleColumns, title, mainTitleStyle));

		rowNum++;

		data.add(new CellContent(rowNum, 0, 1, 1, "PERÍODO:", boldTitleStyle));
		data.add(new CellContent(rowNum, 1, 1, 2, formattedPeriod, fieldStyle));

		rowNum++;

		data.add(new CellContent(rowNum, 0, 1, 1, "FECHA DE EMISIÓN:", boldTitleStyle));
		data.add(new CellContent(rowNum, 1, 1, 2, currentDateAsDDMMYYYY("dash"), fieldStyle));
		data.add(new CellContent(rowNum, 3, 2, mainTitleColumns, SOURCE_NOTE, sourceNoteStyle));

		rowNum++;

		data.add(new CellContent(rowNum, 0, 1, 1, "INSTITUCIÓN:", boldTitleStyle));
		data.add(new CellContent(rowNum, 1, 1, 2, institution.getName(), fieldStyle));

		rowNum++;

		data.add(new CellContent(rowNum, 0, 1, mainTitleColumns + 3, " ", fieldStyle));

		rowNum++;

		int colNum = 0;

		for (String subtitle : subtitles) {
			data.add(new CellContent(rowNum, colNum++, 1, 1, subtitle, boldTitleStyle));
		}

		return data;
	}

	public void newFillRow(ISheet sheet, List<CellContent> data) {
		Map<Integer, List<CellContent>> cellByRow = data.stream()
				.collect(Collectors.groupingBy(CellContent::getRow));

		for (var entry : cellByRow.entrySet()) {
			int rowNum = entry.getKey();
			IRow row = sheet.createRow(rowNum);
			entry.getValue().forEach(cell -> createCell(sheet, row, rowNum, cell));
		}
	}

	public ICellStyle newCreateDataCellsStyle(IWorkbook workbook) {
		ICellStyle cellStyle = workbook.createStyle();
		cellStyle.setFontSize((short) 12);
		cellStyle.setBorders(true);
		cellStyle.setHAlign(ICellStyle.HALIGNMENT.LEFT);
		cellStyle.setVAlign(ICellStyle.VALIGNMENT.TOP);
		return cellStyle;
	}

	public void newCreateHeaderCellsStyle(IWorkbook workbook) {
		fieldStyle = createStyle(workbook, (short) 12, false, ICellStyle.HALIGNMENT.LEFT, ICellStyle.VALIGNMENT.CENTER);
		boldTitleStyle = createStyle(workbook, (short) 13, true, ICellStyle.HALIGNMENT.LEFT, ICellStyle.VALIGNMENT.CENTER);
		mainTitleStyle = createStyle(workbook, (short) 24, true, ICellStyle.HALIGNMENT.CENTER, ICellStyle.VALIGNMENT.CENTER);
		observationStyle = createStyle(workbook, (short) 12, false, ICellStyle.HALIGNMENT.CENTER, ICellStyle.VALIGNMENT.CENTER);
		sourceNoteStyle = createStyle(workbook, (short) 12, false, ICellStyle.HALIGNMENT.CENTER, ICellStyle.VALIGNMENT.TOP);
	}

	public void newSetSheetDimensions(ISheet sheet) {
		sheet.autoSizeColumns();

		sheet.setRowHeight(0, 40);
		for (int rowNumber = 1; rowNumber < sheet.getCantRows(); rowNumber++) {
			sheet.setRowHeight(rowNumber, 23);
		}
	}

	public void newSetMinimalHeaderDimensions(ISheet sheet) {
		sheet.setColumnWidth(0, 175);
		for (int columnNumber = 1; columnNumber < 10; columnNumber++) {
			sheet.setColumnWidth(columnNumber, 125);
		}
	}

	public void setCellValue(IRow row, int cellIndex, ICellStyle style, String value) {
		ICell cell = row.createCell(cellIndex);
		cell.setCellStyle(style);
		cell.setCellValue(value);
	}

	public static String newCurrentDateAsDDMMYYYY(String separator) {
		LocalDate currentDate = LocalDate.now();
		String pattern;
		switch (separator) {
			case "backslash":
				pattern = DATE_FORMAT_WITH_SLASH;
				break;
			case "dash":
				pattern = DATE_FORMAT_WITH_DASH;
				break;
			default:
				throw new IllegalArgumentException("Invalid separator: " + separator);
		}
		return currentDate.format(DateTimeFormatter.ofPattern(pattern));
	}

	public static String newPeriodStringFromLocalDates(LocalDate startDate, LocalDate endDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_WITH_SLASH);
		return String.format("%s - %s", startDate.format(formatter), endDate.format(formatter));
	}

	public static String newGetPeriodForFilenameFromDates(LocalDate startDate, LocalDate endDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_WITH_DOT);
		return String.format("%s - %s", startDate.format(formatter), endDate.format(formatter));
	}

	private ICellStyle createStyle(IWorkbook workbook, short fontSize, boolean bold,
								   ICellStyle.HALIGNMENT hAlign, ICellStyle.VALIGNMENT vAlign) {
		ICellStyle style = workbook.createStyle();
		style.setFontSize(fontSize);
		style.setBold(bold);
		style.setWrap(false);
		style.setBorders(true);
		style.setHAlign(hAlign);
		style.setVAlign(vAlign);
		return style;
	}
}