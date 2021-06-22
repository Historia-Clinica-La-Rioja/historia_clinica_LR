package ar.lamansys.sgx.shared.reports.util.struct;

public interface ISheet extends Iterable<IRow> {

    IRow getRow(int rowNumber);

    IRow createRow(int rowNumber);

    void autoSizeColumns();

    int getCantRows();

    void setColumnWidth(int column, int pixels);

    void setRowHeight(int row, int pixels);

    void addMergedRegion(int firstRow, int lastRow, int firstCol, int lastCol, boolean withBorders);

    String getCellRangeAsString(int firstRow, int lastRow, int firstCol, int lastCol);
}
