package ar.lamansys.sgx.shared.reports.util.struct;

import lombok.Getter;
import org.odftoolkit.odfdom.incubator.doc.office.OdfOfficeStyles;
import org.odftoolkit.odfdom.type.Color;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.style.Font;
import org.odftoolkit.simple.style.StyleTypeDefinitions;

@Getter
public class ODSStyle implements ICellStyle {

    private OdfOfficeStyles style;
    private Font font;

    private String color;
    private short fontSize;
    private boolean bold;

    public ODSStyle(SpreadsheetDocument doc){
        this.style=doc.getOrCreateDocumentStyles();

        setFontSize((short)11);
        setBold(false);

        this.font = new Font(
                "Arial",
                isBold() ?
                        StyleTypeDefinitions.FontStyle.BOLD :
                        StyleTypeDefinitions.FontStyle.REGULAR,
                getFontSize(),
                getColor() != null ? new Color(getColor()) : Color.BLACK);
    }


    @Override
    public void setFontColor(Object value) {
        this.color = (String) value;
    }

    @Override
    public void setFontSize(short size) {

    }

    @Override
    public void setBold(boolean boldFont) {

    }

    @Override
    public boolean isWrap() {
        return false;
    }

    @Override
    public void setWrap(boolean wrapText) {

    }

    @Override
    public boolean isBorders() {
        return false;
    }

    @Override
    public void setBorders(boolean withBorders) {

    }

    @Override
    public HALIGNMENT getHAlign() {
        return null;
    }

    @Override
    public void setHAlign(HALIGNMENT horizontalAlign) {

    }

    @Override
    public VALIGNMENT getVAlign() {
        return null;
    }

    @Override
    public void setVAlign(VALIGNMENT verticalAlign) {

    }
}
