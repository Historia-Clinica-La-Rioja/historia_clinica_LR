package net.pladema.provincialreports.programreports.service.impl;

import ar.lamansys.sgx.shared.reports.util.CellContent;
import ar.lamansys.sgx.shared.reports.util.manager.WorkbookCreator;
import ar.lamansys.sgx.shared.reports.util.struct.ICell;
import ar.lamansys.sgx.shared.reports.util.struct.ICellStyle;
import ar.lamansys.sgx.shared.reports.util.struct.IRow;
import ar.lamansys.sgx.shared.reports.util.struct.ISheet;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.provincialreports.programreports.repository.EpidemiologyOneConsultationDetail;
import net.pladema.provincialreports.programreports.repository.EpidemiologyTwoConsultationDetail;
import net.pladema.provincialreports.programreports.repository.RecuperoGeneralConsultationDetail;
import net.pladema.provincialreports.programreports.repository.SumarGeneralConsultationDetail;
import net.pladema.provincialreports.programreports.repository.OdontologicalConsultationDetail;
import net.pladema.provincialreports.programreports.service.ProgramReportExcelService;
import net.pladema.provincialreports.reportformat.DateFormat;

import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ProgramReportExcelServiceImpl implements ProgramReportExcelService {

	private ICellStyle basicStyle;
	private ICellStyle titleStyle;
	private ICellStyle fieldStyle;
	private ICellStyle subTitleStyle;

	private final DateFormat reformatdate;

	public ProgramReportExcelServiceImpl(DateFormat reformatdate) {
		this.reformatdate = reformatdate;
	}

	@Override
	public IWorkbook buildExcelEpidemiologyOne(String title, String[] headers, List<EpidemiologyOneConsultationDetail> result) {
		IWorkbook wb = WorkbookCreator.createExcelWorkbook();
		createCellStyle(wb);

		ISheet sheet = wb.createSheet(title);

		fillRow(sheet, getHeaderData(headers, title));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());

		ICellStyle styleDataRow = createDataRowStyle(wb);

		result.forEach(
				resultData -> {
					IRow newDataRow = sheet.createRow(rowNumber.getAndIncrement());
					fillRowContent(newDataRow, resultData, styleDataRow);
				}
		);

		setDimensions(sheet);
		return wb;
	}

	@Override
	public IWorkbook buildExcelEpidemiologyTwo(String title, String[] headers, List<EpidemiologyTwoConsultationDetail> result) {
		IWorkbook wb = WorkbookCreator.createExcelWorkbook();
		createCellStyle(wb);

		ISheet sheet = wb.createSheet(title);

		fillRow(sheet, getHeaderData(headers, title));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());

		ICellStyle styleDataRow = createDataRowStyle(wb);

		result.forEach(
				resultData -> {
					IRow newDataRow = sheet.createRow(rowNumber.getAndIncrement());
					fillRowContent(newDataRow, resultData, styleDataRow);
				}
		);

		setDimensions(sheet);
		return wb;
	}

	@Override
	public IWorkbook buildExcelRecuperoGeneral(String title, String[] headers, List<RecuperoGeneralConsultationDetail> result) {
		IWorkbook wb = WorkbookCreator.createExcelWorkbook();
		createCellStyle(wb);

		ISheet sheet = wb.createSheet(title);

		fillRow(sheet, getHeaderData(headers, title));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());

		ICellStyle styleDataRow = createDataRowStyle(wb);

		result.forEach(
				resultData -> {
					IRow newDataRow = sheet.createRow(rowNumber.getAndIncrement());
					fillRowContent(newDataRow, resultData, styleDataRow);
				}
		);

		setDimensions(sheet);
		return wb;
	}

	@Override
	public IWorkbook buildExcelSumarGeneral(String title, String[] headers, List<SumarGeneralConsultationDetail> result) {
		IWorkbook wb = WorkbookCreator.createExcelWorkbook();
		createCellStyle(wb);

		for (SumarGeneralConsultationDetail imc : result) {
			Double peso = imc.getWeight() != null ? Double.valueOf(imc.getWeight()) : null;
			Double altura = imc.getHeight() != null ? Double.valueOf(imc.getHeight()) : null;
			Double imcResult;
			if (peso != null && altura != null) {
				imcResult = peso / (altura * altura) ;
				DecimalFormat df = new DecimalFormat("##.##");
				String imcFormat = df.format(imcResult * 10000);
				SumarGeneralConsultationDetail consultationDetailRecuperoSumar = new SumarGeneralConsultationDetail(imcFormat);
				imc.setBmi(consultationDetailRecuperoSumar.getBmi());
			}
		}

		ISheet sheet = wb.createSheet(title);

		fillRow(sheet,getHeaderData(headers, title));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());

		ICellStyle styleDataRow = createDataRowStyle(wb);

		result.forEach(
				resultData -> {
					IRow newDataRow = sheet.createRow(rowNumber.getAndIncrement());
					fillRowContent(newDataRow, resultData, styleDataRow);
				}
		);

		setDimensions(sheet);
		return wb;
	}

	@Override
	public IWorkbook buildExcelOdontological(String title, String[] headers, List<OdontologicalConsultationDetail> result) {
		IWorkbook wb = WorkbookCreator.createExcelWorkbook();
		createCellStyle(wb);

		ISheet sheet = wb.createSheet(title);

		fillRow(sheet,getHeaderData(headers, title));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());

		ICellStyle styleDataRow = createDataRowStyle(wb);

		result.forEach(
				resultData -> {
					IRow newDataRow = sheet.createRow(rowNumber.getAndIncrement());
					fillRowContent(newDataRow, resultData, styleDataRow);
				}
		);

		setDimensions(sheet);
		return wb;
	}

	private void createCellStyle(IWorkbook workbook){
		basicStyle = workbook.createStyle();
		basicStyle.setFontSize((short)12);
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

	private List<CellContent> getHeaderData(String[] subtitles, String title){
		List<CellContent> data = new ArrayList<>();

		int nRow = 0;

		data.add(new CellContent(nRow, 0, 1, 2, "", basicStyle));
		data.add(new CellContent(nRow, 2, 2, 1, "2", titleStyle));
		data.add(new CellContent(nRow, 3, 2, 16, title, titleStyle));
		data.add(new CellContent(nRow, 19, 1, 3, "1. Hoja N°", fieldStyle));
		data.add(new CellContent(nRow, 22, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 23, 1, 1, "", basicStyle));

		nRow++;
		data.add(new CellContent(nRow, 0, 1, 2, "", basicStyle));
		data.add(new CellContent(nRow, 19, 1, 5, "", basicStyle));

		nRow++;
		data.add(new CellContent(nRow, 0, 1, 2, "2. ESTABLECIMIENTO", fieldStyle));
		data.add(new CellContent(nRow, 2, 1, 14, "", basicStyle));
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

	private void setDimensions(ISheet sheet){
		sheet.autoSizeColumns();
		int nRow = 0;
		sheet.setRowHeight(nRow++, 50);
		sheet.setRowHeight(nRow++, 40);
		sheet.setRowHeight(nRow++, 35);

		while (nRow < sheet.getCantRows())
			sheet.setRowHeight(nRow++, 21);
	}

	private ICellStyle createDataRowStyle(IWorkbook workbook){
		ICellStyle cellStyle = workbook.createStyle();
		cellStyle.setFontSize((short) 12);
		cellStyle.setBorders(true);
		return cellStyle;
	}

	private void fillRow(ISheet sheet, List<CellContent> data){
		Map<Integer, List<CellContent>> cellByRow = data.stream().collect(Collectors.groupingBy(CellContent::getRow));

		for (Map.Entry<Integer, List<CellContent>> entry : cellByRow.entrySet()){
			int nRow = entry.getKey();
			IRow row = sheet.createRow(nRow);
			entry.getValue().forEach(cell -> createCell(sheet, row, nRow, cell));
		}
	}

	private void createCell(ISheet sheet, IRow row, int nRow, CellContent data){

		int nColumn = data.getColumn();

		ICell cell = row.createCell(data.getColumn());
		cell.setCellStyle(data.getStyle());
		cell.setCellValue(data);

		if (data.isCellRange()) {
			sheet.addMergedRegion(nRow, data.lastRow(), nColumn, data.lastCol(), true);

		}
	}

	private void fillRowContent(IRow row, EpidemiologyOneConsultationDetail content, ICellStyle style) {
		AtomicInteger rowNumber = new AtomicInteger(0);

		ICell cell = row.createCell(rowNumber.getAndIncrement());
		cell.setCellValue(content.getPatientFullName());
		cell.setCellStyle(style);

		ICell cell2 = row.createCell(rowNumber.getAndIncrement());
		cell2.setCellValue(content.getCoding());
		cell2.setCellStyle(style);

		ICell cell3 = row.createCell(rowNumber.getAndIncrement());
		cell3.setCellValue(content.getBirthDate());
		cell3.setCellStyle(style);

		ICell cell4 = row.createCell(rowNumber.getAndIncrement());
		cell4.setCellValue(content.getGender());
		cell4.setCellStyle(style);

		ICell cell5 = row.createCell(rowNumber.getAndIncrement());
		cell5.setCellValue(content.getStartDate());
		cell5.setCellStyle(style);

		ICell cell6 = row.createCell(rowNumber.getAndIncrement());
		cell6.setCellValue(content.getDepartment());
		cell6.setCellStyle(style);

		ICell cell7 = row.createCell(rowNumber.getAndIncrement());
		cell7.setCellValue(content.getAddress());
		cell7.setCellStyle(style);

		ICell cell8 = row.createCell(rowNumber.getAndIncrement());
		cell8.setCellValue(content.getCie10Codes());
		cell8.setCellStyle(style);

		ICell cell9 = row.createCell(rowNumber.getAndIncrement());
		cell9.setCellValue(content.getIdentificationNumber());
		cell9.setCellStyle(style);

		ICell cell10 = row.createCell(rowNumber.getAndIncrement());
		cell10.setCellValue(content.getProblems());
		cell10.setCellStyle(style);

	}

	private void fillRowContent(IRow row, EpidemiologyTwoConsultationDetail content, ICellStyle style) {
		AtomicInteger rowNumber = new AtomicInteger(0);

		ICell cell = row.createCell(rowNumber.getAndIncrement());
		cell.setCellValue(content.getDiagnostic());
		cell.setCellStyle(style);

		ICell cell2 = row.createCell(rowNumber.getAndIncrement());
		cell2.setCellValue(content.getGrp());
		cell2.setCellStyle(style);

		ICell cell3 = row.createCell(rowNumber.getAndIncrement());
		cell3.setCellValue(content.getCounter());
		cell3.setCellStyle(style);

	}

	private void fillRowContent(IRow row, RecuperoGeneralConsultationDetail content, ICellStyle style) {
		AtomicInteger rowNumber = new AtomicInteger(0);

		ICell cell = row.createCell(rowNumber.getAndIncrement());
		cell.setCellValue(content.getInstitution());
		cell.setCellStyle(style);

		ICell cell1 = row.createCell(rowNumber.getAndIncrement());
		cell1.setCellValue(content.getOperativeUnit());
		cell1.setCellStyle(style);

		ICell cell2 = row.createCell(rowNumber.getAndIncrement());
		cell2.setCellValue(content.getLender());
		cell2.setCellStyle(style);

		ICell cell3 = row.createCell(rowNumber.getAndIncrement());
		cell3.setCellValue(content.getLenderDni());
		cell3.setCellStyle(style);

		ICell cell4 = row.createCell(rowNumber.getAndIncrement());
		cell4.setCellValue(reformatdate.reformatDateThree(content.getAttentionDate()));
		cell4.setCellStyle(style);

		ICell cell5 = row.createCell(rowNumber.getAndIncrement());
		cell5.setCellValue(content.getHour());
		cell5.setCellStyle(style);

		ICell cell6 = row.createCell(rowNumber.getAndIncrement());
		cell6.setCellValue(content.getConsultationNumber());
		cell6.setCellStyle(style);

		ICell cell7 = row.createCell(rowNumber.getAndIncrement());
		cell7.setCellValue(content.getPatientDni());
		cell7.setCellStyle(style);

		ICell cell8 = row.createCell(rowNumber.getAndIncrement());
		cell8.setCellValue(content.getPatientName());
		cell8.setCellStyle(style);

		ICell cell9 = row.createCell(rowNumber.getAndIncrement());
		cell9.setCellValue(content.getGender());
		cell9.setCellStyle(style);

		ICell cell10 = row.createCell(rowNumber.getAndIncrement());
		cell10.setCellValue(reformatdate.reformatDateFive(content.getBirthDate()));
		cell10.setCellStyle(style);

		ICell cell11 = row.createCell(rowNumber.getAndIncrement());
		cell11.setCellValue(content.getAgeTurn());
		cell11.setCellStyle(style);

		ICell cell12 = row.createCell(rowNumber.getAndIncrement());
		cell12.setCellValue(content.getAgeToday());
		cell12.setCellStyle(style);

		ICell cell13 = row.createCell(rowNumber.getAndIncrement());
		cell13.setCellValue(content.getMedicalCoverage());
		cell13.setCellStyle(style);

		ICell cell14 = row.createCell(rowNumber.getAndIncrement());
		cell14.setCellValue(content.getAddress());
		cell14.setCellStyle(style);

		ICell cell15 = row.createCell(rowNumber.getAndIncrement());
		cell15.setCellValue(content.getLocation());
		cell15.setCellStyle(style);

		ICell cell17 = row.createCell(rowNumber.getAndIncrement());
		cell17.setCellValue(content.getReasons());
		cell17.setCellStyle(style);

		ICell cell18 = row.createCell(rowNumber.getAndIncrement());
		cell18.setCellValue(content.getProcedures());
		cell18.setCellStyle(style);

		ICell cell19 = row.createCell(rowNumber.getAndIncrement());
		cell19.setCellValue(content.getProblems());
		cell19.setCellStyle(style);

		ICell cell20 = row.createCell(rowNumber.getAndIncrement());
		cell20.setCellValue(content.getMedication());
		cell20.setCellStyle(style);

		ICell cell21 = row.createCell(rowNumber.getAndIncrement());
		cell21.setCellValue(content.getEvolution());
		cell21.setCellStyle(style);

	}

	private void fillRowContent(IRow row, SumarGeneralConsultationDetail content, ICellStyle style) {
		AtomicInteger rowNumber = new AtomicInteger(0);

		ICell cell = row.createCell(rowNumber.getAndIncrement());
		cell.setCellValue(content.getInstitution());
		cell.setCellStyle(style);

		ICell cell1 = row.createCell(rowNumber.getAndIncrement());
		cell1.setCellValue(content.getOperativeUnit());
		cell1.setCellStyle(style);

		ICell cell2 = row.createCell(rowNumber.getAndIncrement());
		cell2.setCellValue(content.getLender());
		cell2.setCellStyle(style);

		ICell cell3 = row.createCell(rowNumber.getAndIncrement());
		cell3.setCellValue(content.getLenderDni());
		cell3.setCellStyle(style);

		ICell cell4 = row.createCell(rowNumber.getAndIncrement());
		cell4.setCellValue(content.getAttentionDate());
		cell4.setCellStyle(style);

		ICell cell5 = row.createCell(rowNumber.getAndIncrement());
		cell5.setCellValue(content.getHour());
		cell5.setCellStyle(style);

		ICell cell6 = row.createCell(rowNumber.getAndIncrement());
		cell6.setCellValue(content.getConsultationNumber());
		cell6.setCellStyle(style);

		ICell cell7 = row.createCell(rowNumber.getAndIncrement());
		cell7.setCellValue(content.getPatientDni());
		cell7.setCellStyle(style);

		ICell cell8 = row.createCell(rowNumber.getAndIncrement());
		cell8.setCellValue(content.getPatientName());
		cell8.setCellStyle(style);

		ICell cell9 = row.createCell(rowNumber.getAndIncrement());
		cell9.setCellValue(content.getGender());
		cell9.setCellStyle(style);

		ICell cell10 = row.createCell(rowNumber.getAndIncrement());
		cell10.setCellValue(content.getSelfPerceivedGender());
		cell10.setCellStyle(style);

		ICell cell11 = row.createCell(rowNumber.getAndIncrement());
		cell11.setCellValue(content.getSelfPerceivedName());
		cell11.setCellStyle(style);

		ICell cell12 = row.createCell(rowNumber.getAndIncrement());
		cell12.setCellValue(content.getBirthDate());
		cell12.setCellStyle(style);

		ICell cell13 = row.createCell(rowNumber.getAndIncrement());
		cell13.setCellValue(content.getAgeTurn());
		cell13.setCellStyle(style);

		ICell cell14 = row.createCell(rowNumber.getAndIncrement());
		cell14.setCellValue(content.getAgeToday());
		cell14.setCellStyle(style);

		ICell cell15 = row.createCell(rowNumber.getAndIncrement());
		cell15.setCellValue(content.getEthnicity());
		cell15.setCellStyle(style);

		ICell cell16 = row.createCell(rowNumber.getAndIncrement());
		cell16.setCellValue(content.getMedicalCoverage());
		cell16.setCellStyle(style);

		ICell cell17 = row.createCell(rowNumber.getAndIncrement());
		cell17.setCellValue(content.getDirection());
		cell17.setCellStyle(style);

		ICell cell18 = row.createCell(rowNumber.getAndIncrement());
		cell18.setCellValue(content.getLocation());
		cell18.setCellStyle(style);

		ICell cell19 = row.createCell(rowNumber.getAndIncrement());
		cell19.setCellValue(content.getEducationLevel());
		cell19.setCellStyle(style);

		ICell cell20 = row.createCell(rowNumber.getAndIncrement());
		cell20.setCellValue(content.getOccupation());
		cell20.setCellStyle(style);

		ICell cell21 = row.createCell(rowNumber.getAndIncrement());
		cell21.setCellValue(content.getSystolicBloodPressure());
		cell21.setCellStyle(style);

		ICell cell22 = row.createCell(rowNumber.getAndIncrement());
		cell22.setCellValue(content.getDiastolicBloodPressure());
		cell22.setCellStyle(style);

		ICell cell23 = row.createCell(rowNumber.getAndIncrement());
		cell23.setCellValue(content.getMeanArterialPressure());
		cell23.setCellStyle(style);

		ICell cell24 = row.createCell(rowNumber.getAndIncrement());
		cell24.setCellValue(content.getTemperature());
		cell24.setCellStyle(style);

		ICell cell25 = row.createCell(rowNumber.getAndIncrement());
		cell25.setCellValue(content.getHeartRate());
		cell25.setCellStyle(style);

		ICell cell26 = row.createCell(rowNumber.getAndIncrement());
		cell26.setCellValue(content.getRespirationRate());
		cell26.setCellStyle(style);

		ICell cell27 = row.createCell(rowNumber.getAndIncrement());
		cell27.setCellValue(content.getOxygenSaturationHemoglobin());
		cell27.setCellStyle(style);

		ICell cell28 = row.createCell(rowNumber.getAndIncrement());
		cell28.setCellValue(content.getHeight());
		cell28.setCellStyle(style);

		ICell cell29 = row.createCell(rowNumber.getAndIncrement());
		cell29.setCellValue(content.getWeight());
		cell29.setCellStyle(style);

		ICell cell30 = row.createCell(rowNumber.getAndIncrement());
		cell30.setCellValue(content.getBmi());
		cell30.setCellStyle(style);

		ICell cell31 = row.createCell(rowNumber.getAndIncrement());
		cell31.setCellValue(content.getHeadCircunference());
		cell31.setCellStyle(style);

		ICell cell32 = row.createCell(rowNumber.getAndIncrement());
		cell32.setCellValue(content.getReasons());
		cell32.setCellStyle(style);

		ICell cell33 = row.createCell(rowNumber.getAndIncrement());
		cell33.setCellValue(content.getProcedures());
		cell33.setCellStyle(style);

		ICell cell34 = row.createCell(rowNumber.getAndIncrement());
		cell34.setCellValue(content.getProblems());
		cell34.setCellStyle(style);

		ICell cell35 = row.createCell(rowNumber.getAndIncrement());
		cell35.setCellValue(content.getMedication());
		cell35.setCellStyle(style);

		ICell cell36 = row.createCell(rowNumber.getAndIncrement());
		cell36.setCellValue(content.getEvolution());
		cell36.setCellStyle(style);

	}

	private void fillRowContent(IRow row, OdontologicalConsultationDetail content, ICellStyle style) {
		AtomicInteger rowNumber = new AtomicInteger(0);

		ICell cell = row.createCell(rowNumber.getAndIncrement());
		cell.setCellValue(content.getInstitution());
		cell.setCellStyle(style);

		ICell cell1 = row.createCell(rowNumber.getAndIncrement());
		cell1.setCellValue(content.getOperativeUnit());
		cell1.setCellStyle(style);

		ICell cell2 = row.createCell(rowNumber.getAndIncrement());
		cell2.setCellValue(content.getLender());
		cell2.setCellStyle(style);

		ICell cell3 = row.createCell(rowNumber.getAndIncrement());
		cell3.setCellValue(content.getLenderDni());
		cell3.setCellStyle(style);

		ICell cell4 = row.createCell(rowNumber.getAndIncrement());
		cell4.setCellValue(reformatdate.reformatDateThree(content.getAttentionDate()));
		cell4.setCellStyle(style);

		ICell cell5 = row.createCell(rowNumber.getAndIncrement());
		cell5.setCellValue(content.getAttentionHour());
		cell5.setCellStyle(style);

		ICell cell6 = row.createCell(rowNumber.getAndIncrement());
		cell6.setCellValue(content.getPatientDni());
		cell6.setCellStyle(style);

		ICell cell7 = row.createCell(rowNumber.getAndIncrement());
		cell7.setCellValue(content.getPatientName());
		cell7.setCellStyle(style);

		ICell cell8 = row.createCell(rowNumber.getAndIncrement());
		cell8.setCellValue(content.getGender());
		cell8.setCellStyle(style);

		ICell cell9 = row.createCell(rowNumber.getAndIncrement());
		cell9.setCellValue(reformatdate.reformatDateFive(content.getBirthDate()));
		cell9.setCellStyle(style);

		ICell cell10 = row.createCell(rowNumber.getAndIncrement());
		cell10.setCellValue(content.getAgeTurn());
		cell10.setCellStyle(style);

		ICell cell11 = row.createCell(rowNumber.getAndIncrement());
		cell11.setCellValue(content.getMedicalCoverage());
		cell11.setCellStyle(style);

		ICell cell12 = row.createCell(rowNumber.getAndIncrement());
		cell12.setCellValue(content.getDirection());
		cell12.setCellStyle(style);

		ICell cell13 = row.createCell(rowNumber.getAndIncrement());
		cell13.setCellValue(content.getLocation());
		cell13.setCellStyle(style);

		ICell cell14 = row.createCell(rowNumber.getAndIncrement());
		cell14.setCellValue(content.getIndexCpo());
		cell14.setCellStyle(style);

		ICell cell15 = row.createCell(rowNumber.getAndIncrement());
		cell15.setCellValue(content.getIndexCeo());
		cell15.setCellStyle(style);

		ICell cell16 = row.createCell(rowNumber.getAndIncrement());
		cell16.setCellValue(content.getReasons());
		cell16.setCellStyle(style);

		ICell cell17 = row.createCell(rowNumber.getAndIncrement());
		cell17.setCellValue(content.getProcedures());
		cell17.setCellStyle(style);

		ICell cell18 = row.createCell(rowNumber.getAndIncrement());
		cell18.setCellValue(content.getDentalProcedures());
		cell18.setCellStyle(style);

		ICell cell19 = row.createCell(rowNumber.getAndIncrement());
		cell19.setCellValue(content.getProblems());
		cell19.setCellStyle(style);

		ICell cell20 = row.createCell(rowNumber.getAndIncrement());
		cell20.setCellValue(content.getDentistryDiagnostics());
		cell20.setCellStyle(style);

	}

}
