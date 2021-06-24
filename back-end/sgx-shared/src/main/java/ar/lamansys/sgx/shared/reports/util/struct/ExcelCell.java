package ar.lamansys.sgx.shared.reports.util.struct;

import ar.lamansys.sgx.shared.reports.util.CellContent;
import org.apache.poi.ss.usermodel.Cell;

import javax.validation.constraints.NotNull;

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
    public void setCellValue(@NotNull CellContent content) {
        if(content.isFormula()) {
            String value = ((String) content.getValue()).replace("=","");
            this.cell.setCellFormula(value);
        }
        else
            setCellValue((String) content.getValue());
    }

    @Override
    public void setCellStyle(ICellStyle cellStyle) {
        this.cell.setCellStyle(((ExcelCellStyle)cellStyle).getStyle());
    }
}
