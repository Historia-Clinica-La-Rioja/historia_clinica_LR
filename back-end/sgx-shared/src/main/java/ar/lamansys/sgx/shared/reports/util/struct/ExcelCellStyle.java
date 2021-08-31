package ar.lamansys.sgx.shared.reports.util.struct;

import lombok.Getter;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

@Getter
public class ExcelCellStyle implements ICellStyle {

    private CellStyle style;

    private Font font;

    private short fontSize;
    private boolean bold;
    private Short color;

    private boolean wrap;
    private VALIGNMENT vAlign;
    private HALIGNMENT hAlign;
    private boolean borders;

    public ExcelCellStyle(Workbook workbook){
        this.font = workbook.createFont();
        this.style = workbook.createCellStyle();
        this.style.setFont(this.font);

        setFontColor(IndexedColors.BLACK.getIndex());
        setFontSize((short)11);
        setBold(false);
        setWrap(false);
        setVAlign(VALIGNMENT.BOTTOM);
        setHAlign(HALIGNMENT.LEFT);
        setBorders(false);
    }

    @Override
    public void setFontColor(Object value) {
        this.color = (short) value;
        this.font.setColor(this.color);
    }

    @Override
    public void setFontSize(short size) {
        this.fontSize = size;
        this.font.setFontHeightInPoints(size);
    }

    @Override
    public void setBold(boolean boldFont) {
        this.bold = boldFont;
        this.font.setBold(boldFont);
    }

    @Override
    public void setWrap(boolean wrapText) {
        this.wrap = wrapText;
        this.style.setWrapText(wrapText);
    }

    @Override
    public void setBorders(boolean withBorders) {
        this.borders = withBorders;
        if(withBorders){
            this.style.setBorderTop(BorderStyle.THIN);
            this.style.setBorderBottom(BorderStyle.THIN);
            this.style.setBorderLeft(BorderStyle.THIN);
            this.style.setBorderRight(BorderStyle.THIN);
        }
    }

    @Override
    public void setHAlign(HALIGNMENT horizontalAlign) {
        this.hAlign = horizontalAlign;
        switch (horizontalAlign){
            case LEFT:
                this.style.setAlignment(HorizontalAlignment.LEFT);
                break;
            case CENTER:
                this.style.setAlignment(HorizontalAlignment.CENTER);
                break;
            case RIGHT:
                this.style.setAlignment(HorizontalAlignment.RIGHT);
                break;
            default:
        }
    }

    @Override
    public void setVAlign(VALIGNMENT verticalAlign) {
        this.vAlign = verticalAlign;
        switch (verticalAlign){
            case TOP:
                this.style.setVerticalAlignment(VerticalAlignment.TOP);
                break;
            case CENTER:
                this.style.setVerticalAlignment(VerticalAlignment.CENTER);
                break;
            case BOTTOM:
                this.style.setVerticalAlignment(VerticalAlignment.BOTTOM);
                break;
            default:
        }
    }
}
