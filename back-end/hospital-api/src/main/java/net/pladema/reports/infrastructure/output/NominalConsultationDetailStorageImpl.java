package net.pladema.reports.infrastructure.output;

import ar.lamansys.sgx.shared.reports.util.CellContent;
import ar.lamansys.sgx.shared.reports.util.manager.WorkbookCreator;
import ar.lamansys.sgx.shared.reports.util.struct.ICell;
import ar.lamansys.sgx.shared.reports.util.struct.ICellStyle;
import ar.lamansys.sgx.shared.reports.util.struct.IRow;
import ar.lamansys.sgx.shared.reports.util.struct.ISheet;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import lombok.RequiredArgsConstructor;
import net.pladema.reports.application.ports.InstitutionReportStorage;
import net.pladema.reports.application.ports.NominalConsultationDetailStorage;
import net.pladema.reports.repository.ConsultationDetail;

import net.pladema.reports.repository.InstitutionInfo;
import net.pladema.reports.service.NominalDetailExcelService;

import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Service
public class NominalConsultationDetailStorageImpl implements NominalConsultationDetailStorage {

	private final NominalDetailExcelService nominalDetailExcelService;

	private final InstitutionReportStorage institutionReportStorage;

	@Override
	public IWorkbook buildNominalExternalConsultationDetailReport(String title, String[] headers, List<ConsultationDetail> result,
																  Integer institutionId) {

		InstitutionInfo institutionInfo = institutionReportStorage.getInstitutionInfo(institutionId);

		// creo el workbook de excel
		IWorkbook wb = WorkbookCreator.createExcelWorkbook();

		// creo la hoja con el titulo pasado como parametro
		ISheet sheet = wb.createSheet(title);

		// Header
		nominalDetailExcelService.fillRow(sheet, getHeaderData(headers, wb));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());

		ICellStyle styleDataRow = nominalDetailExcelService.createDataRowStyle(wb);

		//Fill data
		result.forEach(
				resultData -> {
					IRow newDataRow = sheet.createRow(rowNumber.getAndIncrement());
					fillRowContent(newDataRow, styleDataRow, resultData, institutionInfo);
				}
		);

