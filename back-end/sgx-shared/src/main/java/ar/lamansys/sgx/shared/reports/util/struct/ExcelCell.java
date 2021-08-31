package ar.lamansys.sgx.shared.reports.util.struct;

import ar.lamansys.sgx.shared.reports.util.CellContent;
import org.apache.poi.ss.usermodel.Cell;

import javax.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

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
            setCellValueByType(content);
    }

    @Override
    public void setCellStyle(ICellStyle cellStyle) {
        this.cell.setCellStyle(((ExcelCellStyle)cellStyle).getStyle());
    }

    private void setCellValueByType(CellContent content){
        if(content.getValue() != null) {
            switch (content.getDataformat()) {
                case STRING:
                    this.cell.setCellValue((String) content.getValue());
                    break;
                case DOUBLE:
                    this.cell.setCellValue((Double) content.getValue());
                    break;
                case BOOLEAN:
                    this.cell.setCellValue((Boolean) content.getValue());
                    break;
                case DATE:
                    this.cell.setCellValue((Date) content.getValue());
                    break;
                case LOCALDATE:
                    this.cell.setCellValue((LocalDate) content.getValue());
                    break;
                case LOCALDATETIME:
                    this.cell.setCellValue((LocalDateTime) content.getValue());
                    break;
                case CALENDAR:
                    this.cell.setCellValue((Calendar) content.getValue());
                    break;
                default:
            }
        }
    }
}
