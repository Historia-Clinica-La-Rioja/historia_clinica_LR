package ar.lamansys.sgx.shared.reports.util.struct;

import org.odftoolkit.simple.table.Row;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Iterator;

public class ODSRow implements IRow {

    private final Row row;
    private int posicion;

    public ODSRow(Row row){
        this.row = row;
        this.posicion = 0;
    }

    public ICell createCell(int cellNumber){
        return new ODSCell(this.row.getCellByIndex(cellNumber));
    }

    @Override
    public int getCantCells() {
        return this.row.getCellCount();
    }

    @Override
    public int getLastCellIndex() {
        return this.row.getCellCount() - 1;
    }

    @Override
    public String getCellContentAsString(int cellNumber) {
        return this.row.getCellByIndex(cellNumber).getStringValue();
    }

    @Override
    public Number getCellContentAsNumber(int cellNumber) {
        try {
            return this.row.getCellByIndex(cellNumber).getDoubleValue();
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public LocalDateTime getCellContentAsDate(int cellNumber) {
        try {
            return this.row.getCellByIndex(cellNumber).getDateValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public boolean isEmpty() {
        if (this.row == null) return true;
        if (getCantCells() == 0) return true;
        for (int i = 0; i < getCantCells(); i++){
            if (this.row.getCellByIndex(i) != null &&
                    !this.row.getCellByIndex(i).getDisplayText().equals("")){
                return false;
            }
        }
        return true;
    }


    @Override
    public Iterator<ICell> iterator() {
        this.posicion = 0;
        int size = this.row.getCellCount();

        Iterator<ICell> i= new Iterator<ICell>() {
            @Override
            public boolean hasNext() {
                return posicion<size;
            }

            @Override
            public ICell next() {
                return new ODSCell(row.getCellByIndex(posicion++));
            }
        };
        return i;
    }
}
