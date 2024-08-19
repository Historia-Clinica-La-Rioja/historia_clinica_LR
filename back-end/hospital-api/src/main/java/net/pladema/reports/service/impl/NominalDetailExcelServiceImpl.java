package net.pladema.reports.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import net.pladema.reports.repository.InstitutionInfo;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.reports.util.CellContent;
import ar.lamansys.sgx.shared.reports.util.struct.ICell;
import ar.lamansys.sgx.shared.reports.util.struct.ICellStyle;
import ar.lamansys.sgx.shared.reports.util.struct.IRow;
import ar.lamansys.sgx.shared.reports.util.struct.ISheet;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.reports.service.NominalDetailExcelService;

@Service
public class NominalDetailExcelServiceImpl implements NominalDetailExcelService {

	@Override
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

	@Override
	public ICellStyle getFieldStyle(IWorkbook workbook) {
		ICellStyle result = workbook.createStyle();
		result.setFontSize((short)10);
		result.setBold(false);
		result.setWrap(false);
		result.setBorders(true);
		result.setHAlign(ICellStyle.HALIGNMENT.LEFT);
		result.setVAlign(ICellStyle.VALIGNMENT.BOTTOM);
		return result;
	}

	@Override
	public ICellStyle getSubTitleStyle(IWorkbook workbook) {
		ICellStyle result = workbook.createStyle();
		result.setFontSize((short)12);
		result.setBold(true);
		result.setWrap(false);
		result.setBorders(true);
		result.setHAlign(ICellStyle.HALIGNMENT.LEFT);
		result.setVAlign(ICellStyle.VALIGNMENT.BOTTOM);
		return result;
	}

	@Override
	public ICellStyle getBasicStyle(IWorkbook workbook) {
		ICellStyle result = workbook.createStyle();
		result.setFontSize((short) 12);
		result.setBold(false);
		result.setWrap(false);
		result.setBorders(true);
		result.setHAlign(ICellStyle.HALIGNMENT.CENTER);
		result.setVAlign(ICellStyle.VALIGNMENT.CENTER);
		return result;
	}



	@Override
	public void setDimensions(ISheet sheet){
		//Column's width
		sheet.autoSizeColumns();
		//Row's height
		int nRow=0;
		sheet.setRowHeight(nRow++, 50);
		sheet.setRowHeight(nRow++, 40);
		sheet.setRowHeight(nRow++, 35);

		while(nRow < sheet.getCantRows())
			sheet.setRowHeight(nRow++, 21);
	}

	@Override
	public ICellStyle createDataRowStyle(IWorkbook workbook){
		ICellStyle cellStyle = workbook.createStyle();
		cellStyle.setFontSize((short)12);
		cellStyle.setBorders(true);
		return cellStyle;
	}

	@Override
	public void fillRow(ISheet sheet, List<CellContent> data){
		Map<Integer, List<CellContent>> cellByRow = data.stream()
				.collect(Collectors.groupingBy(CellContent::getRow));

		for(Map.Entry<Integer, List<CellContent>> entry : cellByRow.entrySet()){
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

		if(data.isCellRange())
			sheet.addMergedRegion(nRow, data.lastRow(), nColumn, data.lastCol(), true);
	}


	@Override
	public void fillNominalDetailCommonColumns(IRow row, AtomicInteger rowNumber, ICellStyle style, InstitutionInfo institutionInfo,
											   String hierarchicalUnitType, String hierarchicalUnit) {
		ICell cell = row.createCell(rowNumber.getAndIncrement());
		cell.setCellValue(institutionInfo.getProvince());
		cell.setCellStyle(style);
		ICell cell2 = row.createCell(rowNumber.getAndIncrement());
		cell2.setCellValue(institutionInfo.getDepartment());
		cell2.setCellStyle(style);
		ICell cell3 = row.createCell(rowNumber.getAndIncrement());
		cell3.setCellValue(institutionInfo.getSisaCode());
		cell3.setCellStyle(style);
		ICell cell4 = row.createCell(rowNumber.getAndIncrement());
		cell4.setCellValue(institutionInfo.getInstitution());
		cell4.setCellStyle(style);
		ICell cell5 = row.createCell(rowNumber.getAndIncrement());
		cell5.setCellValue(hierarchicalUnitType);
		cell5.setCellStyle(style);
		ICell cell6 = row.createCell(rowNumber.getAndIncrement());
		cell6.setCellValue(hierarchicalUnit);
		cell6.setCellStyle(style);
	}
}
