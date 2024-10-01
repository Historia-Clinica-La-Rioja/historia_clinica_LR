package net.pladema.provincialreports.reportformat.domain.service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

	private void createCell(ISheet sheet, IRow row, int rowNum, CellContent data) {
		int columnNum = data.getColumn();

		ICell cell = row.createCell(columnNum);
		cell.setCellStyle(data.getStyle());
		cell.setCellValue(data);

		if (data.isCellRange()) {
			sheet.addMergedRegion(rowNum, data.lastRow(), columnNum, data.lastCol(), true);
		}
	}

	public List<CellContent> newGetHeaderDataWithoutObservation(String[] subtitles, String title,
															 @NonNull Integer mainTitleColumns,
															 Integer rowsBetweenTitleAndHeaders,
															 String formattedPeriod,
															 @NonNull Integer institutionId,
															 String customObservation) {
		// recommended minimum mainTitleColumns = 7
		Institution institution = institutionRepository.findById(institutionId)
				.orElseThrow(() -> new IllegalArgumentException("Institution not found with id " + institutionId));

		List<CellContent> data = new ArrayList<>();
		int rowNum = 0; // row 1

		data.add(new CellContent(rowNum, 0, 1, 3, " ", fieldStyle));
		data.add(new CellContent(rowNum, 3, 2, mainTitleColumns, title, mainTitleStyle));

		rowNum++;

		String periodText = (formattedPeriod == null || formattedPeriod.isEmpty()) ? "Período no especificado" : formattedPeriod;

		data.add(new CellContent(rowNum, 0, 1, 1, "PERÍODO:", boldTitleStyle));
		data.add(new CellContent(rowNum, 1, 1, 2, periodText, fieldStyle));

		rowNum++;

		data.add(new CellContent(rowNum, 0, 1, 1, "FECHA DE EMISIÓN:", boldTitleStyle));
		data.add(new CellContent(rowNum, 1, 1, 2, newCurrentDateAsDDMMYYYY("dash"), fieldStyle));
		if (customObservation == null || customObservation.isEmpty()) {
			// No observation
			data.add(new CellContent(rowNum, 3, 2 + rowsBetweenTitleAndHeaders, mainTitleColumns, SOURCE_NOTE, sourceNoteStyle));
		} else {
			// Observation present
			data.add(new CellContent(rowNum, 3, 1, mainTitleColumns, SOURCE_NOTE, sourceNoteStyle));
			rowNum++;
			data.add(new CellContent(rowNum, 0, 1, 3, " ", sourceNoteStyle));
			data.add(new CellContent(rowNum, 3, 2 + rowsBetweenTitleAndHeaders, mainTitleColumns, customObservation, observationStyle));
		}

		rowNum++;

		data.add(new CellContent(rowNum, 0, 1, 1, "INSTITUCIÓN:", boldTitleStyle));
		data.add(new CellContent(rowNum, 1, 1, 2, institution.getName(), fieldStyle));

		rowNum++;

		// addition of empty row(s) between title and headers

		for (int i = 0; i < rowsBetweenTitleAndHeaders; i++) {
			for (int j = 0; j < mainTitleColumns + 3; j++) {
				data.add(new CellContent(rowNum, j, 1, 1, " ", fieldStyle));
			}
			rowNum++;
		}

		data.add(new CellContent(rowNum, 0, 1, mainTitleColumns + 3, " ", fieldStyle));

		rowNum++;

		int colNum = 0;

		for (String subtitle : subtitles) {
			data.add(new CellContent(rowNum, colNum++, 1, 1, subtitle, boldTitleStyle));
		}

		return data;
	}

	public List<CellContent> addTotalCountRow(IWorkbook wb, ISheet sheet, Integer rowIndex, Integer headerRows) {

		List<CellContent> data = new ArrayList<>();

		ICellStyle fieldStyle = createStyle(wb, (short) 12, false, ICellStyle.HALIGNMENT.LEFT, ICellStyle.VALIGNMENT.CENTER);
		ICellStyle boldTitleStyle = createStyle(wb, (short) 13, true, ICellStyle.HALIGNMENT.LEFT, ICellStyle.VALIGNMENT.CENTER);

		data.add(new CellContent(rowIndex, 0, 1, 1, "TOTAL DE ATENCIONES:", boldTitleStyle));
		data.add(new CellContent(rowIndex, 1, 1, 2, String.valueOf(sheet.getCantRows() - headerRows), fieldStyle));

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

	public String newPeriodStringFromLocalDates(LocalDate startDate, LocalDate endDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_WITH_SLASH);
		return String.format("%s - %s", startDate.format(formatter), endDate.format(formatter));
	}

	public String newGetPeriodForFilenameFromDates(LocalDate startDate, LocalDate endDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_WITH_DOT);
		return String.format("%s - %s", startDate.format(formatter), endDate.format(formatter));
	}

	public ICellStyle createStyle(IWorkbook workbook, short fontSize, boolean bold,
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

	public ResponseEntity<byte[]> createResponseEntity(IWorkbook wb, String filename) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		wb.write(outputStream);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
				.contentType(MediaType.parseMediaType(wb.getContentType()))
				.body(outputStream.toByteArray());
	}
}