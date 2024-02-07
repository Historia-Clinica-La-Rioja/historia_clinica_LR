package net.pladema.provincialreports.odontologicalreports.service.impl;

import ar.lamansys.sgx.shared.reports.util.CellContent;
import ar.lamansys.sgx.shared.reports.util.manager.WorkbookCreator;
import ar.lamansys.sgx.shared.reports.util.struct.ICell;
import ar.lamansys.sgx.shared.reports.util.struct.ICellStyle;
import ar.lamansys.sgx.shared.reports.util.struct.IRow;
import ar.lamansys.sgx.shared.reports.util.struct.ISheet;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.establishment.repository.entity.Institution;
import net.pladema.provincialreports.odontologicalreports.repository.OdontologicalProceduresConsultationDetail;
import net.pladema.provincialreports.odontologicalreports.repository.OdontologyConsultationDetail;
import net.pladema.provincialreports.odontologicalreports.service.OdontologicalReportExcelService;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OdontologicalReportExcelServiceImpl implements OdontologicalReportExcelService {

	private ICellStyle basicStyle;
	private ICellStyle titleStyle;
	private ICellStyle fieldStyle;
	private ICellStyle subTitleStyle;
	private final InstitutionRepository institutionRepository;

    public OdontologicalReportExcelServiceImpl(InstitutionRepository institutionRepository) {
        this.institutionRepository = institutionRepository;
    }

    @Override
	public IWorkbook buildExcelOdontology(String title, String[] headers, List<OdontologyConsultationDetail> result, String startDate, String endDate, Integer institutionId) {
		IWorkbook wb = WorkbookCreator.createExcelWorkbook();
		createCellStyle(wb);

		Institution institution = institutionRepository.getById(institutionId);
		String institutionName = (institution!=null) ? institution.getName() : "";

		ISheet sheet = wb.createSheet(title);

		fillRow(sheet, getHeaderData(headers, title, startDate, endDate, institutionName));

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
	public IWorkbook buildExcelOdontologicalProcedures(String title, String[] headers, List<OdontologicalProceduresConsultationDetail> result, String startDate, String endDate, String institutionName) {
		IWorkbook wb = WorkbookCreator.createExcelWorkbook();
		createCellStyle(wb);

		ISheet sheet = wb.createSheet(title);

		fillRow(sheet, getHeaderData(headers, title, startDate, endDate, institutionName));

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

	private List<CellContent> getHeaderData(String[] subtitles, String title, String startDate, String endDate, String institutionName) {
		List<CellContent> data = new ArrayList<>();

		int nRow = 0;

		data.add(new CellContent(nRow, 0, 1, 2, "", basicStyle));
		data.add(new CellContent(nRow, 2, 2, 1, "1", titleStyle));
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
		data.add(new CellContent(nRow, 0, 1, 2, "5. Especialidad: Odontologia", fieldStyle));
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

		if (data.isCellRange())
			sheet.addMergedRegion(nRow, data.lastRow(), nColumn, data.lastCol(), true);
	}

	private void fillRowContent(IRow row, OdontologyConsultationDetail content, ICellStyle style) {
		AtomicInteger rowNumber = new AtomicInteger(0);

		ICell cell = row.createCell(rowNumber.getAndIncrement());
		cell.setCellValue(content.getProfessional());
		cell.setCellStyle(style);

		ICell cell2 = row.createCell(rowNumber.getAndIncrement());
		cell2.setCellValue(content.getProcedures());
		cell2.setCellStyle(style);

		ICell cell3 = row.createCell(rowNumber.getAndIncrement());
		cell3.setCellValue(content.getCounter());
		cell3.setCellStyle(style);

	}

	private void fillRowContent(IRow row, OdontologicalProceduresConsultationDetail content, ICellStyle style) {
		AtomicInteger rowNumber = new AtomicInteger(0);

		ICell cell2 = row.createCell(rowNumber.getAndIncrement());
		cell2.setCellValue(content.getProfessionalName());
		cell2.setCellStyle(style);

		ICell cell3 = row.createCell(rowNumber.getAndIncrement());
		cell3.setCellValue(content.getProfessionalIdentificationNumber());
		cell3.setCellStyle(style);

		ICell cell4 = row.createCell(rowNumber.getAndIncrement());
		cell4.setCellValue(content.getProfessionalLicenseNumber());
		cell4.setCellStyle(style);

		ICell cell5 = row.createCell(rowNumber.getAndIncrement());
		cell5.setCellValue(content.getAttentionDate());
		cell5.setCellStyle(style);

		ICell cell6 = row.createCell(rowNumber.getAndIncrement());
		cell6.setCellValue(content.getAttentionHour());
		cell6.setCellStyle(style);

		ICell cell7 = row.createCell(rowNumber.getAndIncrement());
		cell7.setCellValue(content.getPatientNames());
		cell7.setCellStyle(style);


		ICell cell8 = row.createCell(rowNumber.getAndIncrement());
		cell8.setCellValue(content.getPatientIdentificationNumber());
		cell8.setCellStyle(style);

		ICell cell9 = row.createCell(rowNumber.getAndIncrement());
		cell9.setCellValue(content.getPatientGender());
		cell9.setCellStyle(style);

		ICell cell10 = row.createCell(rowNumber.getAndIncrement());
		cell10.setCellValue(content.getPatientSelfPerceivedGender());
		cell10.setCellStyle(style);

		ICell cell11 = row.createCell(rowNumber.getAndIncrement());
		cell11.setCellValue(content.getPatientSelfPerceivedName());
		cell11.setCellStyle(style);

		ICell cell12 = row.createCell(rowNumber.getAndIncrement());
		cell12.setCellValue(content.getPatientBirthDate());
		cell12.setCellStyle(style);

		ICell cell13 = row.createCell(rowNumber.getAndIncrement());
		cell13.setCellValue(content.getPatientAgeTurn());
		cell13.setCellStyle(style);

		ICell cell14 = row.createCell(rowNumber.getAndIncrement());
		cell14.setCellValue(content.getPatientAgeToday());
		cell14.setCellStyle(style);

		ICell cell15 = row.createCell(rowNumber.getAndIncrement());
		cell15.setCellValue(content.getPatientMedicalCoverage());
		cell15.setCellStyle(style);

		ICell cell16 = row.createCell(rowNumber.getAndIncrement());
		cell16.setCellValue(content.getPatientAddress());
		cell16.setCellStyle(style);

		ICell cell17 = row.createCell(rowNumber.getAndIncrement());
		cell17.setCellValue(content.getPatientLocation());
		cell17.setCellStyle(style);

		ICell cell18 = row.createCell(rowNumber.getAndIncrement());
		cell18.setCellValue(content.getPatientPermanentCPO());
		cell18.setCellStyle(style);

		ICell cell19 = row.createCell(rowNumber.getAndIncrement());
		cell19.setCellValue(content.getPatientTemporaryCEO());
		cell19.setCellStyle(style);

		ICell cell20 = row.createCell(rowNumber.getAndIncrement());
		cell20.setCellValue(content.getReasons());
		cell20.setCellStyle(style);

		ICell cell21 = row.createCell(rowNumber.getAndIncrement());
		cell21.setCellValue(content.getOtherDiagnoses());
		cell21.setCellStyle(style);

		ICell cell22 = row.createCell(rowNumber.getAndIncrement());
		cell22.setCellValue(content.getOtherProcedures());
		cell22.setCellStyle(style);

		ICell cell23 = row.createCell(rowNumber.getAndIncrement());
		cell23.setCellValue(content.getAllergies());
		cell23.setCellStyle(style);

		ICell cell24 = row.createCell(rowNumber.getAndIncrement());
		cell24.setCellValue(content.getUsualMedication());
		cell24.setCellStyle(style);

		ICell cell25 = row.createCell(rowNumber.getAndIncrement());
		cell25.setCellValue(content.getDentalDiagnoses());
		cell25.setCellStyle(style);

		ICell cell26 = row.createCell(rowNumber.getAndIncrement());
		cell26.setCellValue(content.getDentalProcedures());
		cell26.setCellStyle(style);

		ICell cell27 = row.createCell(rowNumber.getAndIncrement());
		cell27.setCellValue(content.getEvolution());
		cell27.setCellStyle(style);

	}

}
