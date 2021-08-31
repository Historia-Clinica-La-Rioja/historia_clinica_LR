package ar.lamansys.sgx.shared.reports.util.struct;

import ar.lamansys.sgx.shared.reports.util.CellContent;

public interface ICell {

    String getValue();

     void setCellValue(String value);

    void setCellValue(CellContent content);

    void setCellStyle(ICellStyle cellStyle);
}
