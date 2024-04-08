package net.pladema.reports.service;

import ar.lamansys.sgx.shared.reports.util.CellContent;
import ar.lamansys.sgx.shared.reports.util.struct.ICellStyle;
import ar.lamansys.sgx.shared.reports.util.struct.ISheet;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;

import java.util.List;

public interface NominalDetailExcelService {

	void fillRow(ISheet sheet, List<CellContent> data);

	ICellStyle createDataRowStyle(IWorkbook workbook);

	void setDimensions(ISheet sheet);

	ICellStyle getTitleStyle(IWorkbook workbook);

	ICellStyle getFieldStyle(IWorkbook workbook);

	ICellStyle getSubTitleStyle(IWorkbook workbook);

	ICellStyle getBasicStyle(IWorkbook workbook);

}
