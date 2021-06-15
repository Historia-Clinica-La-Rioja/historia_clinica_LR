package ar.lamansys.sgx.shared.reports.util.manager;

import ar.lamansys.sgx.shared.reports.util.struct.ExcelWorkbook;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import ar.lamansys.sgx.shared.reports.util.struct.ODSWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.odftoolkit.simple.SpreadsheetDocument;

public class WorkbookCreator {

    public static IWorkbook createExcelWorkbook(){
        return new ExcelWorkbook(new HSSFWorkbook());
    };

    public static IWorkbook createOdsWorkbook() {
        try {
            SpreadsheetDocument doc = SpreadsheetDocument.newSpreadsheetTemplateDocument();
            doc.removeSheet(0);
            return new ODSWorkbook(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
