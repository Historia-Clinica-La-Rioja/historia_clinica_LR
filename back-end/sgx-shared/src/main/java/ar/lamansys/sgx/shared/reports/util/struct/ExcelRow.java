package ar.lamansys.sgx.shared.reports.util.struct;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.time.LocalDateTime;
import java.util.Iterator;

public class ExcelRow implements IRow {

    private final Row row;
    private int position;

    public ExcelRow(Row row){
        this.row = row;
        this.position = 0;
    }

    public ICell createCell(int cellNumber){
        return new ExcelCell(this.row.createCell(cellNumber));
    }

    @Override
    public int getCantCells() {
        if (this.row == null) return 0;
        return this.row.getPhysicalNumberOfCells();
    }

    @Override
    public int getLastCellIndex() {
        if (this.row == null) return 0;
        return this.row.getLastCellNum() - 1;
    }

    @Override
    public String getCellContentAsString(int cellNumber) {
        if (this.row.getCell(cellNumber) == null){
            return "";
        }
        switch(this.row.getCell(cellNumber).getCellType()){
            case STRING: FORMULA: BOOLEAN:
                return this.row.getCell(cellNumber).getStringCellValue();
            case NUMERIC:
                return String.format ("%.0f", this.row.getCell(cellNumber).getNumericCellValue());
            case BLANK:
                return "";
            default:
                return this.row.getCell(cellNumber).getStringCellValue();
        }
    }

    @Override
    public Number getCellContentAsNumber(int cellNumber) {
        if (this.row.getCell(cellNumber) == null){
            return null;
        }
        switch(this.row.getCell(cellNumber).getCellType()){
            case NUMERIC:
                return this.row.getCell(cellNumber).getNumericCellValue();
            case STRING:
                try {
                    return Double.parseDouble(this.row.getCell(cellNumber).getStringCellValue());
                }catch (Exception e){
                    return null;
                }
            default:
                return null;
        }
    }

    @Override
    public LocalDateTime getCellContentAsDate(int cellNumber) {
        try {
            return this.row.getCell(cellNumber).getLocalDateTimeCellValue();
        }catch (Exception e){
            return null;
        }

    }

    @Override
    public boolean isEmpty() {
        if (this.row == null) return true;
        if (getCantCells() == 0) return true;
        for (int i = 0; i <= getLastCellIndex(); i++){
            if (this.row.getCell(i) != null &&
                    (!this.row.getCell(i).getCellType().equals(CellType._NONE) && !this.row.getCell(i).getCellType().equals(CellType.BLANK))){
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<ICell> iterator() {
        this.position = 0;
        int size = this.row.getLastCellNum();

        Iterator<ICell> i= new Iterator<ICell>() {
            @Override
            public boolean hasNext() {
                return position<size;
            }

            @Override
            public ICell next() {
                return new ExcelCell(row.getCell(position++));
            }
        };
        return i;
    }
}
