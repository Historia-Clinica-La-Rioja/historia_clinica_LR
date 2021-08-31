package ar.lamansys.sgx.shared.reports.util.struct;

import org.odftoolkit.simple.table.Table;

import java.util.Iterator;

public class ODSSheet implements ISheet{

    private final Table table;
    private int position;

    public ODSSheet(Table table){
        this.table = table;
        this.position = 0;
    }

    @Override
    public IRow getRow(int rowNumber) {
        return new ODSRow(this.table.getRowByIndex(rowNumber));
    }

    @Override
    public IRow createRow(int rowNumber) {
        return new ODSRow(this.table.insertRowsBefore(rowNumber,1).get(0));
    }

    @Override
    public void autoSizeColumns() {
        int cantColumns = this.table.getColumnCount();
        for (int i = 0; i < cantColumns; i++){
            this.table.getColumnByIndex(i).setUseOptimalWidth(true);
        }
    }

    @Override
    public int getCantRows() {
        return this.table.getRowCount();
    }

    @Override
    public void setColumnWidth(int column, int pixels) {
        this.table.getColumnByIndex(column).setWidth(pixels);
    }

    @Override
    public void setRowHeight(int row, int pixels) {
        this.table.getRowByIndex(row).setHeight(pixels, false);
    }

    @Override
    public void addMergedRegion(int firstRow, int lastRow, int firstCol, int lastCol, boolean withBorders) {
        //TODO review
    }

    @Override
    public String getCellRangeAsString(int firstRow, int lastRow, int firstCol, int lastCol) {
        //TODO review
        return null;
    }

    @Override
    public Iterator<IRow> iterator() {
        this.position = 0;
        int size = this.table.getRowCount();

        Iterator<IRow> i= new Iterator<IRow>() {
            @Override
            public boolean hasNext() {
                return position<size;
            }

            @Override
            public IRow next() {
                return new ODSRow(table.getRowByIndex(position++));
            }
        };
        return i;
    }
}
