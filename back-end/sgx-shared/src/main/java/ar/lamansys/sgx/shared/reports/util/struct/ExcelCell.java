package ar.lamansys.sgx.shared.reports.util.struct;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
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

        //Font Style
        Font font = wb.createFont();
        font.setBold(cellStyle.isBold());
        font.setFontHeightInPoints(cellStyle.getFontSize());
        font.setColor(cellStyle.getColor() != null ? cellStyle.getColor().getExcelValue() : IndexedColors.BLACK.getIndex());

        org.apache.poi.ss.usermodel.CellStyle apachePoiCellStyle = wb.createCellStyle();
        apachePoiCellStyle.setFont(font);

        //Alignment
        apachePoiCellStyle.setAlignment(getHAlignment(cellStyle));
        apachePoiCellStyle.setVerticalAlignment(getVAlignment(cellStyle));

        //Wrap text
        apachePoiCellStyle.setWrapText(cellStyle.isWrap());

        //Borders
        if(cellStyle.isBorders()) {
            apachePoiCellStyle.setBorderTop(BorderStyle.THIN);
            apachePoiCellStyle.setBorderBottom(BorderStyle.THIN);
            apachePoiCellStyle.setBorderLeft(BorderStyle.THIN);
            apachePoiCellStyle.setBorderRight(BorderStyle.THIN);
        }
        cell.setCellStyle(apachePoiCellStyle);
    }

    private VerticalAlignment getVAlignment(CellStyle cellStyle) {
        switch (cellStyle.getVAlign()){
            case TOP:
                return VerticalAlignment.TOP;
            case CENTER:
                return VerticalAlignment.CENTER;
            case BOTTOM:
                return VerticalAlignment.BOTTOM;
            default:
                return VerticalAlignment.BOTTOM;
        }
    }

    private HorizontalAlignment getHAlignment(CellStyle cellStyle){
        switch (cellStyle.getHalign()){
            case LEFT:
                return HorizontalAlignment.LEFT;
            case CENTER:
                return HorizontalAlignment.CENTER;
            case RIGHT:
                return HorizontalAlignment.RIGHT;
            default:
                return HorizontalAlignment.LEFT;
        }
    }
}
