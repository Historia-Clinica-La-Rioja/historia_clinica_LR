package net.pladema.provincialreports.nursingreports.service.impl;

import ar.lamansys.sgx.shared.reports.util.CellContent;
import ar.lamansys.sgx.shared.reports.util.manager.WorkbookCreator;
import ar.lamansys.sgx.shared.reports.util.struct.ICell;
import ar.lamansys.sgx.shared.reports.util.struct.ICellStyle;
import ar.lamansys.sgx.shared.reports.util.struct.IRow;
import ar.lamansys.sgx.shared.reports.util.struct.ISheet;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.provincialreports.nursingreports.repository.NursingEmergencyConsultationDetail;
import net.pladema.provincialreports.nursingreports.repository.NursingHospitalizationConsultationDetail;
import net.pladema.provincialreports.nursingreports.repository.NursingOutpatientConsultationDetail;
import net.pladema.provincialreports.nursingreports.repository.NursingProceduresConsultationDetail;
import net.pladema.provincialreports.nursingreports.repository.NursingVaccineConsultationDetail;
import net.pladema.provincialreports.nursingreports.service.NursingReportExcelService;
import net.pladema.provincialreports.reportformat.DateFormat;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class NursingReportExcelServiceImpl implements NursingReportExcelService {

	private ICellStyle basicStyle;
	private ICellStyle titleStyle;
	private ICellStyle fieldStyle;
	private ICellStyle subTitleStyle;
	private final DateFormat reformatdate;

	public NursingReportExcelServiceImpl(DateFormat reformatdate) {
		this.reformatdate = reformatdate;
	}


	@Override
	public IWorkbook buildExcelNursingEmergency(String title, String[] headers, List<NursingEmergencyConsultationDetail> result) {
		IWorkbook wb = WorkbookCreator.createExcelWorkbook();
		createCellStyle(wb);

		ISheet sheet = wb.createSheet(title);

		fillRow(sheet, getHeaderData(headers, title));

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
	public IWorkbook buildExcelNursingOutpatient(String title, String[] headers, List<NursingOutpatientConsultationDetail> result) {
		IWorkbook wb = WorkbookCreator.createExcelWorkbook();
		createCellStyle(wb);

		ISheet sheet = wb.createSheet(title);

		fillRow(sheet, getHeaderData(headers, title));

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
	public IWorkbook buildExcelNursingHospitalization(String title, String[] headers, List<NursingHospitalizationConsultationDetail> result) {
		IWorkbook wb = WorkbookCreator.createExcelWorkbook();
		createCellStyle(wb);

		ISheet sheet = wb.createSheet(title);

		fillRow(sheet, getHeaderData(headers, title));

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
	public IWorkbook buildExcelNursingProcedures(String title, String[] headers, List<NursingProceduresConsultationDetail> result) {
		IWorkbook wb = WorkbookCreator.createExcelWorkbook();
		createCellStyle(wb);

		ISheet sheet = wb.createSheet(title);

		fillRow(sheet, getHeaderData(headers, title));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());

		ICellStyle styleDatarow = createDataRowStyle(wb);

		result.forEach(resultData -> {
			IRow newDataRow = sheet.createRow(rowNumber.getAndIncrement());
			fillRowContent(newDataRow, resultData, styleDatarow);
		});

		setDimensions(sheet);
		return wb;
	}

	@Override
	public IWorkbook buildExcelNursingVaccine(String title, String[] headers, List<NursingVaccineConsultationDetail> result, String startDate, String endDate, String institutionName) {
		IWorkbook wb = WorkbookCreator.createExcelWorkbook();
		createCellStyle(wb);

		ISheet sheet = wb.createSheet(title);

		fillRow(sheet, getHeaderData(headers, title, startDate, endDate, institutionName));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());

		ICellStyle styleDatarow = createDataRowStyle(wb);

		result.forEach(resultData -> {
			IRow newDataRow = sheet.createRow(rowNumber.getAndIncrement());
			fillRowContent(newDataRow, resultData, styleDatarow);
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


	private List<CellContent> getHeaderData(String[] subtitles, String title) {
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

	private List<CellContent> getHeaderData(String[] subtitles, String title, String startDate, String endDate, String institutionName) {
		List<CellContent> data = new ArrayList<>();

		int nRow = 0;

		data.add(new CellContent(nRow, 0, 1, 2, "", basicStyle));
		data.add(new CellContent(nRow, 2, 2, 1, "2", titleStyle));
		data.add(new CellContent(nRow, 3, 2, 16, title, titleStyle));
		data.add(new CellContent(nRow, 19, 1, 3, "1. Hoja N°", fieldStyle));
		data.add(new CellContent(nRow, 22, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 23, 1, 1, "", basicStyle));

		nRow++;
		data.add(new CellContent(nRow, 0, 1, 2, "PERIODO: " + startDate + " hasta " + endDate, basicStyle));
		data.add(new CellContent(nRow, 19, 1, 5, "", basicStyle));

		nRow++;
		data.add(new CellContent(nRow, 0, 1, 2, "2. ESTABLECIMIENTO: " + institutionName, fieldStyle));
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

	private void fillRowContent(IRow row, NursingEmergencyConsultationDetail content, ICellStyle style) {
		AtomicInteger rowNumber = new AtomicInteger(0);

		ICell cell = row.createCell(rowNumber.getAndIncrement());
		cell.setCellValue(content.getInstitution());
		cell.setCellStyle(style);

		ICell cell1 = row.createCell(rowNumber.getAndIncrement());
		cell1.setCellValue(content.getAmbulance());
		cell1.setCellStyle(style);

		ICell cell2 = row.createCell(rowNumber.getAndIncrement());
		cell2.setCellValue(content.getOffice());
		cell2.setCellStyle(style);

		ICell cell3 = row.createCell(rowNumber.getAndIncrement());
		cell3.setCellValue(content.getSector());
		cell3.setCellStyle(style);

		ICell cell4 = row.createCell(rowNumber.getAndIncrement());
		cell4.setCellValue(content.getPoliceIntervention());
		cell4.setCellStyle(style);

		ICell cell5 = row.createCell(rowNumber.getAndIncrement());
		cell5.setCellValue(content.getAttentionDate());
		cell5.setCellStyle(style);

		ICell cell6 = row.createCell(rowNumber.getAndIncrement());
		cell6.setCellValue(content.getAttentionHour());
		cell6.setCellStyle(style);

		ICell cell7 = row.createCell(rowNumber.getAndIncrement());
		cell7.setCellValue(content.getProfessionalRegistering());
		cell7.setCellStyle(style);

		ICell cell8 = row.createCell(rowNumber.getAndIncrement());
		cell8.setCellValue(content.getProfessionalAttention());
		cell8.setCellStyle(style);

		ICell cell9 = row.createCell(rowNumber.getAndIncrement());
		cell9.setCellValue(content.getIdentification());
		cell9.setCellStyle(style);

		ICell cell10 = row.createCell(rowNumber.getAndIncrement());
		cell10.setCellValue(content.getLastName());
		cell10.setCellStyle(style);

		ICell cell11 = row.createCell(rowNumber.getAndIncrement());
		cell11.setCellValue(content.getNames());
		cell11.setCellStyle(style);

		ICell cell12 = row.createCell(rowNumber.getAndIncrement());
		cell12.setCellValue(content.getGender());
		cell12.setCellStyle(style);

		ICell cell13 = row.createCell(rowNumber.getAndIncrement());
		cell13.setCellValue(content.getSelfPerceivedGender());
		cell13.setCellStyle(style);

		ICell cell14 = row.createCell(rowNumber.getAndIncrement());
		cell14.setCellValue(content.getSelfPerceivedName());
		cell14.setCellStyle(style);

		ICell cell15 = row.createCell(rowNumber.getAndIncrement());
		cell15.setCellValue(content.getBirthDate());
		cell15.setCellStyle(style);

		ICell cell16 = row.createCell(rowNumber.getAndIncrement());
		cell16.setCellValue(content.getAgeTurn());
		cell16.setCellStyle(style);

		ICell cell17 = row.createCell(rowNumber.getAndIncrement());
		cell17.setCellValue(content.getAgeToday());
		cell17.setCellStyle(style);

		ICell cell18 = row.createCell(rowNumber.getAndIncrement());
		cell18.setCellValue(content.getEthnicity());
		cell18.setCellStyle(style);

		ICell cell19 = row.createCell(rowNumber.getAndIncrement());
		cell19.setCellValue(content.getAddressPatient());
		cell19.setCellStyle(style);

		ICell cell20 = row.createCell(rowNumber.getAndIncrement());
		cell20.setCellValue(content.getLocationPatient());
		cell20.setCellStyle(style);

		ICell cell21 = row.createCell(rowNumber.getAndIncrement());
		cell21.setCellValue(content.getMedicalCoverage());
		cell21.setCellStyle(style);

		ICell cell22 = row.createCell(rowNumber.getAndIncrement());
		cell22.setCellValue(content.getEmergencyCareEntrance());
		cell22.setCellStyle(style);

		ICell cell23 = row.createCell(rowNumber.getAndIncrement());
		cell23.setCellValue(content.getStatePatient());
		cell23.setCellStyle(style);

		ICell cell24 = row.createCell(rowNumber.getAndIncrement());
		cell24.setCellValue(content.getAttentionType());
		cell24.setCellStyle(style);

		ICell cell25 = row.createCell(rowNumber.getAndIncrement());
		cell25.setCellValue(content.getTriageNote());
		cell25.setCellStyle(style);

		ICell cell26 = row.createCell(rowNumber.getAndIncrement());
		cell26.setCellValue(content.getTriageLevel());
		cell26.setCellStyle(style);

		ICell cell27 = row.createCell(rowNumber.getAndIncrement());
		cell27.setCellValue(content.getDateDischarge());
		cell27.setCellStyle(style);

		ICell cell28 = row.createCell(rowNumber.getAndIncrement());
		cell28.setCellValue(content.getAmbulanceDischarge());
		cell28.setCellStyle(style);

		ICell cell29 = row.createCell(rowNumber.getAndIncrement());
		cell29.setCellValue(content.getTypeDischarge());
		cell29.setCellStyle(style);

		ICell cell30 = row.createCell(rowNumber.getAndIncrement());
		cell30.setCellValue(content.getPatientExit());
		cell30.setCellStyle(style);

	}

	private void fillRowContent(IRow row, NursingOutpatientConsultationDetail content, ICellStyle style) {
		AtomicInteger rowNumber = new AtomicInteger(0);

		ICell cell = row.createCell(rowNumber.getAndIncrement());
		cell.setCellValue(content.getInstitution());
		cell.setCellStyle(style);

		ICell cell1 = row.createCell(rowNumber.getAndIncrement());
		cell1.setCellValue(content.getOperativeUnit());
		cell1.setCellStyle(style);

		ICell cell2 = row.createCell(rowNumber.getAndIncrement());
		cell2.setCellValue(content.getPatientProvider());
		cell2.setCellStyle(style);

		ICell cell3 = row.createCell(rowNumber.getAndIncrement());
		cell3.setCellValue(content.getProviderDni());
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
		cell10.setCellValue(content.getSelfPerceivedGender());
		cell10.setCellStyle(style);

		ICell cell11 = row.createCell(rowNumber.getAndIncrement());
		cell11.setCellValue(content.getSelfPerceivedName());
		cell11.setCellStyle(style);

		ICell cell12 = row.createCell(rowNumber.getAndIncrement());
		cell12.setCellValue(reformatdate.ReformatDateFive(content.getBirthDate()));
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
		cell17.setCellValue(content.getPatientAddress());
		cell17.setCellStyle(style);

		ICell cell18 = row.createCell(rowNumber.getAndIncrement());
		cell18.setCellValue(content.getPatientLocation());
		cell18.setCellStyle(style);

		ICell cell19 = row.createCell(rowNumber.getAndIncrement());
		cell19.setCellValue(content.getEducationLevel());
		cell19.setCellStyle(style);

		ICell cell20 = row.createCell(rowNumber.getAndIncrement());
		cell20.setCellValue(content.getOccupation());
		cell20.setCellStyle(style);

		ICell cell21 = row.createCell(rowNumber.getAndIncrement());
		cell21.setCellValue(content.getSystolicPressure());
		cell21.setCellStyle(style);

		ICell cell22 = row.createCell(rowNumber.getAndIncrement());
		cell22.setCellValue(content.getDiastolicPressure());
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
		cell26.setCellValue(content.getRespiratoryRate());
		cell26.setCellStyle(style);

		ICell cell27 = row.createCell(rowNumber.getAndIncrement());
		cell27.setCellValue(content.getArterialOxygenSaturation());
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
		cell31.setCellValue(content.getProcedures());
		cell31.setCellStyle(style);

		ICell cell32 = row.createCell(rowNumber.getAndIncrement());
		cell32.setCellValue(content.getEvolution());
		cell32.setCellStyle(style);

	}

	private void fillRowContent(IRow row, NursingHospitalizationConsultationDetail content, ICellStyle style) {
		AtomicInteger rowNumber = new AtomicInteger(0);

		ICell cell = row.createCell(rowNumber.getAndIncrement());
		cell.setCellValue(content.getInstitution());
		cell.setCellStyle(style);

		ICell cell1 = row.createCell(rowNumber.getAndIncrement());
		cell1.setCellValue(content.getLastName());
		cell1.setCellStyle(style);

		ICell cell2 = row.createCell(rowNumber.getAndIncrement());
		cell2.setCellValue(content.getCompleteName());
		cell2.setCellStyle(style);

		ICell cell3 = row.createCell(rowNumber.getAndIncrement());
		cell3.setCellValue(content.getGender());
		cell3.setCellStyle(style);

		ICell cell4 = row.createCell(rowNumber.getAndIncrement());
		cell4.setCellValue(content.getIdentification());
		cell4.setCellStyle(style);

		ICell cell5 = row.createCell(rowNumber.getAndIncrement());
		cell5.setCellValue(content.getProfessional());
		cell5.setCellStyle(style);

		ICell cell6 = row.createCell(rowNumber.getAndIncrement());
		cell6.setCellValue(content.getLicenseNumber());
		cell6.setCellStyle(style);

		ICell cell7 = row.createCell(rowNumber.getAndIncrement());
		cell7.setCellValue(content.getEntryDate());
		cell7.setCellStyle(style);

		ICell cell8 = row.createCell(rowNumber.getAndIncrement());
		cell8.setCellValue(content.getProbableDischargeDate());
		cell8.setCellStyle(style);

		ICell cell9 = row.createCell(rowNumber.getAndIncrement());
		cell9.setCellValue(content.getBed());
		cell9.setCellStyle(style);

		ICell cell10 = row.createCell(rowNumber.getAndIncrement());
		cell10.setCellValue(content.getCategoryBed());
		cell10.setCellStyle(style);

		ICell cell11 = row.createCell(rowNumber.getAndIncrement());
		cell11.setCellValue(content.getRoomName());
		cell11.setCellStyle(style);

		ICell cell12 = row.createCell(rowNumber.getAndIncrement());
		cell12.setCellValue(content.getSector());
		cell12.setCellStyle(style);

		ICell cell13 = row.createCell(rowNumber.getAndIncrement());
		cell13.setCellValue(content.getDischargeDate());
		cell13.setCellStyle(style);

		ICell cell14 = row.createCell(rowNumber.getAndIncrement());
		cell14.setCellValue(content.getProcedures());
		cell14.setCellStyle(style);

		ICell cell15 = row.createCell(rowNumber.getAndIncrement());
		cell15.setCellValue(content.getVitalSign());
		cell15.setCellStyle(style);

	}

	private void fillRowContent(IRow row, NursingProceduresConsultationDetail content, ICellStyle style) {
		AtomicInteger rowNumber = new AtomicInteger(0);

		ICell cell = row.createCell(rowNumber.getAndIncrement());
		cell.setCellValue(content.getInstitution());
		cell.setCellStyle(style);

		ICell cell1 = row.createCell(rowNumber.getAndIncrement());
		cell1.setCellValue(content.getSource());
		cell1.setCellStyle(style);

		ICell cell2 = row.createCell(rowNumber.getAndIncrement());
		cell2.setCellValue(content.getLender());
		cell2.setCellStyle(style);

		ICell cell3 = row.createCell(rowNumber.getAndIncrement());
		cell3.setCellValue(content.getLenderDni());
		cell3.setCellStyle(style);

		ICell cell4 = row.createCell(rowNumber.getAndIncrement());
		cell4.setCellValue(reformatdate.reformatDateFour(content.getAttentionDate()));
		cell4.setCellStyle(style);

		ICell cell5 = row.createCell(rowNumber.getAndIncrement());
		cell5.setCellValue(content.getHour());
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
		cell9.setCellValue(content.getSelfPerceivedGener());
		cell9.setCellStyle(style);

		ICell cell10 = row.createCell(rowNumber.getAndIncrement());
		cell10.setCellValue(content.getSelfPerceiverName());
		cell10.setCellStyle(style);

		ICell cell11 = row.createCell(rowNumber.getAndIncrement());
		cell11.setCellValue(reformatdate.reformatDateThree(content.getBirthday()));
		cell11.setCellStyle(style);

		ICell cell12 = row.createCell(rowNumber.getAndIncrement());
		cell12.setCellValue(content.getAgeTurn());
		cell12.setCellStyle(style);

		ICell cell13 = row.createCell(rowNumber.getAndIncrement());
		cell13.setCellValue(content.getAgeToday());
		cell13.setCellStyle(style);

		ICell cell14 = row.createCell(rowNumber.getAndIncrement());
		cell14.setCellValue(content.getEthnicity());
		cell14.setCellStyle(style);

		ICell cell15 = row.createCell(rowNumber.getAndIncrement());
		cell15.setCellValue(content.getMedicalCoverage());
		cell15.setCellStyle(style);

		ICell cell16 = row.createCell(rowNumber.getAndIncrement());
		cell16.setCellValue(content.getAddress());
		cell16.setCellStyle(style);

		ICell cell17 = row.createCell(rowNumber.getAndIncrement());
		cell17.setCellValue(content.getLocation());
		cell17.setCellStyle(style);

		ICell cell18 = row.createCell(rowNumber.getAndIncrement());
		cell18.setCellValue(content.getEducationLevel());
		cell18.setCellStyle(style);

		ICell cell19 = row.createCell(rowNumber.getAndIncrement());
		cell19.setCellValue(content.getOccupation());
		cell19.setCellStyle(style);

		ICell cell20 = row.createCell(rowNumber.getAndIncrement());
		cell20.setCellValue(content.getSystolicBloodPressure());
		cell20.setCellStyle(style);

		ICell cell21 = row.createCell(rowNumber.getAndIncrement());
		cell21.setCellValue(content.getDiastolicBloodPressure());
		cell21.setCellStyle(style);

		ICell cell22 = row.createCell(rowNumber.getAndIncrement());
		cell22.setCellValue(content.getMeanArterialPressure());
		cell22.setCellStyle(style);

		ICell cell23 = row.createCell(rowNumber.getAndIncrement());
		cell23.setCellValue(content.getTemperature());
		cell23.setCellStyle(style);

		ICell cell24 = row.createCell(rowNumber.getAndIncrement());
		cell24.setCellValue(content.getHeartRate());
		cell24.setCellStyle(style);

		ICell cell25 = row.createCell(rowNumber.getAndIncrement());
		cell25.setCellValue(content.getRespirationRate());
		cell25.setCellStyle(style);

		ICell cell26 = row.createCell(rowNumber.getAndIncrement());
		cell26.setCellValue(content.getOxygenSaturationHemoglobin());
		cell26.setCellStyle(style);

		ICell cell27 = row.createCell(rowNumber.getAndIncrement());
		cell27.setCellValue(content.getHeight());
		cell27.setCellStyle(style);

		ICell cell28 = row.createCell(rowNumber.getAndIncrement());
		cell28.setCellValue(content.getWeight());
		cell28.setCellStyle(style);

		ICell cell29 = row.createCell(rowNumber.getAndIncrement());
		cell29.setCellValue(content.getBmi());
		cell29.setCellStyle(style);

		ICell cell30 = row.createCell(rowNumber.getAndIncrement());
		cell30.setCellValue(content.getProcedures());
		cell30.setCellStyle(style);

		ICell cell31 = row.createCell(rowNumber.getAndIncrement());
		cell31.setCellValue(content.getProblems());
		cell31.setCellStyle(style);

		ICell cell32 = row.createCell(rowNumber.getAndIncrement());
		cell32.setCellValue(content.getMedication());
		cell32.setCellStyle(style);

		ICell cell33 = row.createCell(rowNumber.getAndIncrement());
		cell33.setCellValue(content.getEvolution());
		cell33.setCellStyle(style);

	}

	private void fillRowContent(IRow row, NursingVaccineConsultationDetail content, ICellStyle style) {
		AtomicInteger rowNumber = new AtomicInteger(0);

		ICell cell1 = row.createCell(rowNumber.getAndIncrement());
		cell1.setCellValue(content.getOperativeUnit());
		cell1.setCellStyle(style);

		ICell cell2 = row.createCell(rowNumber.getAndIncrement());
		cell2.setCellValue(content.getProvider());
		cell2.setCellStyle(style);

		ICell cell3 = row.createCell(rowNumber.getAndIncrement());
		cell3.setCellValue(content.getProviderIdentificationNumber());
		cell3.setCellStyle(style);

		ICell cell4 = row.createCell(rowNumber.getAndIncrement());
		cell4.setCellValue(reformatdate.reformatDateThree(content.getAttentionDate()));
		cell4.setCellStyle(style);

		ICell cell5 = row.createCell(rowNumber.getAndIncrement());
		cell5.setCellValue(content.getPatientIdentificationNumber());
		cell5.setCellStyle(style);

		ICell cell6 = row.createCell(rowNumber.getAndIncrement());
		cell6.setCellValue(content.getPatientName());
		cell6.setCellStyle(style);

		ICell cell7 = row.createCell(rowNumber.getAndIncrement());
		cell7.setCellValue(content.getPatientGender());
		cell7.setCellStyle(style);

		ICell cell8 = row.createCell(rowNumber.getAndIncrement());
		cell8.setCellValue(reformatdate.reformatDateThree(content.getPatientBirthDate()));
		cell8.setCellStyle(style);

		ICell cell9 = row.createCell(rowNumber.getAndIncrement());
		cell9.setCellValue(content.getAgeOnAppointmentDate());
		cell9.setCellStyle(style);

		ICell cell10 = row.createCell(rowNumber.getAndIncrement());
		cell10.setCellValue(content.getVaccine());
		cell10.setCellStyle(style);

		ICell cell11 = row.createCell(rowNumber.getAndIncrement());
		cell11.setCellValue(content.getSctid());
		cell11.setCellStyle(style);

		ICell cell12 = row.createCell(rowNumber.getAndIncrement());
		cell12.setCellValue(content.getCie10Code());
		cell12.setCellStyle(style);

		ICell cell13 = row.createCell(rowNumber.getAndIncrement());
		cell13.setCellValue(content.getStatus());
		cell13.setCellStyle(style);

		ICell cell14 = row.createCell(rowNumber.getAndIncrement());
		cell14.setCellValue(content.getCondition());
		cell14.setCellStyle(style);

		ICell cell15 = row.createCell(rowNumber.getAndIncrement());
		cell15.setCellValue(content.getVaccinationSchedule());
		cell15.setCellStyle(style);

		ICell cell16 = row.createCell(rowNumber.getAndIncrement());
		cell16.setCellValue(content.getVaccinationDosage());
		cell16.setCellStyle(style);

		ICell cell17 = row.createCell(rowNumber.getAndIncrement());
		cell17.setCellValue(content.getVaccinationLot());
		cell17.setCellStyle(style);

	}

}
