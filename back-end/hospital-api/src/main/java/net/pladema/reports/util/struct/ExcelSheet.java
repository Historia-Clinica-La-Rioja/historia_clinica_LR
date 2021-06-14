package net.pladema.reports.util.struct;

import org.apache.poi.ss.usermodel.Sheet;

import java.util.Iterator;

public class ExcelSheet implements ISheet {

    private final Sheet sheet;
    private int position;

    public ExcelSheet(Sheet sheet){
        this.sheet = sheet;
        this.position = 0;
    }

    @Override
    public IRow getRow(int rowNumber) {
        return new ExcelRow(this.sheet.getRow(rowNumber));
    }

    @Override
    public IRow createRow(int rowNumber) {
        return new ExcelRow(this.sheet.createRow(rowNumber));
    }

    @Override
    public void autoSizeColumns() {
        int cantColumns = this.sheet.getRow(0).getLastCellNum();
        for (int i = 0; i < cantColumns; i++){
            this.sheet.autoSizeColumn(i);
        }
    }

    @Override
    public int getCantRows() {
        return this.sheet.getPhysicalNumberOfRows();
    }

    @Override
    public Iterator<IRow> iterator() {
        this.position = 0;
        int size = this.sheet.getPhysicalNumberOfRows();

        Iterator<IRow> i= new Iterator<IRow>() {
            @Override
            public boolean hasNext() {
                return position<size;
            }

            @Override
            public IRow next() {
                return new ExcelRow(sheet.getRow(position++));
            }
        };
        return i;
    }
}
