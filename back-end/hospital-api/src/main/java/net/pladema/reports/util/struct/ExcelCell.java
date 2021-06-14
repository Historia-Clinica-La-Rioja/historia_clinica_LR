package net.pladema.reports.util.struct;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelCell implements ICell {

    private final Cell cell;

    public ExcelCell(Cell cell){
        this.cell = cell;
    }

    @Override
    public String getValue() {
        return this.cell.getStringCellValue();
    }

    @Override
    public void setCellValue(String value) {
        this.cell.setCellValue(value);
    }

    @Override
    public void setCellStyle(CellStyle cellStyle) {
        Workbook wb = cell.getRow().getSheet().getWorkbook();

        Font font = wb.createFont();
        font.setBold(cellStyle.getBold() != null && cellStyle.getBold());
        font.setFontHeightInPoints(cellStyle.getFontSize() != null ? cellStyle.getFontSize() : 11);
        font.setColor(cellStyle.getColor() != null ? cellStyle.getColor().getExcelValue() : IndexedColors.BLACK.getIndex());
        org.apache.poi.ss.usermodel.CellStyle apachePoiCellStyle = wb.createCellStyle();
        apachePoiCellStyle.setFont(font);
        cell.setCellStyle(apachePoiCellStyle);

    }
}
