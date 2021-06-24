package net.pladema.reports.service.impl;

import ar.lamansys.sgx.shared.reports.util.manager.WorkbookCreator;
import ar.lamansys.sgx.shared.reports.util.struct.ICell;
import ar.lamansys.sgx.shared.reports.util.struct.ICellStyle;
import ar.lamansys.sgx.shared.reports.util.struct.IRow;
import ar.lamansys.sgx.shared.reports.util.struct.ISheet;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.reports.service.ExcelService;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ExcelServiceImpl implements ExcelService {

    private static short FONT_SIZE = 12;

    @Override
    public IWorkbook buildExcelFromQuery(String title, String[] headers, Query query) {
        // creo el workbook de excel
        IWorkbook wb = WorkbookCreator.createExcelWorkbook();

        // obtengo los resultados desde la query
        List<Object> result = query.getResultList();

        // creo la hoja con el titulo pasado como parametro
        ISheet sheet = wb.createSheet(title);

        AtomicInteger rowNumber = new AtomicInteger(0);
        // creo la columna de headers
        IRow headerRow = sheet.createRow(rowNumber.getAndIncrement());

        ICellStyle styleHeader = createHeaderStyle(wb);

        fillRow(headerRow, headers, styleHeader);

        ICellStyle styleDataRow = createDataRowStyle(wb);

        // itero el resultado creando una row para cada uno
        result.stream().forEach(
                resultData -> {
                    IRow newDataRow = sheet.createRow(rowNumber.getAndIncrement());
                    fillRow(newDataRow, (Object[]) resultData, styleDataRow);
                }
        );

        sheet.autoSizeColumns();

        return wb;
    }

    private ICellStyle createHeaderStyle(IWorkbook workbook){
        ICellStyle cellStyle = workbook.createStyle();
        cellStyle.setBold(true);
        cellStyle.setFontSize(FONT_SIZE);

        return cellStyle;
    }

    private ICellStyle createDataRowStyle(IWorkbook workbook){
        ICellStyle cellStyle = workbook.createStyle();
        cellStyle.setFontSize(FONT_SIZE);

        return cellStyle;
    }

    private void fillRow(IRow row, Object[] content, ICellStyle style){
        AtomicInteger rowNumber = new AtomicInteger(0);
        Arrays.stream(content).forEach(
                item -> {
                    ICell cell = row.createCell(rowNumber.getAndIncrement());
                    cell.setCellValue(item != null ? item.toString() : "");
                    cell.setCellStyle(style);
                }
        );
    }
}