		nominalDetailExcelService.setDimensions(sheet);
		return wb;
	}

	private List<CellContent> getHeaderData(String[] subtitles, IWorkbook wb) {
		List<CellContent> data = new ArrayList<>();

		var basicStyle = nominalDetailExcelService.getBasicStyle(wb);
		var titleStyle = nominalDetailExcelService.getTitleStyle(wb);
		var fieldStyle = nominalDetailExcelService.getFieldStyle(wb);
		var subTitleStyle = nominalDetailExcelService.getSubTitleStyle(wb);

		int nRow = 0;

		data.add(new CellContent(nRow, 0, 1, 2, "", basicStyle));
		data.add(new CellContent(nRow, 2, 2, 1, "2", titleStyle));
		data.add(new CellContent(nRow, 3, 2, 16,
				"Detalle nominal de consultorios externos", titleStyle));
		data.add(new CellContent(nRow, 19, 1, 3, "1. Hoja N°", fieldStyle));
		data.add(new CellContent(nRow, 22, 1, 12, "", basicStyle));

		nRow++;
		data.add(new CellContent(nRow, 0, 1, 2, "", basicStyle));
		data.add(new CellContent(nRow, 19, 1, 3, "", basicStyle));
		data.add(new CellContent(nRow, 22, 1, 12, "", basicStyle));
		nRow++;
		data.add(new CellContent(nRow, 0, 1, 2, "2. ESTABLECIMIENTO", fieldStyle));
		data.add(new CellContent(nRow, 2, 1, 14, "", basicStyle));
		data.add(new CellContent(nRow, 16, 1, 1, "3. MES", fieldStyle));
		data.add(new CellContent(nRow, 17, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 18, 1, 1, "4. AÑO", fieldStyle));
		data.add(new CellContent(nRow, 19, 1, 3, "", basicStyle));
		data.add(new CellContent(nRow, 22, 1, 12, "", basicStyle));
		nRow++;
		data.add(new CellContent(nRow, 0, 1, 2, "5. PARTIDO", fieldStyle));
		data.add(new CellContent(nRow, 2, 1, 8, "", basicStyle));
		data.add(new CellContent(nRow, 10, 1, 1, "6. REGIÓN SANITARIA", fieldStyle));
		data.add(new CellContent(nRow, 11, 1, 5, "", basicStyle));
		data.add(new CellContent(nRow, 16, 1, 3, "7. SERVICIO", fieldStyle));
		data.add(new CellContent(nRow, 19, 1, 3, "", basicStyle));
		data.add(new CellContent(nRow, 22, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 23, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 24, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 25, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 26, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 27, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 28, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 29, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 30, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 31, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 32, 1, 1, "", basicStyle));
		data.add(new CellContent(nRow, 33, 1, 1, "", basicStyle));

		nRow++;
		int column = 0;
		for(String subtitle : subtitles)
			data.add(new CellContent(nRow, column++, 1, 1, subtitle, subTitleStyle));

		return data;
	}

	private void fillRowContent(IRow row, ICellStyle style, ConsultationDetail content, InstitutionInfo institutionInfo){
		AtomicInteger rowNumber = new AtomicInteger(0);
		nominalDetailExcelService.fillNominalDetailCommonColumns(row, rowNumber, style, institutionInfo,
				content.getHierarchicalUnitTypeDescription(), content.getHierarchicalUnitAlias());
		ICell cell5 = row.createCell(rowNumber.getAndIncrement());
		cell5.setCellValue(content.getPatientSurname());
		cell5.setCellStyle(style);
		ICell cell6 = row.createCell(rowNumber.getAndIncrement());
		cell6.setCellValue(content.getPatientFirstName());
		cell6.setCellStyle(style);
		ICell cell30 = row.createCell(rowNumber.getAndIncrement());
		cell30.setCellValue(content.getSelfPerceivedName());
		cell30.setCellStyle(style);
		ICell cell7 = row.createCell(rowNumber.getAndIncrement());
		cell7.setCellValue(content.getIdentificationType());
		cell7.setCellStyle(style);
		ICell cell8 = row.createCell(rowNumber.getAndIncrement());
		cell8.setCellValue(content.getIdentificationNumber());
		cell8.setCellStyle(style);
		ICell cell9 = row.createCell(rowNumber.getAndIncrement());
		cell9.setCellValue(content.getBirthDate());
		cell9.setCellStyle(style);
		ICell cell10 = row.createCell(rowNumber.getAndIncrement());
		cell10.setCellValue(content.getGender());
		cell10.setCellStyle(style);
		ICell cell11 = row.createCell(rowNumber.getAndIncrement());
		cell11.setCellValue(content.getAddress());
		cell11.setCellStyle(style);
		ICell cell12 = row.createCell(rowNumber.getAndIncrement());
		cell12.setCellValue(content.getPhoneNumber());
		cell12.setCellStyle(style);
		ICell cell13 = row.createCell(rowNumber.getAndIncrement());
		cell13.setCellValue(content.getEmail());
		cell13.setCellStyle(style);

		ICell cell23 = row.createCell(rowNumber.getAndIncrement());
		cell23.setCellValue(content.getCoverageName());
		cell23.setCellStyle(style);
		ICell cell24 = row.createCell(rowNumber.getAndIncrement());
		cell24.setCellValue(content.getAffiliateNumber());
		cell24.setCellStyle(style);

		ICell cell14 = row.createCell(rowNumber.getAndIncrement());
		cell14.setCellValue(content.getStartDate().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("UTC-3")).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		cell14.setCellStyle(style);
		ICell cell15 = row.createCell(rowNumber.getAndIncrement());
		cell15.setCellValue(content.getClinicalSpecialty());
		cell15.setCellStyle(style);
		ICell cell16 = row.createCell(rowNumber.getAndIncrement());
		cell16.setCellValue(content.getProfessionalName());
		cell16.setCellStyle(style);
		ICell cell17 = row.createCell(rowNumber.getAndIncrement());
		cell17.setCellValue(content.getReasons());
		cell17.setCellStyle(style);
		ICell cell18 = row.createCell(rowNumber.getAndIncrement());
		cell18.setCellValue(content.getProblems());
		cell18.setCellStyle(style);
		ICell cell31 = row.createCell(rowNumber.getAndIncrement());
		cell31.setCellValue(content.getProcedures());
		cell31.setCellStyle(style);
		ICell cell19 = row.createCell(rowNumber.getAndIncrement());
		cell19.setCellValue(content.getWeight());
		cell19.setCellStyle(style);
		ICell cell20 = row.createCell(rowNumber.getAndIncrement());
		cell20.setCellValue(content.getHeight());
		cell20.setCellStyle(style);
		ICell cell21 = row.createCell(rowNumber.getAndIncrement());
		cell21.setCellValue(content.getSystolicBloodPressure());
		cell21.setCellStyle(style);
		ICell cell22 = row.createCell(rowNumber.getAndIncrement());
		cell22.setCellValue(content.getDiastolicBloodPressure());
		cell22.setCellStyle(style);
		ICell cell25 = row.createCell(rowNumber.getAndIncrement());
		cell25.setCellValue(content.getCardiovascularRisk());
		cell25.setCellStyle(style);
		ICell cell26 = row.createCell(rowNumber.getAndIncrement());
		cell26.setCellValue(content.getGlycosylatedHemoglobin());
		cell26.setCellStyle(style);
		ICell cell32 = row.createCell(rowNumber.getAndIncrement());
		cell32.setCellValue(content.getBloodGlucose());
		cell32.setCellStyle(style);
		ICell cell27 = row.createCell(rowNumber.getAndIncrement());
		cell27.setCellValue(content.getHeadCircunference());
		cell27.setCellStyle(style);
		ICell cell28 = row.createCell(rowNumber.getAndIncrement());
		cell28.setCellValue(content.getCpo());
		cell28.setCellStyle(style);
		ICell cell29 = row.createCell(rowNumber.getAndIncrement());
		cell29.setCellValue(content.getCeo());
		cell29.setCellStyle(style);
	}
}
