package ar.lamansys.sgx.shared.reports.util.struct;

import ar.lamansys.sgx.shared.reports.util.SheetConstants;
import org.odftoolkit.simple.SpreadsheetDocument;

import java.io.OutputStream;

public class ODSWorkbook implements IWorkbook {

    private final SpreadsheetDocument doc;

    public ODSWorkbook(SpreadsheetDocument doc){
        this.doc = doc;
    }

    @Override
    public ISheet getSheet(int sheetNumber) {
        return new ODSSheet(this.doc.getSheetByIndex(sheetNumber));
    }

    @Override
    public ISheet createSheet(String sheetName) {
        return new ODSSheet(this.doc.appendSheet(sheetName));
    }

    @Override
    public ICellStyle createStyle() {
        return null;
    }

    @Override
    public void write(OutputStream out) throws Exception {
        this.doc.save(out);
    }

    @Override
    public String getExtension() {
        return "ods";
    }

    @Override
    public String getContentType() {
        return SheetConstants.ODS_CONTENT_TYPE;
    }
}
