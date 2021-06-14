package net.pladema.reports.util.struct;

public interface ISheet extends Iterable<IRow> {

    IRow getRow(int rowNumber);

    IRow createRow(int rowNumber);

    void autoSizeColumns();

    int getCantRows();
}
