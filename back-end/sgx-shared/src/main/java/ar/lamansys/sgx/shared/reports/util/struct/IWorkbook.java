package ar.lamansys.sgx.shared.reports.util.struct;

import java.io.OutputStream;

public interface IWorkbook {

    ISheet getSheet(int sheetNumber);

    ISheet createSheet(String sheetName);

    ICellStyle createStyle();

    void write(OutputStream out) throws Exception;

    String getExtension();

    String getContentType();

}
