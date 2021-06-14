package net.pladema.reports.util.struct;

import org.odftoolkit.odfdom.type.Color;
import org.odftoolkit.simple.style.Font;
import org.odftoolkit.simple.style.StyleTypeDefinitions;
import org.odftoolkit.simple.table.Cell;

public class ODSCell implements ICell {

    private final Cell cell;

    public ODSCell(Cell cell){
        this.cell = cell;
    }

    @Override
    public String getValue() {
        return this.cell.getStringValue();
    }

    @Override
    public void setCellValue(String value) {
        this.cell.setStringValue(value);
    }

    @Override
    public void setCellStyle(CellStyle cellStyle) {
        Font font = new Font(
                "Arial",
                cellStyle.getBold() != null && cellStyle.getBold() ?
                        StyleTypeDefinitions.FontStyle.BOLD :
                        StyleTypeDefinitions.FontStyle.REGULAR,
                cellStyle.getFontSize() != null ? cellStyle.getFontSize() : 11,
                cellStyle.getColor() != null ? new Color(cellStyle.getColor().getODSValue()) : Color.BLACK);
        this.cell.setFont(font);
    }
}
