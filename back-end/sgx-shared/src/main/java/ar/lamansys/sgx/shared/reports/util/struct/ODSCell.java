package ar.lamansys.sgx.shared.reports.util.struct;

import ar.lamansys.sgx.shared.reports.util.CellContent;
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
    public void setCellValue(CellContent content) {
        if(content.isFormula())
            this.cell.setFormula((String)content.getValue());
        else
            setCellValue((String)content.getValue());
    }

    @Override
    public void setCellStyle(ICellStyle cellStyle) {
        this.cell.setFont(((ODSStyle)cellStyle).getFont());
    }
}
