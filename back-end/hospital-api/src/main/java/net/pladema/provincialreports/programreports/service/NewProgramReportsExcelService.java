package net.pladema.provincialreports.programreports.service;

import ar.lamansys.sgx.shared.reports.util.manager.WorkbookCreator;
import ar.lamansys.sgx.shared.reports.util.struct.ICell;
import ar.lamansys.sgx.shared.reports.util.struct.ICellStyle;
import ar.lamansys.sgx.shared.reports.util.struct.IRow;
import ar.lamansys.sgx.shared.reports.util.struct.ISheet;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;

import net.pladema.establishment.repository.entity.Institution;
import net.pladema.provincialreports.programreports.repository.EpidemiologyOneConsultationDetail;

import net.pladema.provincialreports.reportformat.DateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.provincialreports.reportformat.domain.service.ReportExcelUtilsService;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class NewProgramReportsExcelService {


	@Autowired
	private final DateFormat dateTools;

	@Autowired
	public ReportExcelUtilsService excelUtilsService;

	public NewProgramReportsExcelService(InstitutionRepository institutionRepository, DateFormat dateTools) {
		this.dateTools = dateTools;
	}

	public IWorkbook buildEpidemiologyOneExcel (
			String title, String[] headers, List<EpidemiologyOneConsultationDetail> result, Integer institutionId, LocalDate startDate, LocalDate endDate
	) {

		IWorkbook workbook = WorkbookCreator.createExcelWorkbook();

		excelUtilsService.createHeaderCellsStyle(workbook);

		ISheet sheet = workbook.createSheet(title);

		excelUtilsService.fillRow(sheet, excelUtilsService.getHeaderDataWithoutObservation(headers, title, 7, excelUtilsService.periodStringFromLocalDates(startDate, endDate), institutionId));

		AtomicInteger rowNumber = new AtomicInteger(sheet.getCantRows());

		ICellStyle dataCellsStyle = excelUtilsService.getDataCellsStyle(workbook);

		result.forEach(
				resultData -> {
					IRow row = sheet.createRow(rowNumber.getAndIncrement());
					fillEpidemiologyOneRow(row, resultData, dataCellsStyle);
				}
		);

		excelUtilsService.setSheetDimensions(sheet);

		return workbook;
	}

	public void fillEpidemiologyOneRow(IRow row, EpidemiologyOneConsultationDetail content, ICellStyle style) {
		AtomicInteger cellNumber = new AtomicInteger(0);

		ICell cell0 = row.createCell(cellNumber.getAndIncrement());
		cell0.setCellStyle(style);
		cell0.setCellValue(content.getPatientFullName());

		ICell cell1 = row.createCell(cellNumber.getAndIncrement());
		cell1.setCellStyle(style);
		cell1.setCellValue(content.getCoding());

		ICell cell2 = row.createCell(cellNumber.getAndIncrement());
		cell2.setCellStyle(style);
		cell2.setCellValue(dateTools.dateFromYMDToDMY(content.getBirthDate()));

		ICell cell3 = row.createCell(cellNumber.getAndIncrement());
		cell3.setCellStyle(style);
		cell3.setCellValue(content.getGender());

		ICell cell4 = row.createCell(cellNumber.getAndIncrement());
		cell4.setCellStyle(style);
		cell4.setCellValue(dateTools.dateFromYMDHMSNOToDMY(content.getStartDate()));

		ICell cell5 = row.createCell(cellNumber.getAndIncrement());
		cell5.setCellStyle(style);
		cell5.setCellValue(content.getDepartment());

		ICell cell6 = row.createCell(cellNumber.getAndIncrement());
		cell6.setCellStyle(style);
		cell6.setCellValue(content.getAddress());

		ICell cell7 = row.createCell(cellNumber.getAndIncrement());
		cell7.setCellStyle(style);
		cell7.setCellValue(content.getCie10Codes());

		ICell cell8 = row.createCell(cellNumber.getAndIncrement());
		cell8.setCellStyle(style);
		cell8.setCellValue(content.getIdentificationNumber());

		ICell cell9 = row.createCell(cellNumber.getAndIncrement());
		cell9.setCellStyle(style);
		cell9.setCellValue(content.getProblems());

	}

}
