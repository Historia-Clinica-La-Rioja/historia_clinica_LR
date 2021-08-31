package ar.lamansys.sgx.shared.reports.util.struct;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

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
    public void setColumnWidth(int column, int pixels) {
        this.sheet.setColumnWidth(column, width(pixels));
    }

    @Override
    public void setRowHeight(int row, int pixels) {
        this.sheet.getRow(row).setHeightInPoints(height(pixels));
    }

    @Override
    public void addMergedRegion(int firstRow, int lastRow, int firstCol, int lastCol, boolean withBorders) {
        CellRangeAddress range = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
        if(withBorders){
            RegionUtil.setBorderLeft(BorderStyle.THIN, range, this.sheet);
            RegionUtil.setBorderTop(BorderStyle.THIN, range, this.sheet);
            RegionUtil.setBorderRight(BorderStyle.THIN, range, this.sheet);
            RegionUtil.setBorderBottom(BorderStyle.THIN, range, this.sheet);
        }
        this.sheet.addMergedRegion(range);
    }

    @Override
    public String getCellRangeAsString(int firstRow, int lastRow, int firstCol, int lastCol) {
        try {
            return new CellRangeAddress(firstRow, lastRow, firstCol, lastCol).formatAsString();
        } catch (IllegalArgumentException ex){
            return null;
        }
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

    private int width(int pixels){
        return (int) ((pixels * 36.56f) + 20);
    }

    private float height(int pixels){
        return pixels * 0.75f;
    }
}
