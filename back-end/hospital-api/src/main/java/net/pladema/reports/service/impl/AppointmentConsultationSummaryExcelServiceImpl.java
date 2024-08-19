package net.pladema.reports.service.impl;

import ar.lamansys.sgx.shared.reports.util.CellContent;
import ar.lamansys.sgx.shared.reports.util.manager.WorkbookCreator;
import ar.lamansys.sgx.shared.reports.util.struct.ICell;
import ar.lamansys.sgx.shared.reports.util.struct.ICellStyle;
import ar.lamansys.sgx.shared.reports.util.struct.IRow;
import ar.lamansys.sgx.shared.reports.util.struct.ISheet;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.reports.domain.AppointmentConsultationSummaryBo;
import net.pladema.reports.domain.ReportSearchFilterBo;
import net.pladema.reports.repository.InstitutionInfo;
import net.pladema.reports.service.AppointmentConsultationSummaryExcelService;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class AppointmentConsultationSummaryExcelServiceImpl implements AppointmentConsultationSummaryExcelService {

	private static final String REPORT_TITLE = "Resumen Mensual de Turnos en Consultorios Externos";
	private static final int FIRST_COLUMN_GENDER_DATA = 3;
	private static final int LAST_COLUMN_GENDER_DATA = 29;

	@Override
	public IWorkbook buildAppointmentConsultationSummaryExcelReport(String title, ReportSearchFilterBo filter, InstitutionInfo institutionInfo, List<AppointmentConsultationSummaryBo> appointmentConsultationSummaryData, AppointmentState appointmentState) {

		IWorkbook wb = WorkbookCreator.createExcelWorkbook();
		ISheet sheet = wb.createSheet(title);

		fillRow(
				sheet,
				getHeaderData(wb,
						institutionInfo,
						getDateField(filter.getFromDate(),filter.getToDate()),
						appointmentState
				)
		);
		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());
		ICellStyle styleDataRow = createDataRowStyle(wb);
		int firstDataRow = sheet.getCantRows();
		appointmentConsultationSummaryData.forEach(
				resultData -> {
					IRow newDataRow = sheet.createRow(rowNumber.getAndIncrement());
					fillACSRow(newDataRow, styleDataRow, resultData, wb);
				}
		);
		int lastDataRow = sheet.getCantRows();
		fillRow(sheet, getFooterData(sheet, firstDataRow, lastDataRow, wb));

		setDimensions(sheet);

		return wb;
	}

	public void fillRow(ISheet sheet, List<CellContent> data){
		Map<Integer, List<CellContent>> cellByRow = data.stream()
				.collect(Collectors.groupingBy(CellContent::getRow));

		for(Map.Entry<Integer, List<CellContent>> entry : cellByRow.entrySet()){
			int nRow = entry.getKey();
			IRow row = sheet.createRow(nRow);
			entry.getValue().forEach(cell -> createCell(sheet, row, nRow, cell));
		}
	}

	private List<CellContent> getHeaderData(IWorkbook wb, InstitutionInfo institutionInfo, String dateField, AppointmentState appointmentState) {
		List<CellContent> data = new ArrayList<>();

		var titleStyle = getTitleStyle(wb);
		var fieldStyle = getFieldStyle(wb);
		String appointmentStateField = appointmentState != null ? appointmentState.getDescription().toUpperCase() : " TODOS ";

		int nRow = 0;
		data.add(new CellContent(nRow, 0, 1, 1, "2.1", titleStyle));
		data.add(new CellContent(nRow, 1, 1, 30, REPORT_TITLE, titleStyle));
		data.add(new CellContent(nRow, 31, 1, 1, "Hoja N°", fieldStyle));
		data.add(new CellContent(nRow, 32,1, 1, " ", fieldStyle));
		data.add(new CellContent(nRow, 33,1, 1, " ", fieldStyle));

		nRow++;
		data.add(new CellContent(nRow, 0, 1, 1, "ESTABLECIMIENTO", fieldStyle));
		data.add(new CellContent(nRow, 1, 1, 2, institutionInfo.getInstitution().toUpperCase(), fieldStyle));
		data.add(new CellContent(nRow, 3, 1, 6, " ESTADO DE TURNOS ", fieldStyle));
		data.add(new CellContent(nRow, 9, 1, 6, appointmentStateField, fieldStyle));
		data.add(new CellContent(nRow, 15, 1, 9, "", fieldStyle));
		data.add(new CellContent(nRow, 24, 1, 5, "PERIODO", fieldStyle));
		data.add(new CellContent(nRow, 29, 1, 5, dateField, fieldStyle));

		nRow++;
		data.add(new CellContent(nRow, 0, 3, 1, " TIPO DE UNIDAD JERÁRQUICA ", fieldStyle));
		data.add(new CellContent(nRow, 1, 3, 1, " UNIDAD JERÁRQUICA ", fieldStyle));
		data.add(new CellContent(nRow, 2, 3, 1, "ESPECIALIDAD", fieldStyle));
		data.add(new CellContent(nRow, 3, 1, 28, "EDAD Y SEXO", fieldStyle));
		data.add(new CellContent(nRow, 31, 3, 1, " TOTAL ", fieldStyle));
		data.add(new CellContent(nRow, 32, 2, 2, " OBRA SOCIAL ", fieldStyle));

		nRow++;
		data.add(new CellContent(nRow, 0,1, 1, " ", fieldStyle));
		data.add(new CellContent(nRow, 1,1, 1, " ", fieldStyle));
		data.add(new CellContent(nRow, 3, 1, 3, "< 1 año", fieldStyle));
		data.add(new CellContent(nRow, 6, 1, 3, "1 a 4", fieldStyle));
		data.add(new CellContent(nRow, 9, 1, 3, "5 a 9", fieldStyle));
		data.add(new CellContent(nRow, 12, 1, 3, "10 a 14", fieldStyle));
		data.add(new CellContent(nRow, 15, 1, 3, "15 a 19", fieldStyle));
		data.add(new CellContent(nRow, 18, 1, 3, "20 a 34", fieldStyle));
		data.add(new CellContent(nRow, 21, 1, 3, "35 a 49", fieldStyle));
		data.add(new CellContent(nRow, 24, 1, 3, "50 a 64", fieldStyle));
		data.add(new CellContent(nRow, 27, 1, 3, "> 65", fieldStyle));
		data.add(new CellContent(nRow, 30, 2, 1, "Sin especificar", fieldStyle));
		data.add(new CellContent(nRow, 31,1, 1, " ", fieldStyle));
		data.add(new CellContent(nRow, 33,1, 1, " ", fieldStyle));

		nRow++;
		data.add(new CellContent(nRow, 0,1, 1, " ", fieldStyle));
		data.add(new CellContent(nRow, 1,1, 1, " ", fieldStyle));
		data.add(new CellContent(nRow, 30,1, 1, " ", fieldStyle));
		for (int i = FIRST_COLUMN_GENDER_DATA; i <= LAST_COLUMN_GENDER_DATA; i += 3) {
			data.add(new CellContent(nRow, i, 1, 1, "V", fieldStyle));
			data.add(new CellContent(nRow, i + 1, 1, 1, "M", fieldStyle));
			data.add(new CellContent(nRow, i + 2, 1, 1, "X", fieldStyle));
		}
		data.add(new CellContent(nRow, 32, 1, 1, "SI", fieldStyle));
		data.add(new CellContent(nRow, 33, 1, 1, "NO", fieldStyle));

		return data;
	}

	private String getDateField(LocalDate fromDate, LocalDate toDate){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return " " + fromDate.format(formatter) + " - " + toDate.format(formatter) + " ";
	}

	private void createCell(ISheet sheet, IRow row, int nRow, CellContent data){
		int nColumn = data.getColumn();

		ICell cell = row.createCell(data.getColumn());
		cell.setCellStyle(data.getStyle());
		cell.setCellValue(data);

		if(data.isCellRange())
			sheet.addMergedRegion(nRow, data.lastRow(), nColumn, data.lastCol(), true);
	}

	public ICellStyle createDataRowStyle(IWorkbook workbook){
		ICellStyle cellStyle = workbook.createStyle();
		cellStyle.setFontSize((short)12);
		cellStyle.setBorders(true);
		cellStyle.setHAlign(ICellStyle.HALIGNMENT.CENTER);
		cellStyle.setVAlign(ICellStyle.VALIGNMENT.CENTER);
		return cellStyle;
	}

	public ICellStyle getTitleStyle(IWorkbook workbook) {
		ICellStyle result = workbook.createStyle();
		result.setFontSize((short) 25);
		result.setBold(true);
		result.setWrap(false);
		result.setBorders(true);
		result.setHAlign(ICellStyle.HALIGNMENT.CENTER);
		result.setVAlign(ICellStyle.VALIGNMENT.CENTER);
		return result;
	}

	public ICellStyle getFieldStyle(IWorkbook workbook) {
		ICellStyle result = workbook.createStyle();
		result.setFontSize((short)10);
		result.setBold(false);
		result.setWrap(true);
		result.setBorders(true);
		result.setHAlign(ICellStyle.HALIGNMENT.CENTER);
		result.setVAlign(ICellStyle.VALIGNMENT.CENTER);
		return result;
	}

	public ICellStyle getBasicStyle(IWorkbook workbook) {
		ICellStyle result = workbook.createStyle();
		result.setFontSize((short) 10);
		result.setBold(false);
		result.setWrap(false);
		result.setBorders(true);
		result.setHAlign(ICellStyle.HALIGNMENT.LEFT);
		result.setVAlign(ICellStyle.VALIGNMENT.CENTER);
		return result;
	}

	private void fillACSRow(IRow row, ICellStyle style, AppointmentConsultationSummaryBo acsBo, IWorkbook workbook){
		AtomicInteger cellNumber = new AtomicInteger(0);

		fillHierarchicalAndSpecialtyCells(row, cellNumber, acsBo.getHierarchicalUnitType(),
				acsBo.getHierarchicalUnitAlias(), acsBo.getClinicalSpecialty(), workbook);

		fillGenderAndAgeCells(row, cellNumber, acsBo, style);

		ICell cell31 = row.createCell(cellNumber.getAndIncrement());
		cell31.setCellValue(new CellContent(acsBo.getUnspecified(), CellContent.DATAFORMAT.INTEGER));
		cell31.setCellStyle(style);

		ICell cell32 = row.createCell(cellNumber.getAndIncrement());
		cell32.setCellValue(new CellContent(acsBo.getTotal(), CellContent.DATAFORMAT.INTEGER));
		cell32.setCellStyle(style);

		fillCoverageCells(row, cellNumber, acsBo.getHasCoverage(), acsBo.getNoCoverage(), style);

	}

	private void fillHierarchicalAndSpecialtyCells(IRow row, AtomicInteger cellNumber, String hierarchicalUnitType,
												   String hierarchicalUnitAlias, String clinicalSpecialty, IWorkbook wb){
		var basicStyle = getBasicStyle(wb);

		ICell cell1 = row.createCell(cellNumber.getAndIncrement());
		cell1.setCellValue(hierarchicalUnitType);
		cell1.setCellStyle(basicStyle);

		ICell cell2 = row.createCell(cellNumber.getAndIncrement());
		cell2.setCellValue(hierarchicalUnitAlias);
		cell2.setCellStyle(basicStyle);

		ICell cell3 = row.createCell(cellNumber.getAndIncrement());
		cell3.setCellValue(clinicalSpecialty);
		cell3.setCellStyle(basicStyle);
	}

	private void fillGenderAndAgeCells(IRow row, AtomicInteger cellNumber, AppointmentConsultationSummaryBo acsBo, ICellStyle style){
		acsBo.getAllAgeRanges().forEach(
				ageRange -> {
					ICell cell = row.createCell(cellNumber.getAndIncrement());
					cell.setCellValue(
							new CellContent(ageRange, CellContent.DATAFORMAT.INTEGER)
					);
					cell.setCellStyle(style);
				}
		);
	}

	private void fillCoverageCells(IRow row, AtomicInteger cellNumber, Integer hasCoverage, Integer noCoverage, ICellStyle style){
		ICell cell33 = row.createCell(cellNumber.getAndIncrement());
		cell33.setCellValue(new CellContent(hasCoverage, CellContent.DATAFORMAT.INTEGER));
		cell33.setCellStyle(style);

		ICell cell34 = row.createCell(cellNumber.getAndIncrement());
		cell34.setCellValue(new CellContent(noCoverage, CellContent.DATAFORMAT.INTEGER));
		cell34.setCellStyle(style);
	}

	private List<CellContent> getFooterData(ISheet sheet, int firstRow, int lastRow, IWorkbook wb) {
		List<CellContent> data = new ArrayList<>();
		var fieldStyle = getFieldStyle(wb);
		var styleDataRow = createDataRowStyle(wb);

		int nColumn = 0;
		data.add(new CellContent(lastRow, nColumn,1, 3, "TOTALES ", fieldStyle));
		nColumn = FIRST_COLUMN_GENDER_DATA;
		while(nColumn <= 33) {
			String formula = getSumFunction(sheet, firstRow, lastRow-1, nColumn, nColumn);
			data.add(new CellContent(lastRow, nColumn++, 1, 1, formula, styleDataRow));
		}

		return data;
	}

	private String getSumFunction(ISheet sheet, int row, int lastRow, int column, int lastCol){
		String range = sheet.getCellRangeAsString(row, lastRow, column, lastCol);
		return range != null ? "=sum(" + range + ")" : null;
	}

	private void setDimensions(ISheet sheet) {
		sheet.autoSizeColumns();
		sheet.setColumnWidth(0, 220);
		sheet.setColumnWidth(1, 220);
		sheet.setColumnWidth(2, 300);
		for (int i = FIRST_COLUMN_GENDER_DATA; i <= LAST_COLUMN_GENDER_DATA; i++)
			sheet.setColumnWidth(i, 50);
		sheet.setColumnWidth(LAST_COLUMN_GENDER_DATA + 1, 100);
		sheet.setColumnWidth(LAST_COLUMN_GENDER_DATA + 2, 80);
		sheet.setColumnWidth(LAST_COLUMN_GENDER_DATA + 3, 80);
		sheet.setColumnWidth(LAST_COLUMN_GENDER_DATA + 4, 80);
		sheet.setRowHeight(0, 50);
		sheet.setRowHeight(1, 40);
	}
}
