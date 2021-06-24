package ar.lamansys.sgx.shared.reports.util.struct;

import ar.lamansys.sgx.shared.reports.util.SheetConstants;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.OutputStream;

public class ExcelWorkbook implements IWorkbook {

    private final Workbook workbook;

    public ExcelWorkbook(Workbook workbook){
        this.workbook = workbook;
    }

    @Override
    public ISheet getSheet(int sheetNumber) {
        return new ExcelSheet(this.workbook.getSheetAt(sheetNumber));
    }

    @Override
    public ISheet createSheet(String sheetName) {
        return new ExcelSheet(this.workbook.createSheet(sheetName));
    }

    @Override
    public ICellStyle createStyle() {
        return new ExcelCellStyle(this.workbook);
    }

    @Override
    public void write(OutputStream out) throws Exception {
        this.workbook.write(out);
    }

    @Override
    public String getExtension() {
        return "xls";
    }

    @Override
    public String getContentType() {
        return SheetConstants.EXCEL_CONTENT_TYPE;
    }
}
